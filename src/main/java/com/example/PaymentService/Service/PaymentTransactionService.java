package com.example.paymentservice.service;

import com.example.paymentservice.dto.*;
import com.example.paymentservice.model.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.example.paymentservice.enums.TransactionStatus.NEW;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Value("${payment.expiry.duration}")
    private long expiryDuration;

    @Value("${payment.expiry.unit}")
    private String expiryUnit;

    public synchronized PaymentAmountResponseDto generatePayment(PaymentAmountRequestDto request) {
        String transactionId = "txn_" + Instant.now().toString() + "_" + UUID.randomUUID().toString().substring(0, 6);
        Instant expiresAt = Instant.now().plus(expiryDuration, ChronoUnit.valueOf(expiryUnit.toUpperCase()));

        PaymentTransaction transaction = new PaymentTransaction(transactionId,request.getAmount(),expiresAt,NEW);
        paymentTransactionRepository.save(transaction);
        log.debug("created a new payment transaction record with transactionID: {} , amount: {} , expires at: {}",transaction.getTransactionId(),transaction.getAmount(),transaction.getExpiresAt());

        return new PaymentAmountResponseDto(
                transactionId,
                request.getAmount(),
                expiresAt.toString()
        );
    }
}

