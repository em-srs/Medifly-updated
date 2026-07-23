package com.medifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    @Embedded
    private ShippingAddress shippingAddress;

    private String paymentMethod = "Razorpay";

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "payment_id")),
        @AttributeOverride(name = "status", column = @Column(name = "payment_status")),
        @AttributeOverride(name = "update_time", column = @Column(name = "payment_update_time")),
        @AttributeOverride(name = "email_address", column = @Column(name = "payment_email_address"))
    })
    private PaymentResult paymentResult;

    private Double itemsPrice = 0.0;
    private Double taxPrice = 0.0;
    private Double platformFee = 0.0;
    private Double deliveryFee = 0.0;
    private Double coldChainFee = 0.0;
    private Double emergencyFee = 0.0;
    private Double lateNightFee = 0.0;
    private Double totalPrice = 0.0;

    private boolean isPaid = false;
    private LocalDateTime paidAt;
    private boolean isDelivered = false;
    private LocalDateTime deliveredAt;
    private String status = "pending";

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentResult getPaymentResult() { return paymentResult; }
    public void setPaymentResult(PaymentResult paymentResult) { this.paymentResult = paymentResult; }

    public Double getItemsPrice() { return itemsPrice; }
    public void setItemsPrice(Double itemsPrice) { this.itemsPrice = itemsPrice; }

    public Double getTaxPrice() { return taxPrice; }
    public void setTaxPrice(Double taxPrice) { this.taxPrice = taxPrice; }

    public Double getPlatformFee() { return platformFee; }
    public void setPlatformFee(Double platformFee) { this.platformFee = platformFee; }

    public Double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(Double deliveryFee) { this.deliveryFee = deliveryFee; }

    public Double getColdChainFee() { return coldChainFee; }
    public void setColdChainFee(Double coldChainFee) { this.coldChainFee = coldChainFee; }

    public Double getEmergencyFee() { return emergencyFee; }
    public void setEmergencyFee(Double emergencyFee) { this.emergencyFee = emergencyFee; }

    public Double getLateNightFee() { return lateNightFee; }
    public void setLateNightFee(Double lateNightFee) { this.lateNightFee = lateNightFee; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public boolean isDelivered() { return isDelivered; }
    public void setDelivered(boolean delivered) { isDelivered = delivered; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getRider() { return rider; }
    public void setRider(User rider) { this.rider = rider; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Embeddable
    public static class ShippingAddress {
        private String address;
        private String city;
        private String postalCode;
        private String country;

        public ShippingAddress() {}

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }

    @Embeddable
    public static class PaymentResult {
        private String id;
        private String status;
        private String update_time;
        private String email_address;

        public PaymentResult() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getUpdate_time() { return update_time; }
        public void setUpdate_time(String update_time) { this.update_time = update_time; }

        public String getEmail_address() { return email_address; }
        public void setEmail_address(String email_address) { this.email_address = email_address; }
    }
}
