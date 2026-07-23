package com.medifly.controller;

import com.medifly.model.Prescription;
import com.medifly.model.User;
import com.medifly.repository.PrescriptionRepository;
import com.medifly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> uploadPrescription(@RequestBody Prescription prescription) {
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

            prescription.setUser(userOpt.get());
            Prescription saved = prescriptionRepository.save(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authorized"));
        }

        try {
            Long userId = Long.parseLong(auth.getName());
            List<Prescription> list = prescriptionRepository.findByUserId(userId);
            return ResponseEntity.ok(list);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verifyPrescription(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        String notes = payload.get("notes");

        Optional<Prescription> presOpt = prescriptionRepository.findById(id);
        if (presOpt.isPresent()) {
            Prescription p = presOpt.get();
            if (status != null) p.setStatus(status);
            if (notes != null) p.setAdminNotes(notes);
            Prescription updated = prescriptionRepository.save(p);
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Prescription not found"));
    }
}
