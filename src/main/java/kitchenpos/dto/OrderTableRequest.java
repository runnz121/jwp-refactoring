package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, numberOfGuests, empty);
    }
}
