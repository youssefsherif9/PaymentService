package com.example.paymentservice.Service;


import com.example.paymentservice.Dto.PaymentAmountRequestDto;
import com.example.paymentservice.Dto.PaymentAmountResponseDto;
import com.example.paymentservice.Dto.PaymentProcessRequestDto;
import com.example.paymentservice.Dto.PaymentProcessResponseDto;
import com.example.paymentservice.Exception.EntityNotFoundException;
import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.model.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import com.example.paymentservice.validations.PaymentTransactionValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.example.paymentservice.enums.TransactionStatus.NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private  final PaymentTransactionValidation paymentTransactionValidation;
    @Value("${payment.expiry.duration}")
    private long expiryDuration;

    @Value("${payment.expiry.unit}")
    private String expiryUnit;

    public synchronized PaymentAmountResponseDto generatePayment(PaymentAmountRequestDto request) {

        Instant now = Instant.now();

        // Format date as yyyyMMdd
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd")
                .withZone(ZoneId.systemDefault())
                .format(now);

        // Get nanoseconds
        String nanoPart = String.valueOf(now.getNano());

        String transactionId = "txn_" + nanoPart + datePart + "_" + UUID.randomUUID().toString().substring(0, 6);

        Instant expiresAt = Instant.now().plus(expiryDuration, ChronoUnit.valueOf(expiryUnit.toUpperCase()));

        PaymentTransaction transaction = new PaymentTransaction(transactionId, request.getAmount(), expiresAt, NEW);
        paymentTransactionRepository.save(transaction);
        log.debug("created a new payment transaction record with transactionID: {} , amount: {} , expires at: {}", transaction.getTransactionId(), transaction.getAmount(), transaction.getExpiresAt());

        return new PaymentAmountResponseDto(
                transactionId,
                request.getAmount(),
                expiresAt.toString()
        );
    }

    public PaymentProcessResponseDto processPayment(PaymentProcessRequestDto paymentRequest) {
        PaymentTransaction paymentTransaction = validateTransaction(paymentRequest.getTransactionId(), paymentRequest.getAmount());
        return processTransaction(paymentTransaction, paymentRequest.getCardNumber().trim());
    }

    private PaymentProcessResponseDto processTransaction(PaymentTransaction paymentTransaction, String cardNumber) {
        log.info("started processing transaction after passing validations");
        char lastDigitChar = cardNumber.charAt(cardNumber.length() - 1);
        int lastDigit = Character.getNumericValue(lastDigitChar);
        PaymentProcessResponseDto response = new PaymentProcessResponseDto();
        response.setTransactionId(paymentTransaction.getTransactionId());

        if (lastDigit % 2 == 0) {
            paymentTransaction.setStatus(TransactionStatus.SUCCEEDED);
            response.setStatus("success");
            response.setMessage("Payment processed successfully");
        } else {
            paymentTransaction.setStatus(TransactionStatus.FAILED);
            response.setStatus(String.valueOf(TransactionStatus.FAILED));
            response.setMessage("Payment declined");
        }
        paymentTransactionRepository.save(paymentTransaction);
        log.info("Finished payment process for transaction id: [{}]", paymentTransaction.getTransactionId());
        return response;
    }

    public PaymentTransaction validateTransaction(String transactionId, double amount) {
        PaymentTransaction paymentTransaction = fetchTransaction(transactionId);
        paymentTransactionValidation.checkExpiration(paymentTransaction,transactionId);
        paymentTransactionValidation.verifyAmount(paymentTransaction, amount, transactionId);
        paymentTransactionValidation.checkStatus(paymentTransaction, transactionId);
        return paymentTransaction;
    }

    private PaymentTransaction fetchTransaction(String transactionId){
        return paymentTransactionRepository.findByTransactionId(transactionId).orElseThrow(() -> {
            log.error("Transaction Id [{}] not found ", transactionId);
            return new EntityNotFoundException("Transaction Id not found");
        });
    }

    public void updatePayment(String transactionId) {
        PaymentTransaction paymentTransaction = fetchTransaction(transactionId);
        paymentTransactionValidation.updateStatusToFailed(paymentTransaction);
        log.debug("updated payment transaction record with transactionID: {} to Failed", transactionId);
    }
}

