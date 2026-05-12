package com.iprody.ms.order.integration.payment.client;

import com.iprody.ms.order.common.DuplicateIdempotencyKeyException;
import com.iprody.ms.order.integration.payment.client.feign.PaymentFeignClient;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import feign.FeignException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentClientPendingIdempotencyKey {
    private final PaymentFeignClient paymentFeignClient;
    private final JsonMapper mapper;

    @Retry(name = "paymentServiceRetry")
    @CircuitBreaker(name = "paymentServiceCircuitBreaker")
    @RateLimiter(name = "paymentClientRateLimiter")
    @Bulkhead(name = "paymentClientBulkhead")
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        try {
            return paymentFeignClient.createPayment(paymentRequest.orderId().toString(), paymentRequest);
        } catch (FeignException ex) {
            processException(ex);
            throw ex;
        }
    }

    private void processException(FeignException ex) {
        HttpStatusCode statusCode = HttpStatusCode.valueOf(ex.status());
        Optional<ByteBuffer> bodyOptional = ex.responseBody();

        if (statusCode.equals(HttpStatus.CONFLICT) && bodyOptional.isPresent()) {
            String response = getResponse(bodyOptional.get());

            throw new DuplicateIdempotencyKeyException(response);
        }
    }

    private String getResponse(ByteBuffer body) {
        return StandardCharsets.UTF_8.decode(body).toString();
    }
}
