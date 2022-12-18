package kitchenpos.order.ui.response;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

	private final long id;
	private final LocalDateTime createdDate;
	private final List<OrderTableResponse> orderTables;

	private TableGroupResponse(long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		return new TableGroupResponse(id, createdDate, orderTables);
	}

	public long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}
