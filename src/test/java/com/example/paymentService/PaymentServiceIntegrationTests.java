package com.example.paymentservice;

import com.example.paymentservice.dto.PaymentAmountRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
 class PaymentServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generatePayment_ShouldReturn201AndValidResponse() throws Exception {
        // Arrange
        PaymentAmountRequestDto requestDto = new PaymentAmountRequestDto();
        requestDto.setAmount(99.99);

        // Act & Assert
        mockMvc.perform(post("/api/payment/generate-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(99.99))
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
    }

    @Test
    void generatePayment_WithNegativeAmount_ShouldReturn400() throws Exception {
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

