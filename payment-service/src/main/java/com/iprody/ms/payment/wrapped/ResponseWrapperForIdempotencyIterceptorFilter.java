package com.iprody.ms.payment.wrapped;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.ContentCachingResponseWrapper;
import static com.iprody.ms.payment.constant.WebConstants.WRAPPED_RESPONSE_ATTRIBUTE_NAME;

import java.io.IOException;

public class ResponseWrapperForIdempotencyIterceptorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            wrapResponseForNonIdempotentMethods(chain, httpRequest, httpResponse);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static void wrapResponseForNonIdempotentMethods(FilterChain chain,
                                                            HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse) throws IOException, ServletException {
        var method = HttpMethod.valueOf(httpRequest.getMethod());

        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PATCH)) {

            /*
             * Создаем обертку для кеширования тела ответа и сохраняем в атрибуте запроса для дальнейшего использования
             * в IdempotencyInterceptor
             */
            var wrappedResponse = new ContentCachingResponseWrapper(httpResponse);
            httpRequest.setAttribute(WRAPPED_RESPONSE_ATTRIBUTE_NAME, wrappedResponse);

            try {
                chain.doFilter(httpRequest, wrappedResponse);
            } finally {
                // После обработки копируем тело ответа обратно в оригинальный ответ
                wrappedResponse.copyBodyToResponse();
            }
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }
}
