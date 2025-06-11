package com.example.payment.validations;

import com.example.payment.model.PaymentTransaction;
import com.example.payment.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class PaymentTransactionValidation {

    private PaymentTransactionValidation() {
        // Private constructor to prevent instantiation
    }

    public static void checkExpiration(PaymentTransaction paymentTransaction, String transactionId) {
        if (paymentTransaction.getExpiresAt().isBefore(Instant.now())) {
            log.error("Transaction Id: [{}] is expired ", transactionId);
            throw new BusinessException("Transaction id is expired");
        }
    }

    public static void verifyAmount(PaymentTransaction paymentTransaction, double amount, String transactionId) {
        if (amount != paymentTransaction.getAmount()) {
            log.error("Transaction Id: [{}] saved amount doesn't match request amount ", transactionId);
            throw new BusinessException("Transaction saved amount doesn't match request amount");
        }
    }
}
