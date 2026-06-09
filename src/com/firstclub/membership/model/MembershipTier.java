package com.firstclub.membership.model;

public enum MembershipTier {
    SILVER, GOLD, PLATINUM;

    public boolean isHigherThan(MembershipTier other) {
        return this.ordinal() > other.ordinal();
    }

    public boolean isLowerThan(MembershipTier other) {
        return this.ordinal() < other.ordinal();
    }
}
