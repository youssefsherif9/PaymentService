package com.example.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessRequestDto {

    @NotBlank(message = "Transaction ID must not be empty")
    @NotNull(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Card number must not be empty")
    @NotNull(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Expiry date must not be empty")
    @NotNull(message = "Expiry date is required")
    private String expiresAt;

    @NotBlank(message = "CVV must not be empty")
    @NotNull(message = "CVV is required")
    private String cvv;
}
