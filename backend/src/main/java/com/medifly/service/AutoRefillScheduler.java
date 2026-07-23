package com.medifly.service;

import com.medifly.model.Order;
import com.medifly.model.OrderItem;
import com.medifly.model.Subscription;
import com.medifly.repository.OrderRepository;
import com.medifly.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AutoRefillScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoRefillScheduler.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void processAutoRefills() {
        log.info("Starting Auto-Refill Subscription Processing Job...");
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> dueSubscriptions = subscriptionRepository.findByActiveTrueAndNextRefillDateBefore(now);

        for (Subscription sub : dueSubscriptions) {
            try {
                if (sub.getUser() != null && sub.getMedicine() != null) {
                    Order refillOrder = new Order();
                    refillOrder.setUser(sub.getUser());

                    OrderItem item = new OrderItem();
                    item.setName(sub.getMedicine().getBrandName());
                    item.setQty(sub.getQuantity());
                    item.setPrice(sub.getMedicine().getPrice());
                    item.setMedicine(String.valueOf(sub.getMedicine().getId()));

                    refillOrder.setOrderItems(Collections.singletonList(item));
                    refillOrder.setTotalPrice(sub.getMedicine().getPrice() * sub.getQuantity());
                    refillOrder.setStatus("pending");

                    orderRepository.save(refillOrder);

                    // Update next refill date
                    sub.setNextRefillDate(now.plusDays(sub.getFrequencyInDays() != null ? sub.getFrequencyInDays() : 30));
                    subscriptionRepository.save(sub);

                    log.info("Auto-refilled order created for user {} and medicine {}", sub.getUser().getId(), sub.getMedicine().getBrandName());
                }
            } catch (Exception e) {
                log.error("Error processing auto-refill for subscription id {}", sub.getId(), e);
            }
        }
    }
}
