package com.firstclub.membership.config;

import com.firstclub.membership.model.MembershipBenefit;
import com.firstclub.membership.model.MembershipTier;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Defines the benefits for each tier. Edit here to reconfigure perks.
 */
public class TierBenefitConfig {

    private final Map<MembershipTier, MembershipBenefit> benefits;

    public TierBenefitConfig() {
        EnumMap<MembershipTier, MembershipBenefit> map = new EnumMap<>(MembershipTier.class);
        //                                           tier               free   disc  deals  priority
        map.put(MembershipTier.SILVER,   new MembershipBenefit(MembershipTier.SILVER,   true,  5,  false, false));
        map.put(MembershipTier.GOLD,     new MembershipBenefit(MembershipTier.GOLD,     true,  10, true,  false));
        map.put(MembershipTier.PLATINUM, new MembershipBenefit(MembershipTier.PLATINUM, true,  15, true,  true));
        this.benefits = Collections.unmodifiableMap(map);
    }

    public MembershipBenefit get(MembershipTier tier) {
        return benefits.get(tier);
    }

    public Map<MembershipTier, MembershipBenefit> getAll() {
        return benefits;
    }
}
