package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {
    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Autowired
    TableService tableService;

    @Test
    void 빈_테이블_생성() {
        OrderTable savedOrderTable = serviceTestHelper.빈테이블_생성됨();

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 비어있지않은_테이블_생성() {
        int numberOfGuests = 4;
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 테이블_목록_조회() {
        serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.빈테이블_생성됨();
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 빈_테이블로_변경() {
        int numberOfGuests = 4;
        OrderTable orderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);
        OrderTable updatedOrderTable = serviceTestHelper.빈테이블로_변경(orderTable.getId());

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isTrue();
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 비어있지않은_테이블로_변경() {
        OrderTable orderTable = serviceTestHelper.빈테이블_생성됨();
        OrderTable updatedOrderTable = serviceTestHelper.비어있지않은테이블로_변경(orderTable.getId());

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isFalse();
        assertThat(updatedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블_공석상태변경_테이블이_존재하지않는경우() {
        OrderTable notSavedOrderTable = OrderTableFixtureFactory.createEmptyOrderTable();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.비어있지않은테이블로_변경(notSavedOrderTable.getId()));
    }

    @Test
    void 테이블_공석상태변경_테이블그룹에_포함된경우() {
        OrderTable emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable, emptyTable2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.비어있지않은테이블로_변경(emptyTable.getId()));
    }

    @Test
    void 테이블_공석상태변경_주문이_조리_식사상태인경우() {
        OrderTable table = serviceTestHelper.비어있지않은테이블_생성됨(3);
        Order order = 테이블에_임시_주문_추가(table.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.빈테이블로_변경(table.getId()));
    }

    @Test
    void 테이블_인원수_변경() {
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);
        int updatedNumberOfGuests = 3;
        OrderTable updatedOrderTable = serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), updatedNumberOfGuests);

        assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(updatedNumberOfGuests);
    }

    @Test
    void 테이블_인원수_변경_음수로_변경시도() {
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);

        int invalidNumberOfGuests = -5;
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), invalidNumberOfGuests));
    }

    @Test
    void 테이블_인원수_변경_빈테이블인_경우() {
        OrderTable savedOrderTable = serviceTestHelper.빈테이블_생성됨();
        int updatedNumberOfGuests = 4;
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), updatedNumberOfGuests));
    }

    private Order 테이블에_임시_주문_추가(Long tableId) {
        MenuGroup menuGroup = serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        Product product1 = serviceTestHelper.상품_생성됨("상품1", 1000);
        MenuProduct menuProduct = MenuProductFixtureFactory.createMenuProduct(product1.getId(), 4);
        Menu menu = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴1", 4000, Lists.newArrayList(menuProduct));
        OrderLineItem orderLineItem = OrderLineItemFixtureFactory.createOrderLine(menu.getId(), 3);
        return serviceTestHelper.주문_생성됨(tableId, Lists.newArrayList(orderLineItem));
    }
}
