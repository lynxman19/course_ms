package com.iprody.ms.delivery.service.dto;

public record DeliveryAddressDto(
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {
}
