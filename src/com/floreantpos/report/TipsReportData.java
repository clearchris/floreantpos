package com.floreantpos.report;

import java.util.Date;

import com.floreantpos.model.Gratuity;
import com.floreantpos.model.User;

public class TipsReportData {
	private User user;
	private Date date;
	private Gratuity gratuity;
	private int ticketId;
	private double amount;

	public void setUser(User user) {
		this.user = user;
	}

	public void setGratuity(Gratuity gratuity) {
		this.gratuity = gratuity;
		if(gratuity != null)
			setAmount(gratuity.getAmount());
	}

	public int getUserId() {
		return user.getUserId();
	}

	public String getUserName() {
		if(user != null)
			return user.getFirstName() + " " + user.getLastName();
		return "";
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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
}