package com.iprody.ms.payment.web.controller;

import com.iprody.ms.payment.service.PaymentService;
import com.iprody.ms.payment.web.dto.PaymentRequest;
import com.iprody.ms.payment.web.dto.PaymentResponse;
import com.iprody.ms.payment.web.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable("id") Long paymentId) {
        PaymentResponse response = mapper.mapToPaymentResponse(paymentService.getById(paymentId));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        List<PaymentResponse> response =  paymentService.getAll()
                .stream()
                .map(mapper::mapToPaymentResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = mapper.mapToPaymentResponse(
                paymentService.create(mapper.mapToExecutePayment(paymentRequest))
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(@PathVariable("id") Long paymentId, @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = mapper.mapToPaymentResponse(
                paymentService.update(paymentId, mapper.mapToExecutePayment(paymentRequest))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long paymentId) {
        paymentService.delete(paymentId);
    }
}
