package com.iprody.ms.payment.filter;

import io.github.bucket4j.Bandwidth;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bucket;

@Component
@Order(1)
public class RateLimitFilter implements Filter {
    // Хранит мапу IP-адресов клиентов и их соответствующих бакетов для ограничения количества запросов
    private final ConcurrentHashMap<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Получаем IP-адрес клиента для идентификации
        String clientIP = request.getRemoteAddr();

        // Получаем или создаем бакет для этого IP
        Bucket bucket = BUCKETS.computeIfAbsent(clientIP, this::newBucket);

        // Проверяем, есть ли еще токены в бакете для обработки запроса
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too Many Requests");
        }
    }

    private Bucket newBucket(String s) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(20) // Максимальное число токенов (разрешенных запросов)
                .refillIntervally(10, Duration.ofMinutes(1)) // Пополнение бакета 10-ю токенами каждую 1 минуту
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
