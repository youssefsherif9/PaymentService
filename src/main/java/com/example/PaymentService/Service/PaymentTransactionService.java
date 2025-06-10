package com.example.PaymentService.Service;

import com.example.PaymentService.Dto.*;
import com.example.PaymentService.Model.PaymentTransaction;
import com.example.PaymentService.Repository.PaymentTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PaymentTransactionService {


    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository){
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

}

