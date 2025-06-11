package com.example.paymentservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentProcessResponseDto {
    private String status;
    private String transactionId;
    private String message;
}
