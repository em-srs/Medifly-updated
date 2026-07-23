package com.medifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String vehicleType;
    private String licensePlate;
    private boolean isAvailable = true;

    @Embedded
    private User.Location currentLocation;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Rider() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public User.Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(User.Location currentLocation) { this.currentLocation = currentLocation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
