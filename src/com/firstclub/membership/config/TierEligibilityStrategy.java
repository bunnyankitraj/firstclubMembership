package com.firstclub.membership.config;

import com.firstclub.membership.model.MembershipTier;
import com.firstclub.membership.model.User;

/**
 * Strategy interface for determining the recommended tier for a user.
 *
 * Implementations can evaluate based on:
 *  - Order count thresholds
 *  - Monthly spend thresholds
 *  - Cohort membership
 *  - Any combination of the above (composite strategy)
 *
 * This makes tier eligibility logic independently testable and swappable
 * without changing service business logic.
 */
@FunctionalInterface
public interface TierEligibilityStrategy {

    /**
     * Evaluate which tier a user qualifies for based on their current profile.
     *
     * @param user the user to evaluate
     * @return the highest tier the user currently qualifies for
     */
    MembershipTier evaluate(User user);
}
