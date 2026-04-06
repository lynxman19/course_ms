package com.iprody.ms.order.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
public class Money {
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    public Money(BigDecimal totalPrice) {
        if (totalPrice == null) {
            throw new IllegalArgumentException("Стоимость должна быть больше или равной 0");
        }
        this.price = totalPrice;
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(price.add(other.price));
    }

    public Money multiply(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товаров должно быть больше 0");
        }
        return new Money(price.multiply(BigDecimal.valueOf(quantity)));
    }
}
