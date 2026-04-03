package com.iprody.ms.order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressResponse {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
