package com.medifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineId;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String genericName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salt_id")
    private Salt saltComposition;

    private String category;
    private String dosageForm;
    private String strength;
    private String manufacturer;
    private String scheduleType = "OTC";
    private boolean requiresPrescription = false;
    private boolean coldChainRequired = false;
    private String packSize;
    private Double price;
    private boolean stock = true;
    private Integer inventoryCount = 50;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Medicine() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }

    public Salt getSaltComposition() { return saltComposition; }
    public void setSaltComposition(Salt saltComposition) { this.saltComposition = saltComposition; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }

    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }

    public boolean isColdChainRequired() { return coldChainRequired; }
    public void setColdChainRequired(boolean coldChainRequired) { this.coldChainRequired = coldChainRequired; }

    public String getPackSize() { return packSize; }
    public void setPackSize(String packSize) { this.packSize = packSize; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public boolean isStock() { return stock; }
    public void setStock(boolean stock) { this.stock = stock; }

    public Integer getInventoryCount() { return inventoryCount; }
    public void setInventoryCount(Integer inventoryCount) { this.inventoryCount = inventoryCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
