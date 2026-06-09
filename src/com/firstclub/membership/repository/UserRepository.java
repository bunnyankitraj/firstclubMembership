package com.firstclub.membership.repository;

import com.firstclub.membership.model.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final ConcurrentHashMap<String, User> store = new ConcurrentHashMap<>();

    public void save(User user) {
        store.put(user.getUserId(), user);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(store.get(userId));
    }

    public boolean existsById(String userId) {
        return store.containsKey(userId);
    }
}
