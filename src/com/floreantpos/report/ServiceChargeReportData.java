package com.floreantpos.report;

import java.util.Date;

public class ServiceChargeReportData {
    private int userId;
    private Date date;
    private int ticketId;
    private double amount;
    private String userFirstName;
    private String userLastName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userFirstName + " " + userLastName;
    }

    public void setUserFirstName(String userName) {
        this.userFirstName = userName;
    }

    public void setUserLastName(String userName) {
        this.userLastName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}