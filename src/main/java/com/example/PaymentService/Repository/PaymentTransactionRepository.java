package com.example.paymentService.Repository;

import com.example.paymentService.Model.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction,String> {
}
