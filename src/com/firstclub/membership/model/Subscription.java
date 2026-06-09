package com.firstclub.membership.model;

import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A user's membership subscription.
 * Mutations are protected by a per-instance ReentrantLock. (Bonus: concurrency)
 */
public class Subscription {

    private final String userId;
    private MembershipPlan plan;
    private MembershipTier tier;
    private SubscriptionStatus status;
    private LocalDate expiryDate;

    private final ReentrantLock lock = new ReentrantLock();

    public Subscription(String userId, MembershipPlan plan, MembershipTier tier) {
        this.userId = userId;
        this.plan = plan;
        this.tier = tier;
        this.status = SubscriptionStatus.ACTIVE;
        this.expiryDate = LocalDate.now().plusMonths(plan.getDurationInMonths());
    }

    public String getUserId()           { return userId; }
    public MembershipPlan getPlan()     { return plan; }
    public MembershipTier getTier()     { return tier; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getExpiryDate()    { return expiryDate; }
    public ReentrantLock getLock()      { return lock; }

    public void setTier(MembershipTier tier) { this.tier = tier; }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && !LocalDate.now().isAfter(expiryDate);
    }
}
