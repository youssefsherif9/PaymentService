package com.example.payment.service;

import com.example.payment.dto.*;
import com.example.payment.enums.TransactionStatus;
import com.example.payment.exception.EntityNotFoundException;
import com.example.payment.model.PaymentTransaction;
import com.example.payment.repository.PaymentTransactionRepository;
import com.example.payment.validations.PaymentTransactionValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentTransactionService {


    private final PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public PaymentProcessResponseDto processPayment(PaymentProcessRequestDto paymentRequest) {
        PaymentTransaction paymentTransaction = validateTransaction(paymentRequest.getTransactionId(), paymentRequest.getAmount());
        return processTransaction(paymentTransaction, paymentRequest.getCardNumber());
    }

    private PaymentProcessResponseDto processTransaction(PaymentTransaction paymentTransaction, String cardNumber) {
       log.info("started processing transaction after passing validations");
        char lastDigitChar = cardNumber.charAt(cardNumber.length() - 1);
        int lastDigit = Character.getNumericValue(lastDigitChar);
        PaymentProcessResponseDto response = new PaymentProcessResponseDto();
        response.setTransactionId(paymentTransaction.getTransactionId());

        if (lastDigit % 2 == 0) {
            paymentTransaction.setTransactionStatus(TransactionStatus.SUCCEEDED);
            response.setStatus("success");
            response.setMessage("Payment processed successfully");
        } else {
            paymentTransaction.setTransactionStatus(TransactionStatus.FAILED);
            response.setStatus(String.valueOf(TransactionStatus.FAILED));
            response.setMessage("Payment declined");
        }
        paymentTransactionRepository.save(paymentTransaction);
        log.info("Finished payment process for transaction id: [{}]",paymentTransaction.getTransactionId());
        return response;
    }

    public PaymentTransaction validateTransaction(String transactionId, double amount) {
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByTransactionId(transactionId).orElseThrow(() -> {
            log.error("Transaction Id [{}] not found ", transactionId);
            return new EntityNotFoundException("Transaction Id not found");
        });
        PaymentTransactionValidation.checkExpiration(paymentTransaction, transactionId);
        PaymentTransactionValidation.verifyAmount(paymentTransaction, amount, transactionId);

        return paymentTransaction;
    }


}

