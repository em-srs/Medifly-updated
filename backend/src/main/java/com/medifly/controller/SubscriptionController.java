package com.medifly.controller;

import com.medifly.model.Subscription;
import com.medifly.model.User;
import com.medifly.repository.SubscriptionRepository;
import com.medifly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createSubscription(@RequestBody Subscription subscription) {
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

            subscription.setUser(userOpt.get());
            if (subscription.getNextRefillDate() == null) {
                subscription.setNextRefillDate(LocalDateTime.now().plusDays(subscription.getFrequencyInDays() != null ? subscription.getFrequencyInDays() : 30));
            }

            Subscription saved = subscriptionRepository.save(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getMySubscriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authorized"));
        }

        try {
            Long userId = Long.parseLong(auth.getName());
            List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user ID"));
        }
    }
}
