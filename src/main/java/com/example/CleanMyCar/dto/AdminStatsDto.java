package com.example.CleanMyCar.dto;

public class AdminStatsDto {
    private long todaysNewSubscriptions;
    private long todaysRevenue;     // in rupees (integer)
    private long totalRevenue;      // in rupees (integer)
    private long renewalsLast30Days;
    private long totalCustomers;
    private long totalSubscriptions;

    public AdminStatsDto() {}

    public long getTodaysNewSubscriptions() { return todaysNewSubscriptions; }
    public void setTodaysNewSubscriptions(long todaysNewSubscriptions) { this.todaysNewSubscriptions = todaysNewSubscriptions; }

    public long getTodaysRevenue() { return todaysRevenue; }
    public void setTodaysRevenue(long todaysRevenue) { this.todaysRevenue = todaysRevenue; }

    public long getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(long totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getRenewalsLast30Days() { return renewalsLast30Days; }
    public void setRenewalsLast30Days(long renewalsLast30Days) { this.renewalsLast30Days = renewalsLast30Days; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

    public long getTotalSubscriptions() { return totalSubscriptions; }
    public void setTotalSubscriptions(long totalSubscriptions) { this.totalSubscriptions = totalSubscriptions; }
}

