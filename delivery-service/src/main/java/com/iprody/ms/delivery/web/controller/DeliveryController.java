package com.iprody.ms.delivery.web.controller;

import com.iprody.ms.delivery.service.DeliveryService;
import com.iprody.ms.delivery.web.dto.DeliveryRequest;
import com.iprody.ms.delivery.web.dto.DeliveryResponse;

import com.iprody.ms.delivery.web.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final DeliveryMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getById(@PathVariable("id") Long deliveryId) {
        DeliveryResponse response = mapper.mapToDeliveryResponse(deliveryService.getById(deliveryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getAll() {
        List<DeliveryResponse> response = deliveryService.getAll()
                .stream()
                .map(mapper::mapToDeliveryResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeliveryResponse> create(@RequestBody DeliveryRequest deliveryRequest) {
        DeliveryResponse response = mapper.mapToDeliveryResponse(
                deliveryService.create(mapper.mapToDeliveryExecute(deliveryRequest)));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(@PathVariable("id") Long deliveryId, @RequestBody DeliveryRequest deliveryRequest) {
        DeliveryResponse response = mapper.mapToDeliveryResponse(
                deliveryService.update(deliveryId, mapper.mapToDeliveryExecute(deliveryRequest)));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long deliveryId) {
        deliveryService.delete(deliveryId);
    }
}
