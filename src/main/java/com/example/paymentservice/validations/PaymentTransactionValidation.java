package com.example.paymentservice.validations;


import com.example.paymentservice.Exception.PaymentValidationException;
import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.model.PaymentTransaction;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class PaymentTransactionValidation {

    private PaymentTransactionValidation() {
        // Private constructor to prevent instantiation
    }

    public static void checkExpiration(PaymentTransaction paymentTransaction, String transactionId) {
        log.debug("expire at:" + paymentTransaction.getExpiresAt() + "  now:" + Instant.now());

        if (paymentTransaction.getExpiresAt().isBefore(Instant.now())) {
            log.error("Transaction Id: [{}] has expired ", transactionId);
            throw new PaymentValidationException("Transaction is expired");
        }
    }

    public static void verifyAmount(PaymentTransaction paymentTransaction, double amount, String transactionId) {
        if (amount != paymentTransaction.getAmount()) {
            log.error("Transaction Id: [{}] saved amount doesn't match request amount ", transactionId);
            throw new PaymentValidationException("Transaction saved amount doesn't match request amount");
        }
    }

    public static void checkStatus(PaymentTransaction paymentTransaction, String transactionId) {
        if (!paymentTransaction.getStatus().equals(TransactionStatus.NEW)) {
            log.error("Transaction Id: [{}] has already been processed ", transactionId);
            throw new PaymentValidationException("Transaction id" + transactionId + "has already been processed");
        }
    }


}
