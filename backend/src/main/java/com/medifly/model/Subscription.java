package com.medifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity = 1;
    private Integer frequencyInDays = 30;
    private LocalDateTime nextRefillDate;
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Subscription() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getFrequencyInDays() { return frequencyInDays; }
    public void setFrequencyInDays(Integer frequencyInDays) { this.frequencyInDays = frequencyInDays; }

    public LocalDateTime getNextRefillDate() { return nextRefillDate; }
    public void setNextRefillDate(LocalDateTime nextRefillDate) { this.nextRefillDate = nextRefillDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
