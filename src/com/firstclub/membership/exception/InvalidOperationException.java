package com.firstclub.membership.exception;

/**
 * Thrown when a requested operation is not valid in the current state
 * (e.g., upgrading to a same or lower tier, subscribing when already subscribed).
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
