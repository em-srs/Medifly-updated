package com.medifly.controller;

import com.medifly.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalMedicines", medicineRepository.count());
        stats.put("totalOrders", orderRepository.count());
        stats.put("totalPrescriptions", prescriptionRepository.count());
        stats.put("activeSubscriptions", subscriptionRepository.count());
        return ResponseEntity.ok(stats);
    }
}
