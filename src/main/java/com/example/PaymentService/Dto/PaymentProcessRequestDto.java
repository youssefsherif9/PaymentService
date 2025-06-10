package com.example.PaymentService.Dto;

public class PaymentProcessRequestDto {
    private String transactionId;
    private double amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public PaymentProcessRequestDto() {
    }

    public PaymentProcessRequestDto(String transactionId, String cvv, String expiryDate, String cardNumber, double amount) {
        this.transactionId = transactionId;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardNumber = cardNumber;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
