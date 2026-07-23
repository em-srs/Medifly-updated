package com.medifly.controller;

import com.medifly.model.Medicine;
import com.medifly.model.Salt;
import com.medifly.repository.MedicineRepository;
import com.medifly.repository.SaltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private SaltRepository saltRepository;

    @GetMapping
    public ResponseEntity<List<Medicine>> getMedicines(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(medicineRepository.findByBrandNameContainingIgnoreCaseOrGenericNameContainingIgnoreCase(search, search));
        }
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable String id) {
        try {
            Long medId = Long.parseLong(id);
            Optional<Medicine> medicine = medicineRepository.findById(medId);
            if (medicine.isPresent()) {
                return ResponseEntity.ok(medicine.get());
            }
        } catch (NumberFormatException e) {
            Optional<Medicine> medicine = medicineRepository.findByMedicineId(id);
            if (medicine.isPresent()) {
                return ResponseEntity.ok(medicine.get());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Medicine not found"));
    }

    @GetMapping("/alternatives/{saltName}")
    public ResponseEntity<?> getAlternatives(@PathVariable String saltName) {
        Optional<Salt> saltOpt = saltRepository.findBySaltNameIgnoreCase(saltName);
        if (saltOpt.isPresent()) {
            List<Medicine> alternatives = medicineRepository.findBySaltComposition_Id(saltOpt.get().getId());
            return ResponseEntity.ok(alternatives);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/salt-comparison/{medicineId}")
    public ResponseEntity<?> compareSalts(@PathVariable String medicineId) {
        Optional<Medicine> targetOpt = Optional.empty();
        try {
            Long medId = Long.parseLong(medicineId);
            targetOpt = medicineRepository.findById(medId);
        } catch (NumberFormatException e) {
            targetOpt = medicineRepository.findByMedicineId(medicineId);
        }

        if (targetOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Medicine not found"));
        }

        Medicine target = targetOpt.get();
        if (target.getSaltComposition() == null) {
            return ResponseEntity.ok(Map.of("target", target, "substitutes", Collections.emptyList()));
        }

        List<Medicine> substitutes = medicineRepository.findBySaltComposition_Id(target.getSaltComposition().getId());
        substitutes.removeIf(m -> m.getId().equals(target.getId()));

        return ResponseEntity.ok(Map.of("target", target, "substitutes", substitutes));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<?> updateMedicinePrice(@PathVariable Long id, @RequestBody Map<String, Double> payload) {
        Double price = payload.get("price");
        if (price == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Price is required"));
        }

        Optional<Medicine> medOpt = medicineRepository.findById(id);
        if (medOpt.isPresent()) {
            Medicine med = medOpt.get();
            med.setPrice(price);
            med.setUpdatedAt(LocalDateTime.now());
            Medicine updated = medicineRepository.save(med);
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Medicine not found"));
    }
}
