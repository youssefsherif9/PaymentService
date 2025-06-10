package com.example.paymentservice.service;

import com.example.paymentservice.dto.*;
import com.example.paymentservice.model.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.example.paymentservice.enums.TransactionStatus.NEW;
@Slf4j
@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Value("${payment.expiry.duration}")
    private long expiryDuration;

    @Value("${payment.expiry.unit}")
    private String expiryUnit;

    @Autowired
    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository){
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public PaymentAmountResponseDto generatePayment(PaymentAmountRequestDto request) {
        String transactionId = "txn_" + LocalDate.now() + "_" + UUID.randomUUID().toString().substring(0, 6);
        Instant expiresAt = Instant.now().plus(expiryDuration, ChronoUnit.valueOf(expiryUnit.toUpperCase()));

        PaymentTransaction txn = new PaymentTransaction(transactionId,request.getAmount(),expiresAt,NEW);
        paymentTransactionRepository.save(txn);
        log.debug("created a new payment transaction record with transactionID: {} , amount: {} , expires at: {}",txn.getTransactionId(),txn.getAmount(),txn.getExpiresAt());

        return new PaymentAmountResponseDto(
                transactionId,
                request.getAmount(),
                expiresAt.toString()
        );
    }
}

