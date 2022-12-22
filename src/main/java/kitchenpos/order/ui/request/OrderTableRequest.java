package kitchenpos.order.ui.request;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableEmpty;

public class OrderTableRequest {
	private final int numberOfGuests;
	private final boolean empty;

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toEntity() {
		return OrderTable.of(NumberOfGuests.from(numberOfGuests), TableEmpty.from(empty));
	}
}
