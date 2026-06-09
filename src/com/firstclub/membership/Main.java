package com.firstclub.membership;

import com.firstclub.membership.config.DefaultTierEligibilityStrategy;
import com.firstclub.membership.config.TierBenefitConfig;
import com.firstclub.membership.model.*;
import com.firstclub.membership.repository.SubscriptionRepository;
import com.firstclub.membership.repository.UserRepository;
import com.firstclub.membership.service.MembershipService;
import com.firstclub.membership.service.MembershipServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Bootstrap
        UserRepository userRepo         = new UserRepository();
        SubscriptionRepository subRepo  = new SubscriptionRepository();
        MembershipService service       = new MembershipServiceImpl(
                subRepo, userRepo, new TierBenefitConfig(), new DefaultTierEligibilityStrategy());

        // Seed users
        User alice = new User("u001", "STANDARD");
        User bob   = new User("u002", "STANDARD");
        User carol = new User("u003", "VIP");
        userRepo.save(alice);
        userRepo.save(bob);
        userRepo.save(carol);

        // 1. Available plans and tiers
        section("1. Available Plans");
        service.getAvailablePlans().forEach(p -> System.out.println("  " + p));

        section("2. Available Tiers");
        service.getAvailableTiers().forEach(t -> System.out.println("  " + t));

        section("3. Tier Benefits");
        service.getAvailableTiers().forEach(t -> System.out.println("  " + service.getBenefitsForTier(t)));

        // 2. Subscribe
        section("4. Subscribe Alice — Monthly / SILVER");
        printSub(service.subscribe("u001", MembershipPlan.MONTHLY, MembershipTier.SILVER));

        // 3. Track membership and expiry
        section("5. Track Alice's Subscription");
        printSub(service.getSubscription("u001"));

        // 4. Upgrade
        section("6. Upgrade Alice: SILVER -> GOLD");
        printSub(service.upgradeTier("u001", MembershipTier.GOLD));

        section("7. Upgrade Alice: GOLD -> PLATINUM");
        printSub(service.upgradeTier("u001", MembershipTier.PLATINUM));

        // 4. Downgrade
        section("8. Downgrade Alice: PLATINUM -> SILVER");
        printSub(service.downgradeTier("u001", MembershipTier.SILVER));

        // 5. Cancel
        section("9. Subscribe Bob — Yearly / GOLD, then Cancel");
        Subscription bobSub = service.subscribe("u002", MembershipPlan.YEARLY, MembershipTier.GOLD);
        printSub(bobSub);
        service.cancelSubscription("u002");
        System.out.println("  After cancel — status: " + bobSub.getStatus());

        // 6. Tier evaluation — cohort based
        section("10. Tier Evaluation — Carol (VIP cohort -> Platinum)");
        service.subscribe("u003", MembershipPlan.QUARTERLY, MembershipTier.SILVER);
        System.out.println("  Eligible: " + service.evaluateEligibleTier("u003"));
        printSub(service.checkAndApplyTierEvaluation("u003"));

        // 6. Tier evaluation — order based
        section("11. Tier Evaluation — Alice (orderCount=25 -> Platinum)");
        alice.setOrderCount(25);
        System.out.println("  Eligible: " + service.evaluateEligibleTier("u001"));
        printSub(service.checkAndApplyTierEvaluation("u001"));

        // Bonus: Concurrency
        section("12. [Bonus] Concurrent Subscribe — only 1 should succeed");
        service.cancelSubscription("u001");
        AtomicInteger success = new AtomicInteger();
        AtomicInteger blocked = new AtomicInteger();
        ExecutorService pool  = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                try {
                    service.subscribe("u001", MembershipPlan.MONTHLY, MembershipTier.SILVER);
                    success.incrementAndGet();
                } catch (Exception e) {
                    blocked.incrementAndGet();
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("  Succeeded: " + success.get() + " | Blocked: " + blocked.get());
        System.out.println("  Result: " + (success.get() == 1 ? "PASSED" : "FAILED"));

        section("Done.");
    }

    private static void section(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    private static void printSub(Subscription sub) {
        System.out.printf("  userId=%s | plan=%s | tier=%s | status=%s | expires=%s%n",
                sub.getUserId(), sub.getPlan(), sub.getTier(), sub.getStatus(), sub.getExpiryDate());
    }
}
