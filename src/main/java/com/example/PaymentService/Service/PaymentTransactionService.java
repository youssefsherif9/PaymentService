package com.example.paymentservice.Service;


import com.example.paymentservice.Dto.*;
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

    @Value("${card.mask}")
    private String mask;

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
        PaymentTransaction paymentTransaction = fetchTransaction(paymentRequest.getTransactionId());
        //mask card number
        paymentTransaction.setCardNumber(maskCardNumber(paymentRequest.getCardNumber()));
        paymentTransaction.setCardExpireDate(paymentRequest.getExpiryDate());
        PaymentTransaction transaction = validateTransaction(paymentTransaction);
        return processTransaction(transaction, paymentRequest.getCardNumber().trim());
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

    public PaymentTransaction validateTransaction(PaymentTransaction paymentTransaction) {
        paymentTransactionValidation.checkExpiration(paymentTransaction,paymentTransaction.getTransactionId());
        paymentTransactionValidation.verifyAmount(paymentTransaction, paymentTransaction.getAmount(), paymentTransaction.getTransactionId());
        paymentTransactionValidation.checkStatus(paymentTransaction, paymentTransaction.getTransactionId());
        return paymentTransaction;
    }

    private PaymentTransaction fetchTransaction(String transactionId){
        return paymentTransactionRepository.findByTransactionId(transactionId).orElseThrow(() -> {
            log.error("Transaction Id [{}] not found ", transactionId);
            return new EntityNotFoundException("Transaction Id not found");
        });
    }

    public void updatePayment(UpdatePaymentRequestDto updatePaymentRequestDto) {
        validateUpdateTransactionStatus(updatePaymentRequestDto.getStatus());
        PaymentTransaction paymentTransaction = fetchTransaction(updatePaymentRequestDto.getTransactionId());
        paymentTransactionValidation.checkStatus(paymentTransaction, updatePaymentRequestDto.getTransactionId());
        paymentTransaction.setStatus(updatePaymentRequestDto.getStatus());
        paymentTransactionRepository.save(paymentTransaction);
        log.debug("updated payment transaction record with transactionID: {} to {}", updatePaymentRequestDto.getTransactionId(),updatePaymentRequestDto.getStatus());
    }

    private void validateUpdateTransactionStatus(TransactionStatus status) {
        if (status != TransactionStatus.CANCELLED && status != TransactionStatus.TIMEOUT) {
            throw new IllegalArgumentException("Invalid status for update: Status must be 'TIMEOUT' or 'CANCELLED'.");
        }
    }

    private String maskCardNumber(String cardNumber){
        mask=mask+" "+cardNumber.substring(cardNumber.length() - 4);
        return mask;
    }
}

