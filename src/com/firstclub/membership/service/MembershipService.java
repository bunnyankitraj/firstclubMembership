package com.firstclub.membership.service;

import com.firstclub.membership.model.*;

import java.util.List;

public interface MembershipService {

    // --- Discovery ---
    List<MembershipPlan> getAvailablePlans();
    List<MembershipTier> getAvailableTiers();
    MembershipBenefit getBenefitsForTier(MembershipTier tier);

    // --- Subscription lifecycle ---
    Subscription subscribe(String userId, MembershipPlan plan, MembershipTier tier);
    Subscription upgradeTier(String userId, MembershipTier newTier);
    Subscription downgradeTier(String userId, MembershipTier newTier);
    void cancelSubscription(String userId);

    // --- Query ---
    Subscription getSubscription(String userId);

    // --- Tier evaluation ---
    MembershipTier evaluateEligibleTier(String userId);
    Subscription checkAndApplyTierEvaluation(String userId);
}
