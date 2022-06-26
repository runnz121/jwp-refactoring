package kitchenpos.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    protected Order() {
    }

    public Order(OrderStatus orderStatus, OrderLineItems orderLineItems, LocalDateTime orderedTime, OrderTable orderTable) {
        validate(orderTable, orderLineItems);
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
    }

    public Order(Long id, OrderStatus orderStatus, LocalDateTime orderedTime, OrderTable orderTable) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
    }

    private void validate(OrderTable orderTable, OrderLineItems orderLineItems) {
        if (orderTable.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈테이블인 경우 주문을 등록 할 수 없습니다.");
        }
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 항목이 없는 경우 주문을 등록 할 수 없습니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validatePossibleChangOrder();
        this.orderStatus = orderStatus;
    }

    private void validatePossibleChangOrder() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("[ERROR] 계산완료상태에서 주문 상태를 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

}
