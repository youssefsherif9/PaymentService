package com.example.payment.model;

import com.example.payment.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "paymentTransactions")
public class PaymentTransaction {
    //TODO: id is long
    @Id
    private String id;
    @Indexed(unique = true)
    private String transactionId;
    private double amount;
    private Instant expiresAt;
    private TransactionStatus transactionStatus;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
