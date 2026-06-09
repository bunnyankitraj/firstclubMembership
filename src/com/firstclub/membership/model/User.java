package com.firstclub.membership.model;

/**
 * A platform user. Holds the order data used for tier evaluation.
 */
public class User {

    private final String userId;
    private String cohort;
    private int orderCount;
    private double totalOrderValueThisMonth;

    public User(String userId, String cohort) {
        this.userId = userId;
        this.cohort = cohort;
    }

    public String getUserId()                    { return userId; }
    public String getCohort()                    { return cohort; }
    public int getOrderCount()                   { return orderCount; }
    public double getTotalOrderValueThisMonth()  { return totalOrderValueThisMonth; }

    public void setOrderCount(int orderCount)                          { this.orderCount = orderCount; }
    public void setTotalOrderValueThisMonth(double value)              { this.totalOrderValueThisMonth = value; }
}
