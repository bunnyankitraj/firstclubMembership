package com.firstclub.membership.repository;

import com.firstclub.membership.model.Subscription;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionRepository {

    private final ConcurrentHashMap<String, Subscription> store = new ConcurrentHashMap<>();

    public void save(Subscription subscription) {
        store.put(subscription.getUserId(), subscription);
    }

    public Optional<Subscription> findByUserId(String userId) {
        return Optional.ofNullable(store.get(userId));
    }

    public boolean existsByUserId(String userId) {
        return store.containsKey(userId);
    }
}
