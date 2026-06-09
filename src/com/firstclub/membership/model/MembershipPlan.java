package com.firstclub.membership.model;

/**
 * Represents the available membership plan durations with associated pricing.
 * Each plan defines how long the subscription lasts and its cost.
 */
public enum MembershipPlan {

    MONTHLY("Monthly Plan", 199.0, 1),
    QUARTERLY("Quarterly Plan", 499.0, 3),
    YEARLY("Yearly Plan", 1499.0, 12);

    private final String displayName;
    private final double price;          // price in INR
    private final int durationInMonths;

    MembershipPlan(String displayName, double price, int durationInMonths) {
        this.displayName = displayName;
        this.price = price;
        this.durationInMonths = durationInMonths;
    }

    public String getDisplayName() { return displayName; }
    public double getPrice() { return price; }
    public int getDurationInMonths() { return durationInMonths; }

    @Override
    public String toString() {
        return String.format("%s (₹%.0f / %d month%s)",
                displayName, price, durationInMonths, durationInMonths > 1 ? "s" : "");
    }
}
