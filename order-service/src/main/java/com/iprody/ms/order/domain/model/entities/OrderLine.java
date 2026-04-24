package com.iprody.ms.order.domain.model.entities;

import com.iprody.ms.order.domain.model.valueobjects.Money;
import jakarta.persistence.*;

import com.iprody.ms.order.domain.model.aggregate.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(schema = "order_service", name = "order_lines")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long orderLineId;

    @Column
    private String productName;

    @Column
    private int quantity;

    @Embedded
    private Money price;

    public OrderLine(String productName, Integer quantity, Money money) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Необходимо ввести название товара");
        }
        this.productName = productName.trim();

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Количество товаров должно быть выше 0");
        }
        this.quantity = quantity;

        if (money == null) {
            throw new IllegalArgumentException("Необходимо ввести стоимость товара");
        }
        this.price = money;
    }
}
