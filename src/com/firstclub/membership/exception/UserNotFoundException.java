package com.firstclub.membership.exception;

/**
 * Thrown when a user lookup yields no result.
 */
public class UserNotFoundException extends RuntimeException {

    private final String userId;

    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }

    public String getUserId() { return userId; }
}
