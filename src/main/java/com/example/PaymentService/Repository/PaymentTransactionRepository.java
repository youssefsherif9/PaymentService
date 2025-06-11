package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction,String> {
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
}
