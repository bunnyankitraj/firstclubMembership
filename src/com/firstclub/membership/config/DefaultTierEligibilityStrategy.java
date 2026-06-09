package com.firstclub.membership.config;

import com.firstclub.membership.model.MembershipTier;
import com.firstclub.membership.model.User;

/**
 * Tier eligibility rules:
 *   PLATINUM : orders >= 20  OR  monthlySpend >= 10000  OR  cohort == "VIP"
 *   GOLD     : orders >= 10  OR  monthlySpend >= 5000   OR  cohort == "PREMIUM"
 *   SILVER   : default
 */
public class DefaultTierEligibilityStrategy implements TierEligibilityStrategy {

    @Override
    public MembershipTier evaluate(User user) {
        if (user.getOrderCount() >= 20 || user.getTotalOrderValueThisMonth() >= 10_000
                || "VIP".equalsIgnoreCase(user.getCohort())) {
            return MembershipTier.PLATINUM;
        }
        if (user.getOrderCount() >= 10 || user.getTotalOrderValueThisMonth() >= 5_000
                || "PREMIUM".equalsIgnoreCase(user.getCohort())) {
            return MembershipTier.GOLD;
        }
        return MembershipTier.SILVER;
    }
}
