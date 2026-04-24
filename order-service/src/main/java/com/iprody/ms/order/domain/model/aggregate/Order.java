package com.iprody.ms.order.domain.model.aggregate;

import com.iprody.ms.order.domain.model.entities.OrderLine;
import com.iprody.ms.order.domain.model.valueobjects.Address;
import com.iprody.ms.order.domain.model.valueobjects.Money;
import com.iprody.ms.order.domain.model.valueobjects.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "order_service", name = "orders")
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OrderStatus status;

    @Embedded
    private Money totalPrice;

    @Embedded
    private Address address;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLine> orderLines = new ArrayList<>();

    @Column(nullable = false)
    private Long customerId;

    public Order(Long customerId, OrderStatus status, Address address, List<OrderLine> orderLines) {
        setCustomerId(customerId);
        setStatus(status);
        setAddress(address);
        replaceOrderLines(orderLines);
    }

    public void update(Long customerId, OrderStatus status, Address address, List<OrderLine> orderLines) {
        setCustomerId(customerId);
        setStatus(status);
        setAddress(address);
        replaceOrderLines(orderLines);
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = OrderStatus.NEW;
        }
        if (totalPrice == null) {
            recalculateTotalPrice();
        }
    }

    @PreUpdate
    public void onUpdate() {
        recalculateTotalPrice();
    }

    public void setCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Идентификатор заказчика должен быть больше 0");
        }
        this.customerId = customerId;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Не введен адрес");
        }
        this.address = address;
    }

    public void setStatus(OrderStatus status) {
        this.status = status == null ? OrderStatus.NEW : status;
    }

    public void replaceOrderLines(List<OrderLine> newOrderLines) {
        orderLines.clear();
        if (newOrderLines != null) {
            orderLines.addAll(newOrderLines);
        }
        recalculateTotalPrice();
    }

    private void recalculateTotalPrice() {
        if (orderLines.isEmpty()) {
            totalPrice = Money.zero();
            return;
        }

        Money sum = Money.zero();
        for (OrderLine orderLine : orderLines) {
            sum = sum.add(orderLine.getPrice().multiply(orderLine.getQuantity()));
        }
        totalPrice = sum;
    }
}
