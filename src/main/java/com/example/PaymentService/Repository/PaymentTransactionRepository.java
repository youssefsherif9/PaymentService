package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction,String> {
}
