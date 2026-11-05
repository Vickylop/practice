package com.acme.payments.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id private String id;
    private String status;
    private double amount;
    private String currency;
    private String customerIban;
    private LocalDateTime updatedAt;
    @ManyToOne private Merchant merchant;

    public String getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public String getCustomerIban() { return customerIban; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Merchant getMerchant() { return merchant; }
}
