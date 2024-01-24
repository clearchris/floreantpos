/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Date;


/**
 * This is an object that contains data related to the GIFT_CERTIFICATE table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="GIFT_CERTIFICATE"
 */

public abstract class BaseGiftCertificate implements Comparable, Serializable {

	public static String REF = "GiftCerficate";
	public static String PROP_ID = "id";
	public static String PROP_NUMBER = "number";
	public static String PROP_PIN = "pin";
	public static String PROP_FACE_VALUE = "faceValue";
	public static String PROP_CURRENT_BALANCE = "currentBalance";
	public static String PROP_CREATE_DATE = "createDate";
	public static String PROP_SOLD_DATE = "soldDate";
	public static String PROP_SOLD_TRANSACTION = "soldTransaction";
	public static String PROP_TICKET_ID = "ticketId";
	public static String PROP_ITEM_ID = "itemId";
	public static String PROP_EXPIRY_DATE = "expiryDate";
    public static String PROP_USER_AUTO_ID = "user";
	public static String PROP_TICKET_ITEM_ID ="ticketItemId";

	// constructors
	public BaseGiftCertificate() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseGiftCertificate(Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private Integer id;

	// fields
	private String number;
    private String pin;
	private Date createDate;
	private Date soldDate;
	private Date expiryDate;
	private Double faceValue;
	private Double currentBalance;
	private Integer ticketItemId;
	private Integer user;

	// many to one
	private Integer ticketId;
	private Integer soldTransaction;
	private Integer itemId;

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.GiftCertificate)) return false;
		else {
			com.floreantpos.model.GiftCertificate giftCertificate = (com.floreantpos.model.GiftCertificate) obj;
			if (null == this.getId() || null == giftCertificate.getId()) return false;
			else return (this.getId().equals(giftCertificate.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Double faceValue) {
		this.faceValue = faceValue;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Integer getTicketItemId() {
		return ticketItemId;
	}

	public void setTicketItemId(Integer ticketItemId) {
		this.ticketItemId = ticketItemId;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getSoldTransaction() {
		return soldTransaction;
	}

	public void setSoldTransaction(Integer soldTransaction) {
		this.soldTransaction = soldTransaction;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}