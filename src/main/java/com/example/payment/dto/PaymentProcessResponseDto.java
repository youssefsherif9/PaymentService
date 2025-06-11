package com.example.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessResponseDto {
    private String status;
    private String transactionId;
    private String message;
}
