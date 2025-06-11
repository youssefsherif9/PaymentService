package com.example.payment.validations;

import com.example.payment.enums.TransactionStatus;
import com.example.payment.model.PaymentTransaction;
import com.example.payment.exception.PaymentValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class PaymentTransactionValidation {

    private PaymentTransactionValidation() {
        // Private constructor to prevent instantiation
    }

    public static void checkExpiration(PaymentTransaction paymentTransaction, String transactionId) {
        if (paymentTransaction.getExpiresAt().isBefore(Instant.now() )){
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
        if (!paymentTransaction.getTransactionStatus().equals(TransactionStatus.NEW)) {
            log.error("Transaction Id: [{}] has already been processed ", transactionId);
            throw new PaymentValidationException("Transaction id"+transactionId+"has already been processed");
        }
    }

//    public static void validatePaymentRequestIsPresent(PaymentProcessRequestDto requestDto) {
//        if (Objects.isNull(requestDto.getTransactionId()) || requestDto.getTransactionId().isBlank()) {
//            log.error("Transaction ID is missing");
//            throw new BusinessException("Transaction ID is required");
//        }
//
//        if (requestDto.getAmount() <= 0) {
//            log.error("Invalid transaction amount");
//            throw new BusinessException("Transaction must have amount and must be greater than zero");
//        }
//
//        if (Objects.isNull(requestDto.getCardNumber()) || requestDto.getCardNumber().isBlank()) {
//            log.error("Card number is missing");
//            throw new EntityNotFoundException("Card number is required");
//        }
//
//        if (Objects.isNull(requestDto.getExpiresAt()) || requestDto.getExpiresAt().isBlank()) {
//            log.error("Expiry date is missing");
//            throw new BusinessException("Expiry date is required");
//        }
//
//        if (Objects.isNull(requestDto.getCvv()) || requestDto.getCvv().isBlank()) {
//            log.error("CVV is missing");
//            throw new BusinessException("CVV is required");
//        }
//
//        log.info("Payment request validation passed for Transaction ID: [{}]", requestDto.getTransactionId());
//    }
}
