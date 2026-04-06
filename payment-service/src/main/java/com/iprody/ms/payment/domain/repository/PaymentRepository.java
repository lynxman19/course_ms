package com.iprody.ms.payment.domain.repository;

import com.iprody.ms.payment.domain.model.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
