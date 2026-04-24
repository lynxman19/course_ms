package com.iprody.ms.order.integration.payment.client.feign;

import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient (name = "payment-service", url = "http://localhost:8082/api/payments")
public interface PaymentFeignClient {
    @PostMapping("/create")
    PaymentResponse createPayment(@RequestBody PaymentRequest paymentRequest);
}
