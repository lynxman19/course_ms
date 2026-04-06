package com.iprody.ms.order.web.controller;

import com.iprody.ms.order.service.OrderService;
import com.iprody.ms.order.web.dto.OrderRequest;
import com.iprody.ms.order.web.dto.OrderResponse;
import com.iprody.ms.order.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable("id") Long orderId) {
        OrderResponse response = mapper.mapToOrderResponse(orderService.getById(orderId));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderResponse> response = orderService.getAll()
                .stream()
                .map(mapper::mapToOrderResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        OrderResponse response = mapper.mapToOrderResponse(
                orderService.create(mapper.mapToExecuteOrder(orderRequest))
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId, @RequestBody OrderRequest orderRequest) {
        OrderResponse response = mapper.mapToOrderResponse(
                orderService.update(orderId, mapper.mapToExecuteOrder(orderRequest))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long orderId) {
        orderService.delete(orderId);
    }
}
