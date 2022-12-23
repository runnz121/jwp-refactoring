package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final String TABLE_GROUP_REGISTERED_EXCEPTION = "테이블 그룹이 등록되어 상태를 변경할 수 없습니다.";
    private static final String ORDER_IS_NOT_COMPLETE_EXCEPTION = "완료되지 않은 주문이 있어 상태를 변경할 수 없습니다.";
    private static final String INVALID_NUMBER_EXCEPTION = "손님 수는 0 보다 작을 수 없습니다.";
    private static final String EMPTY_TABLE_EXCEPTION = "빈 테이블의 손님 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {

    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(Order order, boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(TABLE_GROUP_REGISTERED_EXCEPTION);
        }

        if (!order.isOrderComplete()) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETE_EXCEPTION);
        }

        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(INVALID_NUMBER_EXCEPTION);
        }

        if (empty) {
            throw new IllegalArgumentException(EMPTY_TABLE_EXCEPTION);
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unGroup(Order order) {

        if (!order.isOrderComplete()) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETE_EXCEPTION);
        }

        this.tableGroup = null;
        this.empty = true;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
