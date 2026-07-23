package com.medifly.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medifly.model.Medicine;
import com.medifly.model.Salt;
import com.medifly.repository.MedicineRepository;
import com.medifly.repository.SaltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Component
public class MedicineDataSeeder implements CommandLineRunner {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private SaltRepository saltRepository;

    @Override
    public void run(String... args) throws Exception {
        if (medicineRepository.count() > 0) {
            System.out.println("Medicines database already populated (" + medicineRepository.count() + " records). Skipping seeder.");
            return;
        }

        System.out.println("Populating database with medicines from medicines.json...");

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> rawList = null;

        // Try reading from classpath or frontend file
        try (InputStream is = getClass().getResourceAsStream("/data/medicines.json")) {
            if (is != null) {
                rawList = mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            }
        } catch (Exception ignored) {}

        if (rawList == null) {
            File localFile = new File("frontend/public/medicines.json");
            if (localFile.exists()) {
                rawList = mapper.readValue(localFile, new TypeReference<List<Map<String, Object>>>() {});
            } else {
                File relativeFile = new File("../frontend/public/medicines.json");
                if (relativeFile.exists()) {
                    rawList = mapper.readValue(relativeFile, new TypeReference<List<Map<String, Object>>>() {});
                }
            }
        }

        if (rawList == null || rawList.isEmpty()) {
            System.out.println("medicines.json file not found. Seeder idle.");
            return;
        }

        Map<String, Salt> saltCache = new HashMap<>();
        List<Medicine> medicineEntities = new ArrayList<>();

        for (Map<String, Object> map : rawList) {
            Medicine med = new Medicine();
            med.setMedicineId(String.valueOf(map.getOrDefault("id", "MED-" + UUID.randomUUID())));
            med.setBrandName(String.valueOf(map.getOrDefault("brandName", map.getOrDefault("name", "Medicine"))));
            
            String genericName = String.valueOf(map.getOrDefault("genericName", map.getOrDefault("salt", "Paracetamol")));
            med.setGenericName(genericName);

            // Handle Salt relationship
            Salt saltEntity = saltCache.computeIfAbsent(genericName.toLowerCase(), k -> {
                Optional<Salt> existing = saltRepository.findBySaltNameIgnoreCase(genericName);
                if (existing.isPresent()) return existing.get();
                Salt s = new Salt();
                s.setSaltName(genericName);
                s.setDescription("Active composition: " + genericName);
                return saltRepository.save(s);
            });
            med.setSaltComposition(saltEntity);

            med.setCategory(String.valueOf(map.getOrDefault("category", "General")));
            med.setDosageForm(String.valueOf(map.getOrDefault("form", "Tablet")));
            med.setStrength(String.valueOf(map.getOrDefault("strength", "500 mg")));
            med.setManufacturer(String.valueOf(map.getOrDefault("manufacturer", "Generic Labs")));
            med.setScheduleType(String.valueOf(map.getOrDefault("schedule", "OTC")));
            
            Object reqRx = map.get("requiresPrescription");
            if (reqRx == null) reqRx = map.get("prescriptionRequired");
            med.setRequiresPrescription(Boolean.TRUE.equals(reqRx));

            med.setColdChainRequired(Boolean.TRUE.equals(map.get("coldChain")));
            med.setPackSize(String.valueOf(map.getOrDefault("packSize", "Pack of 10")));

            Object p = map.get("price");
            med.setPrice(p instanceof Number ? ((Number) p).doubleValue() : 100.0);

            med.setStock(Boolean.TRUE.equals(map.getOrDefault("stock", true)));
            med.setInventoryCount(100);

            medicineEntities.add(med);
        }

        medicineRepository.saveAll(medicineEntities);
        System.out.println("Successfully seeded " + medicineEntities.size() + " medicines into database!");
    }
}
