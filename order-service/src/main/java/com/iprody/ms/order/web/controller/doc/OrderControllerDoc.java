package com.iprody.ms.order.web.controller.doc;

import com.iprody.ms.order.web.dto.OrderRequest;
import com.iprody.ms.order.web.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Order", description = "API для работы с заказами")

public interface OrderControllerDoc {
    @Operation(
            summary = "Создать заказ",
            description = "Позволить пользователю создать заказ. Возвращает информацию о созданном заказе или ошибку, если запись невозможна."
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Заказ успешно создана",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Созданть заказ не получается",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные входные данные",
                    content = @Content
            )
    })

    @GetMapping
    ResponseEntity<OrderResponse> create(
            @RequestBody(description = "Данные для создания заказа",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class)
                    )
            ) OrderRequest orderRequest);
}
