package com.iprody.ms.payment.service;

import com.iprody.ms.payment.common.ResourceNotFoundException;
import com.iprody.ms.payment.domain.model.aggregate.Payment;
import com.iprody.ms.payment.domain.model.valueobjects.PaymentAmount;
import com.iprody.ms.payment.domain.repository.PaymentRepository;
import com.iprody.ms.payment.service.dto.PaymentAmountDto;
import com.iprody.ms.payment.service.dto.PaymentDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.iprody.ms.payment.service.execute.PaymentExecute;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    public PaymentDto getById(Long paymentId) {
        return transformToPaymentDto(getPayment(paymentId));
    }

    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::transformToPaymentDto)
                .toList();
    }

    @Transactional
    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    public PaymentDto create(PaymentExecute paymentExecute) {
        Payment payment = new Payment(
                paymentExecute.orderId(),
                paymentExecute.status(),
                paymentExecute.method(),
                transformToPaymentAmount(paymentExecute.amount())
        );
        return transformToPaymentDto(paymentRepository.save(payment));
    }

    @Transactional
    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    public PaymentDto update(Long paymentId, PaymentExecute paymentExecute) {
        Payment payment = getPayment(paymentId);
        payment.update(
                paymentExecute.orderId(),
                paymentExecute.status(),
                paymentExecute.method(),
                transformToPaymentAmount(paymentExecute.amount())
        );
        return transformToPaymentDto(payment);
    }

    @Transactional
    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    public void delete(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new ResourceNotFoundException("Платеж с идентификатором " + paymentId + " не был найден");
        }
        paymentRepository.deleteById(paymentId);
    }

    private Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Платеж с идентификатором " + paymentId + " не был найден"));
    }

    private PaymentDto transformToPaymentDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getStatus(),
                payment.getMethod(),
                new PaymentAmountDto(payment.getAmount().getAmount()),
                payment.getCreatedAt()
        );
    }

    private PaymentAmount transformToPaymentAmount(PaymentAmountDto paymentAmountDto) {
        if (paymentAmountDto == null) {
            throw new IllegalArgumentException("Payment amount must be provided");
        }
        return new PaymentAmount(paymentAmountDto.amount());
    }

}
