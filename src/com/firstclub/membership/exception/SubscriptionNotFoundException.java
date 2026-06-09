package com.firstclub.membership.exception;

/**
 * Thrown when a subscription lookup yields no result.
 */
public class SubscriptionNotFoundException extends RuntimeException {

    private final String userId;

    public SubscriptionNotFoundException(String userId) {
        super("No active subscription found for userId: " + userId);
        this.userId = userId;
    }

    public String getUserId() { return userId; }
}
