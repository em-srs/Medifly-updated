package com.medifly.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medifly.model.Medicine;
import com.medifly.model.Salt;
import com.medifly.repository.MedicineRepository;
import com.medifly.repository.SaltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MedicineDataSeeder implements CommandLineRunner {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private SaltRepository saltRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        long currentCount = medicineRepository.count();
        if (currentCount > 0) {
            System.out.println("✅ Database already populated with " + currentCount + " medicine records. Skipping seeder.");
            return;
        }

        File csvFile = new File("data/meds_dB_original.csv");
        if (!csvFile.exists()) {
            csvFile = new File("backend/data/meds_dB_original.csv");
        }

        if (csvFile.exists()) {
            System.out.println("🚀 Found CSV dataset (" + csvFile.getName() + "). Starting high-speed batch import into Supabase PostgreSQL...");
            importFromCsv(csvFile);
            return;
        }

        System.out.println("Populating database with fallback medicines.json...");
        importFromJson();
    }

    private void importFromCsv(File csvFile) {
        String sql = "INSERT INTO medicines (medicine_id, brand_name, generic_name, manufacturer, category, pack_size, price, stock, inventory_count, schedule_type, requires_prescription, cold_chain_required, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int batchSize = 5000;
        int totalInserted = 0;
        long startTime = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine(); // Skip CSV Header line

            List<Object[]> batch = new ArrayList<>();
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            while ((line = reader.readLine()) != null) {
                // Parse CSV with handling for quotes and commas
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (fields.length < 3) continue;

                String medId = "MED-" + clean(fields[0]);
                String name = clean(fields[1]);
                if (name.isEmpty()) continue;

                double price = 100.0;
                try {
                    if (fields.length > 2 && !clean(fields[2]).isEmpty()) {
                        price = Double.parseDouble(clean(fields[2]));
                    }
                } catch (NumberFormatException ignored) {}

                String manufacturer = fields.length > 4 ? clean(fields[4]) : "Generic Labs";
                String type = fields.length > 5 ? clean(fields[5]) : "allopathy";
                String packSize = fields.length > 6 ? clean(fields[6]) : "Pack of 10";

                String comp1 = fields.length > 7 ? clean(fields[7]) : "";
                String comp2 = fields.length > 8 ? clean(fields[8]) : "";
                String genericName = comp1;
                if (!comp2.isEmpty()) {
                    genericName = genericName.isEmpty() ? comp2 : genericName + " + " + comp2;
                }
                if (genericName.isEmpty()) genericName = name;

                batch.add(new Object[]{
                    medId, name, genericName, manufacturer, type, packSize, price,
                    true, 100, "OTC", false, false, now, now
                });

                if (batch.size() >= batchSize) {
                    jdbcTemplate.batchUpdate(sql, batch);
                    totalInserted += batch.size();
                    batch.clear();
                    System.out.println("   --> Seeded " + totalInserted + " records into Supabase...");
                }
            }

            if (!batch.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batch);
                totalInserted += batch.size();
            }

            long elapsedSec = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("🎉 Successfully imported " + totalInserted + " medicine records into Supabase PostgreSQL in " + elapsedSec + " seconds!");

        } catch (Exception e) {
            System.err.println("❌ CSV Import failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importFromJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> rawList = null;

        try (InputStream is = getClass().getResourceAsStream("/data/medicines.json")) {
            if (is != null) {
                rawList = mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            }
        } catch (Exception ignored) {}

        if (rawList == null) {
            File localFile = new File("frontend/public/medicines.json");
            if (localFile.exists()) {
                rawList = mapper.readValue(localFile, new TypeReference<List<Map<String, Object>>>() {});
            }
        }

        if (rawList == null || rawList.isEmpty()) {
            System.out.println("No json data found. Seeder idle.");
            return;
        }

        List<Medicine> medicineEntities = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            Medicine med = new Medicine();
            med.setMedicineId(String.valueOf(map.getOrDefault("id", "MED-" + UUID.randomUUID())));
            med.setBrandName(String.valueOf(map.getOrDefault("brandName", map.getOrDefault("name", "Medicine"))));
            med.setGenericName(String.valueOf(map.getOrDefault("genericName", map.getOrDefault("salt", "Paracetamol"))));
            med.setCategory(String.valueOf(map.getOrDefault("category", "General")));
            med.setManufacturer(String.valueOf(map.getOrDefault("manufacturer", "Generic Labs")));
            med.setPackSize(String.valueOf(map.getOrDefault("packSize", "Pack of 10")));
            Object p = map.get("price");
            med.setPrice(p instanceof Number ? ((Number) p).doubleValue() : 100.0);
            med.setStock(true);
            med.setInventoryCount(100);
            medicineEntities.add(med);
        }

        medicineRepository.saveAll(medicineEntities);
        System.out.println("Successfully seeded " + medicineEntities.size() + " medicines from JSON!");
    }

    private String clean(String str) {
        if (str == null) return "";
        return str.replace("\"", "").trim();
    }
}
