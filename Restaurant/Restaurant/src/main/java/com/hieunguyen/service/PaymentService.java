package com.hieunguyen.service;

import com.hieunguyen.entity.Payment;
import com.hieunguyen.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
}
