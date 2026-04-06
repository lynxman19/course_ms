package com.iprody.ms.delivery.web.mapper;

import com.iprody.ms.delivery.service.dto.DeliveryAddressDto;
import com.iprody.ms.delivery.service.dto.DeliveryDto;
import com.iprody.ms.delivery.service.dto.TimeWindowDto;
import com.iprody.ms.delivery.service.execute.DeliveryExecute;
import com.iprody.ms.delivery.web.dto.*;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {
    public DeliveryResponse mapToDeliveryResponse(DeliveryDto deliveryDto) {
        return new DeliveryResponse(
                deliveryDto.id(),
                deliveryDto.orderId(),
                deliveryDto.status(),
                mapToDeliveryAddressResponse(deliveryDto.deliveryAddress()),
                deliveryDto.deliveryDate(),
                mapToTimeWindowResponse(deliveryDto.timeWindow()),
                deliveryDto.trackingNumber()
        );
    }

    public DeliveryExecute mapToDeliveryExecute(DeliveryRequest deliveryRequest) {
        return new DeliveryExecute(
                deliveryRequest.getOrderId(),
                deliveryRequest.getStatus(),
                mapToDeliveryAddressDto(deliveryRequest.getDeliveryAddress()),
                deliveryRequest.getDeliveryDate(),
                mapToTimeWindowDto(deliveryRequest.getTimeWindow()),
                deliveryRequest.getTrackingNumber()
        );
    }

    private DeliveryAddressResponse mapToDeliveryAddressResponse(DeliveryAddressDto deliveryAddressDto) {
        return new DeliveryAddressResponse(
                deliveryAddressDto.street(),
                deliveryAddressDto.city(),
                deliveryAddressDto.state(),
                deliveryAddressDto.zipCode(),
                deliveryAddressDto.country()
        );
    }

    private TimeWindowResponse mapToTimeWindowResponse(TimeWindowDto timeWindowDto) {
        return new TimeWindowResponse(timeWindowDto.startTime(), timeWindowDto.endTime());
    }

    private DeliveryAddressDto mapToDeliveryAddressDto(DeliveryAddressRequest deliveryAddressRequest) {
        if (deliveryAddressRequest == null) {
            return null;
        }
        return new DeliveryAddressDto(
                deliveryAddressRequest.getStreet(),
                deliveryAddressRequest.getCity(),
                deliveryAddressRequest.getState(),
                deliveryAddressRequest.getZipCode(),
                deliveryAddressRequest.getCountry()
        );
    }

    private TimeWindowDto mapToTimeWindowDto(TimeWindowRequest timeWindowRequest) {
        if (timeWindowRequest == null) {
            return null;
        }
        return new TimeWindowDto(timeWindowRequest.getStartTime(), timeWindowRequest.getEndTime());
    }
}
