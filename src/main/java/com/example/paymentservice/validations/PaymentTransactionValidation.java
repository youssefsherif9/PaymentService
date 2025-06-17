package com.example.paymentservice.validations;


import com.example.paymentservice.Exception.PaymentValidationException;
import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.model.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentTransactionValidation {

    private final PaymentTransactionRepository paymentTransactionRepository;


    public void checkExpiration(PaymentTransaction paymentTransaction, String transactionId) {
        log.debug("expire at:" + paymentTransaction.getExpiresAt() + "  now:" + Instant.now());

        if (paymentTransaction.getExpiresAt().isBefore(Instant.now())) {
            updateStatusToFailed(paymentTransaction);
            log.error("Transaction Id: [{}] has expired ", transactionId);
            throw new PaymentValidationException("Transaction is expired");
        }
    }

    public  void verifyAmount(PaymentTransaction paymentTransaction, double amount, String transactionId) {
        if (amount != paymentTransaction.getAmount()) {
            updateStatusToFailed(paymentTransaction);
            log.error("Transaction Id: [{}] saved amount doesn't match request amount ", transactionId);
            throw new PaymentValidationException("Transaction saved amount doesn't match request amount");
        }
    }

    public void checkStatus(PaymentTransaction paymentTransaction, String transactionId) {
        if (!paymentTransaction.getStatus().equals(TransactionStatus.NEW)) {
            updateStatusToFailed(paymentTransaction);
            log.error("Transaction Id: [{}] has already been processed ", transactionId);
            throw new PaymentValidationException("Transaction id" + transactionId + "has already been processed");
        }
    }

    public void updateStatusToFailed(PaymentTransaction paymentTransaction){
        paymentTransaction.setStatus(TransactionStatus.FAILED);
        paymentTransactionRepository.save(paymentTransaction);
    }


}
