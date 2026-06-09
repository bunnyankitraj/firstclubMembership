package com.firstclub.membership.model;

/**
 * Benefits attached to a membership tier.
 * Configurable via TierBenefitConfig.
 */
public class MembershipBenefit {

    private final MembershipTier tier;
    private final boolean freeDelivery;
    private final int discountPercent;
    private final boolean exclusiveDeals;
    private final boolean prioritySupport;

    public MembershipBenefit(MembershipTier tier, boolean freeDelivery,
                              int discountPercent, boolean exclusiveDeals,
                              boolean prioritySupport) {
        this.tier = tier;
        this.freeDelivery = freeDelivery;
        this.discountPercent = discountPercent;
        this.exclusiveDeals = exclusiveDeals;
        this.prioritySupport = prioritySupport;
    }

    public MembershipTier getTier()      { return tier; }
    public boolean hasFreeDelivery()     { return freeDelivery; }
    public int getDiscountPercent()      { return discountPercent; }
    public boolean hasExclusiveDeals()   { return exclusiveDeals; }
    public boolean hasPrioritySupport()  { return prioritySupport; }

    @Override
    public String toString() {
        return String.format("[%s] freeDelivery=%b | discount=%d%% | exclusiveDeals=%b | prioritySupport=%b",
                tier, freeDelivery, discountPercent, exclusiveDeals, prioritySupport);
    }
}
