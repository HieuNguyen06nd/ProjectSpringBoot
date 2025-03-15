package com.hieunguyen.repository;

import com.hieunguyen.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByPaymentLinkId(String paymentId);
}
