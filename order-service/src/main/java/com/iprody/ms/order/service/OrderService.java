package com.iprody.ms.order.service;

import com.iprody.ms.order.domain.model.aggregate.Order;
import com.iprody.ms.order.domain.model.entities.OrderLine;
import com.iprody.ms.order.domain.model.valueobjects.Money;
import com.iprody.ms.order.domain.repository.OrderRepository;
import com.iprody.ms.order.common.ResourceNotFoundException;
import com.iprody.ms.order.integration.payment.client.PaymentClient;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import com.iprody.ms.order.service.dto.AddressDto;
import com.iprody.ms.order.service.dto.MoneyDto;
import com.iprody.ms.order.service.dto.OrderLineDto;
import com.iprody.ms.order.service.execute.OrderExecute;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iprody.ms.order.service.dto.OrderDto;

import com.iprody.ms.order.domain.model.valueobjects.Address;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "orderServiceCircuitBreaker")
    public OrderDto getById(Long orderId) {
        return transformToOrderDto(getOrder(orderId));
    }

    @CircuitBreaker(name = "orderServiceCircuitBreaker")
    public List<OrderDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::transformToOrderDto)
                .toList();
    }

    @Transactional
    @CircuitBreaker(name = "orderServiceCircuitBreaker")
    public OrderDto create(OrderExecute orderExecute) {
        Order order = new Order(
                orderExecute.customerId(),
                orderExecute.status(),
                transformToAddressDto(orderExecute.address()),
                transformToOrderLines(orderExecute.lines())
        );
        return transformToOrderDto(orderRepository.save(order));
    }

    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        if (paymentRequest.method() == null) {
            throw new IllegalArgumentException("Необходимо указать способо оплаты заказа");
        }
        if (paymentRequest.amount() == null) {
            throw new IllegalArgumentException("Необходимо указать сумму оплаты заказа");
        }
        return paymentClient.createPayment(paymentRequest);
    }

    @Transactional
    @CircuitBreaker(name = "orderServiceCircuitBreaker")
    public OrderDto update(Long orderId, OrderExecute orderExecute) {
        Order order = getOrder(orderId);
        order.update(
                orderExecute.customerId(),
                orderExecute.status(),
                transformToAddressDto(orderExecute.address()),
                transformToOrderLines(orderExecute.lines())
        );
        return transformToOrderDto(order);
    }

    @Transactional
    @CircuitBreaker(name = "orderServiceCircuitBreaker")
    public void delete(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Заказ с идентификатором " + orderId + " не был найден");
        }
        orderRepository.deleteById(orderId);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с идентификатором " + orderId + " не был найден"));
    }

    private OrderDto transformToOrderDto(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getCreatedAt(),
                new AddressDto(
                        order.getAddress().getStreet(),
                        order.getAddress().getCity(),
                        order.getAddress().getState(),
                        order.getAddress().getZipCode(),
                        order.getAddress().getCountry()
                ),
                new MoneyDto(
                        order.getTotalPrice().getPrice()
                 ),
                order.getOrderLines().stream()
                                .map(orderLine -> new OrderLineDto(
                                        orderLine.getOrderLineId(),
                                        orderLine.getProductName(),
                                        orderLine.getQuantity(),
                                        new MoneyDto(orderLine.getPrice().getPrice())
                                ))
                                .toList()
        );
    }

    private Address transformToAddressDto(AddressDto addressDto) {
        if (addressDto == null) {
            throw new IllegalArgumentException("Не указан адрес");
        }
        return new Address(
                addressDto.street(),
                addressDto.city(),
                addressDto.state(),
                addressDto.zipCode(),
                addressDto.country()
        );
    }

    private List<OrderLine> transformToOrderLines(List<OrderLineDto> orderLinesDto) {
        if (orderLinesDto == null) {
            return Collections.emptyList();
        }
        return orderLinesDto.stream()
                .map(orderLineDto -> new OrderLine(
                        orderLineDto.productName(),
                        orderLineDto.quantity(),
                        transformToMoney(orderLineDto)
                ))
                .toList();
    }

    private Money transformToMoney(OrderLineDto orderLineDto) {
        if (orderLineDto == null || orderLineDto.price() == null) {
            throw new IllegalArgumentException("В каждой строке заказа должна быть указана цена");
        }
        return new Money(orderLineDto.price().amount());
    }
}
