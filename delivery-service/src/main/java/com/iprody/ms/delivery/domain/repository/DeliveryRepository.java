package com.iprody.ms.delivery.domain.repository;

import com.iprody.ms.delivery.domain.model.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
