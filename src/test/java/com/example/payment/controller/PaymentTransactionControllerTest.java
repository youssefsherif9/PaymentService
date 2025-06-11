//package com.example.payment.controller;
//
//import com.example.payment.dto.PaymentProcessRequestDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.bson.Document;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.http.MediaType;
//
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.Instant;
//import java.util.Map;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
////@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
// class PaymentTransactionControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
////    @Autowired
////    private MongoTemplate mongoTemplate; // Embedded MongoDB Access
//
////    @BeforeEach
////    void setupDatabase() {
////        // Clear existing data before each test
////        mongoTemplate.getDb().getCollection("paymentTransaction").drop();
////
////        // Insert test data into embedded MongoDB
////        Document paymentTransaction = new Document(Map.of(
////                "_id", "6848ad1bc6de2fc44a6b10ba",
////                "transactionId", "abc123",
////                "amount", 100.0,
////                "cardNumber", "4111111111111111",
////                "expiresAt", Instant.parse("2025-12-01T00:00:00Z"),
////                "cvv", "123"
////        ));
////
////        mongoTemplate.getDb().getCollection("paymentTransaction").insertOne(paymentTransaction);
//        // No need to manually create collections; Flapdoodle handles it automatically
//  //  }
//
//    @Test
//    void testProcessPayment() throws Exception {
//        PaymentProcessRequestDto requestDto = new PaymentProcessRequestDto();
//        requestDto.setTransactionId("abc123");
//        requestDto.setAmount(100.0);
//        requestDto.setCardNumber("4111111111111111");
//        requestDto.setExpiryDate("2025-12-01T00:00:00Z");
//        requestDto.setCvv("123");
//
////        String requestJson = """
////        {
////            "transactionId": "abc123",
////            "amount": 100.0,
////            "cardNumber": "4111111111111111",
////            "expiresAt": "2025-12-01T00:00:00Z",
////            "cvv": "123"
////        }
////        """;
//
//        mockMvc.perform(post("/api/payment/process-payment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                      //  .content(requestJson))
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//}
