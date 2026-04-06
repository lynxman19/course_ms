package com.iprody.ms.order.domain.model.entities;

import com.iprody.ms.order.domain.model.valueobjects.Email;
import jakarta.persistence.*;

@Entity
@Table(schema = "order_service", name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long customerId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Email email;
}
