package com.example.paymentService.Controller;

import com.example.paymentService.dto.PaymentAmountRequestDto;
import com.example.paymentService.dto.PaymentAmountResponseDto;
import com.example.paymentService.Service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @PostMapping("/generate-payment")
    public ResponseEntity<PaymentAmountResponseDto> generatePayment(@RequestBody @Valid PaymentAmountRequestDto paymentAmountRequestDto){
        log.info("Received payment generation request for amount: {}", paymentAmountRequestDto.getAmount());
        PaymentAmountResponseDto paymentAmountResponseDto = paymentTransactionService.generatePayment(paymentAmountRequestDto);
        log.info("Successfully generated transaction ID {} for amount {} ", paymentAmountResponseDto.getTransactionId() ,paymentAmountResponseDto.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentAmountResponseDto);
    }
}

