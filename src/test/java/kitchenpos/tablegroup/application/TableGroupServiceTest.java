package kitchenpos.tablegroup.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.message.OrderTableMessage;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableRequest;
import kitchenpos.tablegroup.message.TableGroupMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("한가지 메뉴");
        MenuProduct menuProduct = MenuProduct.of(ProductFixture.후라이드, 1L);
        Menu menu = Menu.of("후라이드치킨", 15_000L, menuGroup, Arrays.asList(menuProduct));
        orderLineItems = Arrays.asList(OrderLineItem.of(menu, 1L));
    }

    @Test
    @DisplayName("테이블 그룹 지정시 성공하고 그룹 정보를 반환한다")
    void createTableGroupThenReturnResponseTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L),
                new TableRequest(3L)
        ));
        TableGroup tableGroup = new TableGroup(orderTables);
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
        then(tableGroupRepository).should(times(1)).save(any());
        boolean isNotEmptyAllTables = response.getOrderTables().stream().noneMatch(OrderTableResponse::isEmpty);
        assertAll(
                () -> assertThat(isNotEmptyAllTables).isTrue(),
                () -> assertThat(tableGroup.getOrderTables().getAll()).containsAll(orderTables)
        );
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 2개 미만인경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByLessThanTwoTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(OrderTable.of(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(new TableRequest(1L)));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 등록된 주문 테이블 개수가 다른경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByNotEqualTableSizeTest() {
        // given
        List<OrderTable> storedOrderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(storedOrderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_NOT_EQUAL_TABLE_SIZE.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 사용중인경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByTableIsNotEmptyTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, false), // 사용중인 주문 테이블
                OrderTable.of(2, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_ORDER_TABLE_IS_NOT_EMPTY.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 다른 그룹에 속한경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByEnrolledOtherGroupTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);
        List<OrderTable> otherOrderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroup otherTableGroup = new TableGroup(otherOrderTables);
        otherTableGroup.group();
        List<OrderTable> orderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블 그룹 해지에 성공한다")
    void unGroupTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true)
        );
        TableGroup tableGroup = new TableGroup(orderTables);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(1L);

        // then
        then(tableGroupRepository).should(times(1)).findById(any());
        assertThat(orderTables.stream().noneMatch(OrderTable::isEnrolledGroup)).isTrue();
    }

    @Test
    @DisplayName("테이블 그룹 해지시 속해져있는 테이블중 조리 또는 식사 상태인경우 예외처리되어 해지에 실패한다")
    void unGroupTablesThrownByCookingOrMealOrderStatesTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);
        List<OrderTable> orderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroup.group();

        Order order = Order.cooking(orderTable, orderLineItems);
        order.changeState(OrderStatus.COMPLETION);

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(tableGroupRepository).should(times(1)).findById(any());
    }
}
