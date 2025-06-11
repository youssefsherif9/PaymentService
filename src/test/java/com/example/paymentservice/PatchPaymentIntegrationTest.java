package com.example.paymentservice;


import com.example.paymentservice.enums.TransactionStatus;
import com.example.paymentservice.model.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles // <--- matches application-test.properties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatchPaymentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @BeforeEach
    void setUp() {
        // Create and save a mock transaction in embedded MongoDB
        paymentTransactionRepository.deleteAll();
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId("abc123");
        transaction.setAmount(100.0);
        transaction.setExpiresAt(Instant.parse("2025-12-01T00:00:00Z"));
        transaction.setStatus(TransactionStatus.NEW);
        transaction.setCreatedAt(Instant.now());
        transaction.setUpdatedAt(Instant.now());

        paymentTransactionRepository.save(transaction);
    }

    @Test
    void whenProcessPayment_thenReturnSuccess() throws Exception {
        String requestJson = """
        {
            "transactionId": "abc123",
            "amount": 100.0,
            "cardNumber": "4111111111111112",
            "expiryDate": "2025-12-01T00:00:00Z",
            "cvv": "123"
        }
        """;

        mockMvc.perform(patch("/api/payment/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                         .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void whenProcessPayment_thenReturnFailed() throws Exception {
        String requestJson = """
        {
            "transactionId": "abc123",
            "amount": 100.0,
            "cardNumber": "4111111111111111",
            "expiryDate": "2025-12-01T00:00:00Z",
            "cvv": "123"
        }
        """;

        mockMvc.perform(patch("/api/payment/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void whenProcessPayment_throwsEntityNotFoundException() throws Exception {
        String requestJson = """
        {
            "transactionId": "abc1234",
            "amount": 100.0,
            "cardNumber": "4111111111111111",
            "expiryDate": "2025-06-11T16:56:00+02:00",
            "cvv": "123"
        }
        """;

        mockMvc.perform(patch("/api/payment/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenProcessPayment_missingCardNumber_throwsException() throws Exception {
        String requestJson = """
        {
            "transactionId": "abc123",
            "amount": 100.0,
            "expiryDate": "2025-12-01T00:00:00Z",
            "cvv": "123"
        }
        """;

        mockMvc.perform(patch("/api/payment/process-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }


}
