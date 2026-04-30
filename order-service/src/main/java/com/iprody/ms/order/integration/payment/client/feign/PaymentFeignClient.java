package com.iprody.ms.order.integration.payment.client.feign;

import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient (name = "payment-service", url = "http://localhost:8082/api/payments")
public interface PaymentFeignClient {
    @PostMapping("/create")
    PaymentResponse createPayment(@RequestHeader("X-Idempotency-Key") UUID idempotencyKey,
                                  @RequestBody PaymentRequest paymentRequest);
}
