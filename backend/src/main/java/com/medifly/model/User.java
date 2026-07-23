package com.medifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String role = "user"; // user, pharmacy, rider, admin

    @Embedded
    private Address address;

    private boolean isSubscribed = false;
    private String subscriptionPlan = "none";
    private LocalDateTime subscriptionExpiry;

    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(Long id, String name, String email, String password, String phone, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public boolean isSubscribed() { return isSubscribed; }
    public void setSubscribed(boolean subscribed) { isSubscribed = subscribed; }

    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

    public LocalDateTime getSubscriptionExpiry() { return subscriptionExpiry; }
    public void setSubscriptionExpiry(LocalDateTime subscriptionExpiry) { this.subscriptionExpiry = subscriptionExpiry; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Embeddable
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String zipCode;

        @Embedded
        private Location location;

        public Address() {}

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }

        public Location getLocation() { return location; }
        public void setLocation(Location location) { this.location = location; }
    }

    @Embeddable
    public static class Location {
        private Double lat;
        private Double lng;

        public Location() {}
        public Location(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }

        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
    }
}
