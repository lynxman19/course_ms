package com.iprody.ms.order.integration.payment.client;

import com.iprody.ms.order.integration.payment.client.feign.PaymentFeignClient;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentClient {
    private final PaymentFeignClient paymentFeignClient;
    private final JsonMapper mapper;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        try {
            return paymentFeignClient.createPayment(UUID.fromString("97aace5a-965d-430b-a148-87ab2cc9ed8e"),
                                                    paymentRequest);
        } catch (FeignException ex) {
            return processException(ex);
        }
    }

    private PaymentResponse processException(FeignException ex) {
        HttpStatusCode statusCode = HttpStatusCode.valueOf(ex.status());
        Optional<ByteBuffer> bodyOptional = ex.responseBody();

        if (isAcceptable(statusCode) && bodyOptional.isPresent()) {
            return getResponse(bodyOptional.get());
        } else {
            throw new RuntimeException("Не была выполнена оплата заказа");
        }
    }

    private boolean isAcceptable(HttpStatusCode statusCode) {
        return statusCode.is2xxSuccessful() || statusCode.isSameCodeAs(HttpStatus.CONFLICT);
    }

    private PaymentResponse getResponse(ByteBuffer body) {
        return mapper.readValue(body.array(), PaymentResponse.class);
    }
}
