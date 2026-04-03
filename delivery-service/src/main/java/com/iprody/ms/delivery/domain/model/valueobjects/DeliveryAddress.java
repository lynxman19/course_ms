package com.iprody.ms.delivery.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class DeliveryAddress {
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column
    private String zipCode;

    @Column
    private String state;

    @Column(nullable = false)
    private String country;

    public DeliveryAddress(String street, String city, String state, String zipCode, String country) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Для street должно быть указано непустое значение");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("Для city должно быть указано непустое значение");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Для country должно быть указано непустое значение");
        }
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}
