package com.iprody.ms.delivery.domain.model.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryAddress;
import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryStatus;
import com.iprody.ms.delivery.domain.model.valueobjects.TimeWindow;

@Entity
@Table(schema = "delivery_service", name = "deliveries")
@Getter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DeliveryStatus status;

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Embedded
    private TimeWindow timeWindow;

    @Column(nullable = false)
    private String trackingNumber;

    public Delivery(Long orderId, DeliveryStatus status, DeliveryAddress deliveryAddress, LocalDate deliveryDate,
                    TimeWindow timeWindow, String trackingNumber) {
        setOrderId(orderId);
        setStatus(status);
        setDeliveryAddress(deliveryAddress);
        setDeliveryDate(deliveryDate);
        setTimeWindow(timeWindow);
        setTrackingNumber(trackingNumber);
    }

    public void update(Long orderId, DeliveryStatus status, DeliveryAddress deliveryAddress, LocalDate deliveryDate,
                       TimeWindow timeWindow, String trackingNumber) {
        setOrderId(orderId);
        setStatus(status);
        setDeliveryAddress(deliveryAddress);
        setDeliveryDate(deliveryDate);
        setTimeWindow(timeWindow);
        setTrackingNumber(trackingNumber);
    }

    public void setOrderId(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Идентификатор заказа должен быть большим 0");
        }
        this.orderId = orderId;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status == null ? DeliveryStatus.CREATED : status;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        if (deliveryAddress == null) {
            throw new IllegalArgumentException("Должен быть указан адрес доставки");
        }
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        if (deliveryDate == null) {
            throw new IllegalArgumentException("Должна быть указана дата доставки");
        }
        this.deliveryDate = deliveryDate;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        if (timeWindow == null) {
            throw new IllegalArgumentException("Должен быть указан промежуток времени, в течение которого должна быть выполнена доставка");
        }
        this.timeWindow = timeWindow;
    }

    public void setTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Должен быть указан трек-номер доставки");
        }
        this.trackingNumber = trackingNumber.trim();
    }
}
