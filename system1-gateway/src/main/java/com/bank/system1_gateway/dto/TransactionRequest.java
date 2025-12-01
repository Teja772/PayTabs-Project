package com.bank.system1_gateway.dto; // CHECK YOUR PACKAGE NAME!

public class TransactionRequest {
    private String cardNumber;
    private String pin;
    private double amount;
    private String type;

    // Getters and Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}