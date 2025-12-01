package com.bank.system2_corebank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Card {

    @Id
    private String cardNumber;
    private String pinHash;
    private double balance;
    private String customerName;

    public Card() {}

    public Card(String cardNumber, String pinHash, double balance, String customerName) {
        this.cardNumber = cardNumber;
        this.pinHash = pinHash;
        this.balance = balance;
        this.customerName = customerName;
    }

    // Getters and Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}