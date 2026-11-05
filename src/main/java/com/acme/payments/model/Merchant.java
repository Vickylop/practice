package com.acme.payments.model;

import jakarta.persistence.*;

@Entity
public class Merchant {
    @Id private String id;
    private double balance;
    private String webhookUrl;

    public String getId() { return id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getWebhookUrl() { return webhookUrl; }
}
