package com.iprody.ms.delivery.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor
public class TimeWindow {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public TimeWindow(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Должны быть указаны границы промежутка времени доставки");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Время начала доставки должно быть меньше времени окончания доставки");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
