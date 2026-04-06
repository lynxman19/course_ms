package com.iprody.ms.payment.domain.model.aggregate;

import com.iprody.ms.payment.domain.model.valueobjects.PaymentAmount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import  com.iprody.ms.payment.domain.model.valueobjects.PaymentStatus;
import  com.iprody.ms.payment.domain.model.valueobjects.PaymentMethod;

@Entity
@Table(schema = "payment_service", name = "payments")
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PaymentMethod method;

    @Embedded
    private PaymentAmount amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Payment(Long orderId, PaymentStatus status, PaymentMethod method, PaymentAmount amount) {
        setOrderId(orderId);
        setStatus(status);
        setMethod(method);
        setAmount(amount);
    }

    public void update(Long orderId, PaymentStatus status, PaymentMethod method, PaymentAmount amount) {
        setOrderId(orderId);
        setStatus(status);
        setMethod(method);
        setAmount(amount);
    }

    public void setOrderId(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Идентификатор заказа должен быть больше 0");
        }
        this.orderId = orderId;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status == null ? PaymentStatus.PENDING : status;
    }

    public void setMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Способ оплаты должен быть указан");
        }
        this.method = method;
    }

    public void setAmount(PaymentAmount amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Оплата должна быть указана");
        }
        this.amount = amount;
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }
}
