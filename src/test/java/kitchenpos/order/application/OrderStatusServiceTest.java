package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.application.OrderService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 서비스")
class OrderStatusServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;


    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("a"));
        Menu menu = menuRepository.save(new Menu(new Name("menu"), new Price(BigDecimal.ONE), menuGroup, Arrays.asList(new MenuProduct(null, ProductFixture.product(), 1L))));

        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(false));

        List<OrderTable> orderTables = new ArrayList<>();

        orderTable1.empty();
        orderTable2.empty();

        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        orderTable1.setTableGroup(tableGroup);
        orderTable2.setTableGroup(tableGroup);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        createOrder(orderTable1, menu);
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    private void createOrder(OrderTable orderTable1, Menu menu) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        orderTable1.setEmpty(false);
        order = orderRepository.save(new Order(orderTable1, orderLineItems));
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void statusMeal_success() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThat(orderService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeStatus_fail() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COMPLETION);

        assertThat(orderService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.COMPLETION.name());

        주문완료_검증됨();

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void name() {
        orderService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        주문완료_검증됨();
    }

    private void 주문완료_검증됨() {
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
