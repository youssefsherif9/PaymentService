package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentAmountRequestDto;
import com.example.paymentservice.dto.PaymentAmountResponseDto;
import com.example.paymentservice.service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/generate-payment")
    public ResponseEntity<PaymentAmountResponseDto> generatePayment(@RequestBody @Valid PaymentAmountRequestDto paymentAmountRequestDto){
        log.info("Received payment generation request for amount: {}", paymentAmountRequestDto.getAmount());
        PaymentAmountResponseDto paymentAmountResponseDto = paymentTransactionService.generatePayment(paymentAmountRequestDto);
        log.info("Successfully generated transaction ID {} for amount {} ", paymentAmountResponseDto.getTransactionId() ,paymentAmountResponseDto.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentAmountResponseDto);
    }
}

