package com.floreantpos.model;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.model.base.BaseGiftCertificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GiftCertificate extends BaseGiftCertificate {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat numberDateFormat = new SimpleDateFormat("yyMMdd");

	public GiftCertificate() {
		super();
	}

	public GiftCertificate(Integer id) {
		super(id);
	}

	private String getUniqueNumber(){
		Date date = new Date();
		Random random = new Random();
		String number = numberDateFormat.format(date) + this.getTicketItemId();
		number += String.format("%03d", random.nextInt(1000));
		// TODO CHRIS: insert checksum here?
		return number;
	}

	public void ticketItemToGiftCertificate(TicketItem ticketItem) throws Exception{
		Date date = new Date();
		Ticket ticket = ticketItem.getTicket();

		if (!ticketItem.isGiftCertificateType()) throw new PosException(Messages.getString("GiftCertificate.0"));
		if (ticketItem.getQuantity()>1) throw new PosException(Messages.getString("GiftCertificate.0"));

		this.setTicketId(ticket.getId());
		this.setUser(ticket.getOwner().getAutoId());
		this.setSoldDate(date);
		this.setFaceValue(ticketItem.getTotalAmount());
		this.setCurrentBalance(ticketItem.getTotalAmount());
		this.setTicketItemId(ticketItem.getId());
		this.setItemId(ticketItem.getItemId());
		this.setCreateDate(date);
		this.setNumber(getUniqueNumber());

	}
}