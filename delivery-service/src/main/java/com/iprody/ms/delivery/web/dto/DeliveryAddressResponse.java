package com.iprody.ms.delivery.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryAddressResponse {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
