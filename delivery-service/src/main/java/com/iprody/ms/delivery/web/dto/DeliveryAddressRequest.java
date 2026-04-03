package com.iprody.ms.delivery.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressRequest {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
