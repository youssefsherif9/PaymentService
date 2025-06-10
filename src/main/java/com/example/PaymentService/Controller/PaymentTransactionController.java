package com.example.paymentService.Controller;

import com.example.paymentService.dto.PaymentAmountRequestDto;
import com.example.paymentService.dto.PaymentAmountResponseDto;
import com.example.paymentService.Service.PaymentTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")

public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @PostMapping("/generate-payment")
    public ResponseEntity<PaymentAmountResponseDto> generatePayment(@RequestBody @Valid PaymentAmountRequestDto paymentAmountRequestDto){
        PaymentAmountResponseDto paymentAmountResponseDto = paymentTransactionService.generatePayment(paymentAmountRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentAmountResponseDto);
    }
}

