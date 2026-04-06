package com.iprody.ms.payment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@NoArgsConstructor
public class PaymentAmount {
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    public PaymentAmount(BigDecimal amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Payment amount must be greater than or equal to zero");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
