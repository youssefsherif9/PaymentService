package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentProcessRequestDto {
    private String transactionId;
    private double amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
