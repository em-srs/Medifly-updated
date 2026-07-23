package com.medifly.controller;

import com.medifly.model.Pharmacy;
import com.medifly.model.User;
import com.medifly.repository.PharmacyRepository;
import com.medifly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> registerPharmacy(@RequestBody Pharmacy pharmacy) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authorized"));
        }

        try {
            Long userId = Long.parseLong(auth.getName());
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
            }

            pharmacy.setOwner(userOpt.get());
            Pharmacy saved = pharmacyRepository.save(pharmacy);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getPharmacyDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authorized"));
        }

        try {
            Long userId = Long.parseLong(auth.getName());
            Optional<Pharmacy> pharmacyOpt = pharmacyRepository.findByOwnerId(userId);
            return ResponseEntity.ok(pharmacyOpt.orElse(null));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }
}
