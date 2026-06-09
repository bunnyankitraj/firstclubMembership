package com.firstclub.membership.service;

import com.firstclub.membership.config.TierBenefitConfig;
import com.firstclub.membership.config.TierEligibilityStrategy;
import com.firstclub.membership.exception.InvalidOperationException;
import com.firstclub.membership.exception.SubscriptionNotFoundException;
import com.firstclub.membership.exception.UserNotFoundException;
import com.firstclub.membership.model.*;
import com.firstclub.membership.repository.SubscriptionRepository;
import com.firstclub.membership.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

public class MembershipServiceImpl implements MembershipService {

    private final SubscriptionRepository subscriptionRepo;
    private final UserRepository userRepo;
    private final TierBenefitConfig benefitConfig;
    private final TierEligibilityStrategy eligibilityStrategy;

    public MembershipServiceImpl(SubscriptionRepository subscriptionRepo,
                                  UserRepository userRepo,
                                  TierBenefitConfig benefitConfig,
                                  TierEligibilityStrategy eligibilityStrategy) {
        this.subscriptionRepo    = subscriptionRepo;
        this.userRepo            = userRepo;
        this.benefitConfig       = benefitConfig;
        this.eligibilityStrategy = eligibilityStrategy;
    }

    // --- Discovery ---

    @Override
    public List<MembershipPlan> getAvailablePlans() {
        return Arrays.asList(MembershipPlan.values());
    }

    @Override
    public List<MembershipTier> getAvailableTiers() {
        return Arrays.asList(MembershipTier.values());
    }

    @Override
    public MembershipBenefit getBenefitsForTier(MembershipTier tier) {
        return benefitConfig.get(tier);
    }

    // --- Subscription lifecycle ---

    @Override
    public synchronized Subscription subscribe(String userId, MembershipPlan plan, MembershipTier tier) {
        if (!userRepo.existsById(userId)) throw new UserNotFoundException(userId);

        subscriptionRepo.findByUserId(userId).ifPresent(s -> {
            if (s.isActive()) throw new InvalidOperationException(
                    "User already has an active subscription.");
        });

        Subscription sub = new Subscription(userId, plan, tier);
        subscriptionRepo.save(sub);
        return sub;
    }

    @Override
    public Subscription upgradeTier(String userId, MembershipTier newTier) {
        Subscription sub = getActiveSubscriptionOrThrow(userId);
        sub.getLock().lock();
        try {
            if (!newTier.isHigherThan(sub.getTier()))
                throw new InvalidOperationException("Target tier must be higher than current tier.");
            sub.setTier(newTier);
            subscriptionRepo.save(sub);
            return sub;
        } finally {
            sub.getLock().unlock();
        }
    }

    @Override
    public Subscription downgradeTier(String userId, MembershipTier newTier) {
        Subscription sub = getActiveSubscriptionOrThrow(userId);
        sub.getLock().lock();
        try {
            if (!newTier.isLowerThan(sub.getTier()))
                throw new InvalidOperationException("Target tier must be lower than current tier.");
            sub.setTier(newTier);
            subscriptionRepo.save(sub);
            return sub;
        } finally {
            sub.getLock().unlock();
        }
    }

    @Override
    public void cancelSubscription(String userId) {
        Subscription sub = getActiveSubscriptionOrThrow(userId);
        sub.getLock().lock();
        try {
            sub.cancel();
            subscriptionRepo.save(sub);
        } finally {
            sub.getLock().unlock();
        }
    }

    // --- Query ---

    @Override
    public Subscription getSubscription(String userId) {
        return subscriptionRepo.findByUserId(userId)
                .filter(Subscription::isActive)
                .orElseThrow(() -> new SubscriptionNotFoundException(userId));
    }

    // --- Tier evaluation ---

    @Override
    public MembershipTier evaluateEligibleTier(String userId) {
        return eligibilityStrategy.evaluate(getUserOrThrow(userId));
    }

    @Override
    public Subscription checkAndApplyTierEvaluation(String userId) {
        MembershipTier eligible = eligibilityStrategy.evaluate(getUserOrThrow(userId));
        Subscription sub = getActiveSubscriptionOrThrow(userId);
        sub.getLock().lock();
        try {
            if (eligible != sub.getTier()) {
                sub.setTier(eligible);
                subscriptionRepo.save(sub);
            }
            return sub;
        } finally {
            sub.getLock().unlock();
        }
    }

    // --- Helpers ---

    private User getUserOrThrow(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Subscription getActiveSubscriptionOrThrow(String userId) {
        return subscriptionRepo.findByUserId(userId)
                .filter(Subscription::isActive)
                .orElseThrow(() -> new SubscriptionNotFoundException(userId));
    }
}
