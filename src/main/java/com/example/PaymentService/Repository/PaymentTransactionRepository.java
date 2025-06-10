package com.example.PaymentService.Repository;

import com.example.PaymentService.Model.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction,String> {
}
