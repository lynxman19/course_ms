package com.iprody.ms.order.domain.repository;

import com.iprody.ms.order.domain.model.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
