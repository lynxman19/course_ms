package com.iprody.ms.order.web.mapper;

import com.iprody.ms.order.service.dto.AddressDto;
import com.iprody.ms.order.service.dto.MoneyDto;
import com.iprody.ms.order.service.dto.OrderDto;
import com.iprody.ms.order.service.dto.OrderLineDto;
import com.iprody.ms.order.service.execute.OrderExecute;
import com.iprody.ms.order.web.dto.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderMapper {
    public OrderExecute mapToExecuteOrder(OrderRequest orderRequest) {
        return new OrderExecute(
                orderRequest.getCustomerId(),
                orderRequest.getStatus(),
                mapToAddressDto(orderRequest.getAddress()),
                mapToOrderLinesDto(orderRequest.getOrderLines()));

    }

    public OrderResponse mapToOrderResponse(OrderDto orderDto) {
        return new OrderResponse(
                orderDto.id(),
                orderDto.customerId(),
                orderDto.status(),
                orderDto.createdAt(),
                mapToAddressResponse(orderDto.address()),
                mapToMoneyResponse(orderDto.totalAmount()),
                orderDto.orderLines()
                        .stream()
                        .map(this::mapToOrderLinesResponse)
                        .toList()
        );
    }

    private AddressResponse mapToAddressResponse(AddressDto addressDto) {
        return new AddressResponse(
                addressDto.street(),
                addressDto.city(),
                addressDto.state(),
                addressDto.zipCode(),
                addressDto.country()
        );
    }

    private AddressDto mapToAddressDto(AddressRequest addressRequest) {
        if (addressRequest == null) {
            return null;
        }
        return new AddressDto(
                addressRequest.getStreet(),
                addressRequest.getCity(),
                addressRequest.getState(),
                addressRequest.getZipCode(),
                addressRequest.getCountry()
        );
    }

    private MoneyResponse mapToMoneyResponse(MoneyDto moneyDto) {
        return new MoneyResponse(moneyDto.amount());
    }

    private OrderLineResponse mapToOrderLinesResponse(OrderLineDto orderLineDto) {
        return new OrderLineResponse(
                orderLineDto.id(),
                orderLineDto.productName(),
                orderLineDto.quantity(),
                mapToMoneyResponse(orderLineDto.price())
        );
    }

    private List<OrderLineDto> mapToOrderLinesDto(List<OrderLineRequest> orderLinesRequests) {
        if (orderLinesRequests == null) {
            return Collections.emptyList();
        }
        return orderLinesRequests.stream()
                .map(this::mapToOrderLineDto)
                .toList();
    }

    private OrderLineDto mapToOrderLineDto(OrderLineRequest orderLineRequest) {
        return new OrderLineDto(
                null,
                orderLineRequest.getProductName(),
                orderLineRequest.getQuantity(),
                mapToMoneyDto(orderLineRequest.getPrice())
        );
    }

    private MoneyDto mapToMoneyDto(MoneyRequest moneyRequest) {
        if (moneyRequest == null) {
            return null;
        }
        return new MoneyDto(moneyRequest.getAmount());
    }
}
