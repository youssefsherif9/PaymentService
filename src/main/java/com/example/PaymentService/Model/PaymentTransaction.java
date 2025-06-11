package com.example.paymentservice.model;

import com.example.paymentservice.enums.TransactionStatus;
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
@EnableMongoAuditing
@Document(collection = "paymentTransactions")
public class PaymentTransaction {

    @Id
    private String id;

    @Indexed(unique = true)
    private String transactionId;
    private double amount;
    private Instant expiresAt;
    private TransactionStatus status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public PaymentTransaction( String transactionId, double amount, Instant expiresAt, TransactionStatus status) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.expiresAt = expiresAt;
        this.status = status;
    }
}
