package com.medifly.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "salts")
public class Salt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String saltName;

    private String therapeuticClass;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    private List<String> commonUses;

    @ElementCollection
    private List<String> sideEffects;

    public Salt() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSaltName() { return saltName; }
    public void setSaltName(String saltName) { this.saltName = saltName; }

    public String getTherapeuticClass() { return therapeuticClass; }
    public void setTherapeuticClass(String therapeuticClass) { this.therapeuticClass = therapeuticClass; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCommonUses() { return commonUses; }
    public void setCommonUses(List<String> commonUses) { this.commonUses = commonUses; }

    public List<String> getSideEffects() { return sideEffects; }
    public void setSideEffects(List<String> sideEffects) { this.sideEffects = sideEffects; }
}
