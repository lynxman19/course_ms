package com.iprody.ms.delivery.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeWindowRequest {
    private LocalTime startTime;
    private LocalTime endTime;
}
