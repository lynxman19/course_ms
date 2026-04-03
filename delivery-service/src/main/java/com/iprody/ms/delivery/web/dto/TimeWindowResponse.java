package com.iprody.ms.delivery.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeWindowResponse {
    private LocalTime startTime;
    private LocalTime endTime;
}
