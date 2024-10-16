package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
