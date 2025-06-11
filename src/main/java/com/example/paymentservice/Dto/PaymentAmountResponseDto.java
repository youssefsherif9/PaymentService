package com.example.paymentservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentAmountResponseDto {
    private String transactionId;
    private double amount;
    private String expiresAt;

}
