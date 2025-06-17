package com.example.paymentservice.Dto;

import com.example.paymentservice.enums.TransactionStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentRequestDto {

    @NotBlank(message = "Transaction ID must not be empty")
    @NotNull(message = "Transaction ID is required")
    private String transactionId;


    @NotNull(message = "Status is required")
    private TransactionStatus status;

}
