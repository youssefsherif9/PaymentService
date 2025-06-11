package com.example.paymentservice;

import com.example.paymentservice.dto.PaymentAmountRequestDto;
import com.example.paymentservice.dto.PaymentAmountResponseDto;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class PaymentServiceIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Test
    void generatePayment_ShouldReturn201AndValidResponse() throws Exception {
        // Arrange
        PaymentAmountRequestDto requestDto = new PaymentAmountRequestDto();
        requestDto.setAmount(99.99);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/payment/generate-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(99.99))
                .andExpect(jsonPath("$.expiresAt").isNotEmpty())
                .andReturn();

        // Extract the transactionId from response
        String responseBody = result.getResponse().getContentAsString();
        PaymentAmountResponseDto responseDto = objectMapper.readValue(responseBody, PaymentAmountResponseDto.class);
        String transactionId = responseDto.getTransactionId();

        // Assert that it exists in the DB
        boolean exists = paymentTransactionRepository
                .findByTransactionId(transactionId)
                .isPresent();

        assertTrue(exists, "Transaction record should exist in the database");
    }

    @Test
    void generatePayment_WithNonPositiveAmount_ShouldReturn400() throws Exception {
        // Arrange
        PaymentAmountRequestDto requestDto = new PaymentAmountRequestDto();
        requestDto.setAmount(-10.0); // Invalid negative amount

        // Act & Assert
        mockMvc.perform(post("/api/payment/generate-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}

