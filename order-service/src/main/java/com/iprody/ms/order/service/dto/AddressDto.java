package com.iprody.ms.order.service.dto;

public record AddressDto (
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {
}
