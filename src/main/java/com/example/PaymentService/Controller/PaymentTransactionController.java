package com.example.PaymentService.Controller;

import com.example.PaymentService.Service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentService) {
        this.paymentService = paymentService;
    }
}

