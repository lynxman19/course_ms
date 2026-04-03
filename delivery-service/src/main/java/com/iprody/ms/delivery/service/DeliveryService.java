package com.iprody.ms.delivery.service;

import com.iprody.ms.delivery.common.ResourceNotFoundException;
import com.iprody.ms.delivery.domain.model.aggregate.Delivery;
import com.iprody.ms.delivery.domain.model.valueobjects.DeliveryAddress;
import com.iprody.ms.delivery.domain.model.valueobjects.TimeWindow;
import com.iprody.ms.delivery.domain.repository.DeliveryRepository;
import com.iprody.ms.delivery.service.dto.DeliveryAddressDto;
import com.iprody.ms.delivery.service.dto.DeliveryDto;
import com.iprody.ms.delivery.service.dto.TimeWindowDto;
import com.iprody.ms.delivery.service.execute.DeliveryExecute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryDto getById(Long deliveryId) {
        return transformToDeliveryDto(getDelivery(deliveryId));
    }

    public List<DeliveryDto> getAll() {
        return deliveryRepository.findAll()
                .stream()
                .map(this::transformToDeliveryDto)
                .toList();
    }

    @Transactional
    public DeliveryDto create(DeliveryExecute deliveryExecute) {
        Delivery delivery = new Delivery(
                deliveryExecute.orderId(),
                deliveryExecute.status(),
                transformToDeliveryAddress(deliveryExecute.deliveryAddress()),
                deliveryExecute.deliveryDate(),
                transformToTimeWindow(deliveryExecute.timeWindow()),
                deliveryExecute.trackingNumber()
        );
        return transformToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Transactional
    public DeliveryDto update(Long deliveryId, DeliveryExecute deliveryExecute) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.update(
                deliveryExecute.orderId(),
                deliveryExecute.status(),
                transformToDeliveryAddress(deliveryExecute.deliveryAddress()),
                deliveryExecute.deliveryDate(),
                transformToTimeWindow(deliveryExecute.timeWindow()),
                deliveryExecute.trackingNumber()
        );
        return transformToDeliveryDto(delivery);
    }

    @Transactional
    public void delete(Long deliveryId) {
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new ResourceNotFoundException("Доставка с идентификатором" + deliveryId + " не была найдена");
        }
        deliveryRepository.deleteById(deliveryId);
    }

    private Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Доставка с идентификатором " + deliveryId + " не была найдена"));
    }

    private DeliveryDto transformToDeliveryDto(Delivery delivery) {
        return new DeliveryDto(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                new DeliveryAddressDto(
                        delivery.getDeliveryAddress().getStreet(),
                        delivery.getDeliveryAddress().getCity(),
                        delivery.getDeliveryAddress().getZipCode(),
                        delivery.getDeliveryAddress().getState(),
                        delivery.getDeliveryAddress().getCountry()
                ),
                delivery.getDeliveryDate(),
                new TimeWindowDto(
                        delivery.getTimeWindow().getStartTime(),
                        delivery.getTimeWindow().getEndTime()
                ),
                delivery.getTrackingNumber()
        );
    }

    private DeliveryAddress transformToDeliveryAddress(DeliveryAddressDto deliveryAddressDto) {
        if (deliveryAddressDto == null) {
            throw new IllegalArgumentException("Необходимо указать адрес доставки");
        }
        return new DeliveryAddress(
                deliveryAddressDto.street(),
                deliveryAddressDto.city(),
                deliveryAddressDto.state(),
                deliveryAddressDto.zipCode(),
                deliveryAddressDto.country()
        );
    }

    private TimeWindow transformToTimeWindow(TimeWindowDto timeWindowDto) {
        if (timeWindowDto == null) {
            throw new IllegalArgumentException("Необходимо указать промежуток времени доставки");
        }
        return new TimeWindow(timeWindowDto.startTime(), timeWindowDto.endTime());
    }
}
