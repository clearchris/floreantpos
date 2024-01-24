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
package com.floreantpos.model.dao;

import java.util.Date;
import java.util.List;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.model.Ticket;

import com.floreantpos.model.GiftCertificate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class GiftCertificateDAO extends BaseGiftCertificateDAO {

	public GiftCertificateDAO () {}

	public void chargeGiftCertificate (Session session, String number, double amount) throws PosException, Exception {
		GiftCertificate giftCertificate = getGiftCertificateByNumber(session, number);
		if (giftCertificate == null) throw new PosException(Messages.getString("GiftCertificate.3"));
		if (giftCertificate.getCurrentBalance() < amount)
			throw new PosException(Messages.getString("GiftCertificate.3")+ giftCertificate.getCurrentBalance());
		giftCertificate.setCurrentBalance(giftCertificate.getCurrentBalance()-amount);
		session.saveOrUpdate(giftCertificate);
	}

	public GiftCertificate getGiftCertificateById(Integer id){
		Session session = null;
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(GiftCertificate.PROP_ID, id));
			return (GiftCertificate) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

	public GiftCertificate getGiftCertificateByNumber(Session session, String number) throws Exception {
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(GiftCertificate.PROP_NUMBER, number));
		return (GiftCertificate) criteria.uniqueResult();
	}

	public GiftCertificate getGiftCertificateByNumber(String number){
		Session session = null;
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(GiftCertificate.PROP_NUMBER, number));
			return (GiftCertificate) criteria.uniqueResult();
		} finally {
			closeSession(session);
		}
	}

	public double getBalanceByNumber(String number){
		GiftCertificate gc = getGiftCertificateByNumber(number);
		return (gc == null)? 0.0 : gc.getCurrentBalance();
	}

	public void voidGiftCertificatesByTicket(Session session, Ticket ticket) throws Exception{
		List<GiftCertificate> giftCertificates = getGiftCertificatesByTicket(session, ticket);
		for (GiftCertificate giftCertificate : giftCertificates){
			giftCertificate.setFaceValue(0.0);
			giftCertificate.setCurrentBalance(0.0);
			session.saveOrUpdate(giftCertificate);
		}
	}

	public List<GiftCertificate> getGiftCertificatesByTicket(Session session, Ticket ticket) throws Exception{
		Criteria criteria = session.createCriteria(getReferenceClass());
		if (ticket != null)
			criteria.add(Restrictions.eq(GiftCertificate.PROP_TICKET_ID, ticket.getId()));
		List list = criteria.list();
		return list;
	}

	public List<GiftCertificate> getGiftCertificatesByDate(Date startDate, Date endDate) {
		Session session = null;

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			if (startDate != null && endDate != null) {
				criteria.add(Restrictions.ge(GiftCertificate.PROP_CREATE_DATE, startDate));
				criteria.add(Restrictions.le(GiftCertificate.PROP_CREATE_DATE, endDate));
			}
			List list = criteria.list();
			return list;
		} finally {
			closeSession(session);
		}
	}

	public double calculateGiftCertSoldAmount(Date fromDate, Date toDate) {
		Session session = null;

		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ge(GiftCertificate.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(GiftCertificate.PROP_CREATE_DATE, toDate));
			//criteria.add(Restrictions.eq(GiftCertificate.PROP_TRANSACTION_TYPE, TransactionType.CREDIT.name()));

			criteria.setProjection(Projections.sum(GiftCertificate.PROP_FACE_VALUE));
			return getDoubleAmount(criteria.uniqueResult());
		} finally {
			closeSession(session);
		}
	}

	private double getDoubleAmount(Object result) {
		if (result != null && result instanceof Number) {
			return ((Number) result).doubleValue();
		}
		return 0;
	}

}