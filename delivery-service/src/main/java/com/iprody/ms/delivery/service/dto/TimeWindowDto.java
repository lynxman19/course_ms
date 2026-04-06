package com.iprody.ms.delivery.service.dto;

import java.time.LocalTime;

public record TimeWindowDto(
        LocalTime startTime,
        LocalTime endTime
) {
}
