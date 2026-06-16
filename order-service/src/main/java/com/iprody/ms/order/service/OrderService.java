package com.iprody.ms.order.service;

import com.iprody.ms.order.common.DuplicateIdempotencyKeyException;
import com.iprody.ms.order.domain.model.aggregate.Order;
import com.iprody.ms.order.domain.model.entities.OrderLine;
import com.iprody.ms.order.domain.model.entities.outbox.AsyncMessage;
import com.iprody.ms.order.domain.model.valueobjects.*;
import com.iprody.ms.order.domain.repository.OrderRepository;
import com.iprody.ms.order.common.ResourceNotFoundException;
import com.iprody.ms.order.integration.delivery.messaging.config.KafkaDeliveryProperties;
import com.iprody.ms.order.integration.delivery.messaging.dto.OrderPaidMessage;
import com.iprody.ms.order.integration.payment.client.PaymentClient;
import com.iprody.ms.order.integration.payment.client.PaymentClientPendingIdempotencyKey;
import com.iprody.ms.order.integration.payment.messaging.PaymentRequestSender;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import com.iprody.ms.order.service.dto.AddressDto;
import com.iprody.ms.order.service.dto.MoneyDto;
import com.iprody.ms.order.service.dto.OrderLineDto;
import com.iprody.ms.order.service.execute.OrderExecute;
import com.iprody.ms.order.service.outbox.AsyncMessageService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iprody.ms.order.service.dto.OrderDto;
import tools.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentClientPendingIdempotencyKey paymentClientIdempotency;
    private final PaymentRequestSender paymentRequestSender;
//    private final OrderPaidPublisher orderPaidPublisher;
    private final PaymentClient paymentClient;
    private final AsyncMessageService asyncMessageService;
    private final KafkaDeliveryProperties kafkaDeliveryProperties;
    private final JsonMapper mapper;

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

        try {
            return paymentClientIdempotency.createPayment(paymentRequest);
        } catch (RequestNotPermitted | BulkheadFullException | CallNotPermittedException |
                 DuplicateIdempotencyKeyException ex) {
            throw ex;
        }
    }

    @Transactional
    public PaymentResponse createPaymentKafka(PaymentRequest paymentRequest) {
        if (paymentRequest.method() == null) {
            throw new IllegalArgumentException("Необходимо указать способо оплаты заказа");
        }
        if (paymentRequest.amount() == null) {
            throw new IllegalArgumentException("Необходимо указать сумму оплаты заказа");
        }

        Order order = getOrder(paymentRequest.orderId());

        try {
            PaymentResponse result = paymentClient.createPayment(paymentRequest);

            order.setStatus(OrderStatus.PAID);
//            orderPaidPublisher.publish(order);
            saveToOutbox(order);
            return result;
        } catch (RequestNotPermitted | BulkheadFullException | CallNotPermittedException |
                 DuplicateIdempotencyKeyException ex) {
            throw ex;
        }
    }

    @Transactional
    public OrderDto createPaymentAsync(Long orderId, PaymentRequest paymentRequest) {
        if (paymentRequest.method() == null) {
            throw new IllegalArgumentException("Необходимо указать способо оплаты заказа");
        }
        if (paymentRequest.amount() == null) {
            throw new IllegalArgumentException("Необходимо указать сумму оплаты заказа");
        }
        Order order = getOrder(orderId);

        log.info("Creating payment for order id {}", orderId);
        paymentRequestSender.send(order, paymentRequest);

        return transformToOrderDto(order);
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

    private void saveToOutbox(Order order) {
//        try {
            var payload = new OrderPaidMessage(
                    order.getOrderId(),
                    order.getTotalPrice().getPrice()
            );
            AsyncMessage message = AsyncMessage.builder()
                    .id(UUID.randomUUID().toString())
                    .topic(kafkaDeliveryProperties.orderPaidTopic())
                    .value(mapper.writeValueAsString(payload))
                    .type(AsyncMessageType.OUTBOX)
                    .status(AsyncMessageStatus.CREATED)
                    .build();
            asyncMessageService.saveMessage(message);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize OrderPaidMessage for outbox", e);
//        }
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
