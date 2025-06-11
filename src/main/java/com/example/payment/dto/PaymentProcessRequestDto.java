package com.example.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessRequestDto {
    private String transactionId;
    private double amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

}
