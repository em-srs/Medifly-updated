package com.medifly.controller;

import com.medifly.model.Rider;
import com.medifly.model.User;
import com.medifly.repository.RiderRepository;
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
@RequestMapping("/api/riders")
public class RiderController {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> registerRider(@RequestBody Rider rider) {
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

            rider.setUser(userOpt.get());
            Rider saved = riderRepository.save(rider);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }

    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody User.Location location) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authorized"));
        }

        try {
            Long userId = Long.parseLong(auth.getName());
            Optional<Rider> riderOpt = riderRepository.findByUserId(userId);
            if (riderOpt.isPresent()) {
                Rider r = riderOpt.get();
                r.setCurrentLocation(location);
                Rider updated = riderRepository.save(r);
                return ResponseEntity.ok(updated);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Rider record not found"));
    }
}
