package com.example.PaymentService.Dto;

public class PaymentAmountRequestDto {

    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentAmountRequestDto() {
    }

    public PaymentAmountRequestDto(double amount) {
        this.amount = amount;
    }
}
