package com.iprody.ms.order.integration.payment.client;

import com.iprody.ms.order.integration.payment.dto.common.PaymentAmount;
import com.iprody.ms.order.integration.payment.dto.common.PaymentMethod;
import com.iprody.ms.order.integration.payment.dto.common.PaymentStatus;
import com.iprody.ms.order.integration.payment.dto.request.PaymentRequest;
import com.iprody.ms.order.integration.payment.dto.response.PaymentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableWireMock( // Включает и настраивает WireMock сервер для мокирования внешних HTTP-запросов
        @ConfigureWireMock(
                name = "payment-service", // Название мок-сервиса
                port = 9999, // Порт, на котором работает WireMock
 ////               baseUrlProperties = "http://localhost", // Базовый URL для клиента
                filesUnderClasspath = "wiremock" // Папка с файлами сценариев мок-ответов
        )
)
public class PaymentClientIntegrationTest {
        @Autowired
        private PaymentClient paymentClient;

        @Test
        void createPayment_success() {
                PaymentRequest paymentRequest = new PaymentRequest(
                        1L,
                        PaymentStatus.PENDING,
                        PaymentMethod.CARD,
                        new PaymentAmount(new BigDecimal("100.00"))

                );

                PaymentResponse result = paymentClient.createPayment(paymentRequest);

                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals(1L, result.getOrderId());
                assertEquals(PaymentStatus.PENDING, result.getStatus());
                assertEquals(PaymentMethod.CARD, result.getMethod());
                verify(postRequestedFor(urlEqualTo("/api/payments/create"))
                        .withHeader("X-Idempotency-Key", equalTo("1")));
        }

        @Test
        void createPayment_Conflict() {
                // 409 Conflict означает: ключ идемпотентности уже существует,
                // payment-service возвращает тело с кешированным ответом.
                // PaymentClientAdapter.isAcceptable() = true для 409 → парсит и возвращает тело.
                PaymentRequest request = new PaymentRequest(
                        409L,
                        PaymentStatus.PENDING,
                        PaymentMethod.CARD,
                        new PaymentAmount(new BigDecimal("100.00"))
                );

                PaymentResponse result = paymentClient.createPayment(request);

                assertNotNull(result);
                assertEquals(409L, result.getOrderId());
                verify(postRequestedFor(urlEqualTo("/api/payments/create"))
                        .withHeader("X-Idempotency-Key", equalTo("409")));
        }

        @Test
        void createPayment_badRequest_throwsRuntimeException() {
                PaymentRequest request = new PaymentRequest(
                        400L,
                        PaymentStatus.PENDING,
                        PaymentMethod.CARD,
                        new PaymentAmount(new BigDecimal("100.00"))
                );

                assertThrows(RuntimeException.class, () -> paymentClient.createPayment(request));
                verify(postRequestedFor(urlEqualTo("/api/payments/create"))
                        .withHeader("X-Idempotency-Key", equalTo("400")));
        }

        @Test
        void createPayment_serverError_throwsRuntimeException() {
                PaymentRequest request = new PaymentRequest(
                        500L,
                        PaymentStatus.PENDING,
                        PaymentMethod.CARD,
                        new PaymentAmount(new BigDecimal("100.00"))
                );

                assertThrows(RuntimeException.class, () -> paymentClient.createPayment(request));
                verify(postRequestedFor(urlEqualTo("/api/payments/create"))
                        .withHeader("X-Idempotency-Key", equalTo("500")));
        }
}
