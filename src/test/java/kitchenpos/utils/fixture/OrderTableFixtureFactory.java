package kitchenpos.utils.fixture;

import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }
    public static OrderTable createOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}
