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
import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.*;
import com.floreantpos.report.TipsReportData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.model.util.DateUtil;
import org.hibernate.transform.Transformers;

public class GratuityDAO extends BaseGratuityDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public GratuityDAO() {
	}

	public List<Gratuity> findByUser(User user) throws PosException {
		Session session = null;

		try {
			session = getSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Gratuity.PROP_OWNER, user));
			criteria.add(Restrictions.eq(Gratuity.PROP_PAID, Boolean.FALSE));
			criteria.add(Restrictions.eq(Gratuity.PROP_REFUNDED, Boolean.FALSE));

			return criteria.list();
		} catch (Exception e) {
			throw new PosException(Messages.getString("GratuityDAO.0") + user.getFirstName() + " " + user.getLastName()); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			closeSession(session);
		}
	}

	public void payGratuities(List<Gratuity> gratuities) {
		Session session = null;
		Transaction tx = null;

		double total = 0;
		try {
			session = getSession();
			tx = session.beginTransaction();
			for (Gratuity gratuity : gratuities) {
				total += gratuity.getAmount();
				gratuity.setPaid(true);
				session.saveOrUpdate(gratuity);

				Terminal terminal = gratuity.getTerminal();
				terminal.setCurrentBalance(terminal.getCurrentBalance() - gratuity.getAmount());
				session.saveOrUpdate(terminal);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new PosException(Messages.getString("GratuityDAO.2")); //$NON-NLS-1$
		} finally {
			closeSession(session);
		}
	}

	public TipsCashoutReport createReport(Date fromDate, Date toDate, User user) {
		Session session = null;

		try {
			session = getSession();

			fromDate = DateUtil.startOfDay(fromDate);
			toDate = DateUtil.endOfDay(toDate);

			Criteria criteria = session.createCriteria(Ticket.class);
			//criteria = criteria.createAlias(Ti, "t");
			criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			//criteria.add(Restrictions.eq(Ticket.PROP_DRAWER_RESETTED, Boolean.FALSE));
			//criteria.add(Restrictions.eq(Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));

			List list = criteria.list();

			TipsCashoutReport report = new TipsCashoutReport();
			report.setServer(user.getUserId() + "/" + user.toString()); //$NON-NLS-1$
			report.setFromDate(fromDate);
			report.setToDate(toDate);
			report.setReportTime(new Date());

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				Gratuity gratuity = ticket.getGratuity();

				TipsCashoutReportData data = new TipsCashoutReportData();
				data.setTicketId(ticket.getId());
				//				data.setSaleType(ticket.getCardType());
				data.setTicketTotal(ticket.getTotalAmount());

				if (gratuity != null && !gratuity.isRefunded()) {
					data.setTips(gratuity.getAmount());
					data.setPaid(gratuity.isPaid().booleanValue());
				}
				else {
					data.setTips(Double.valueOf(0));

				}
				report.addReportData(data);
			}
			report.calculateOthers();
			return report;
		} catch (Exception e) {
			throw new PosException(Messages.getString("GratuityDAO.4") + user.getFirstName() + " " + user.getLastName(), e); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			closeSession(session);
		}

	}

	public List<TipsReportData> createTipsReport(Date fromDate, Date toDate) {
		return createTipsReport(fromDate, toDate, null);
	}

	//TODO:  CHRIS I have to think about this.  I do like pushing the bulk of the
	// work to the database.  But I don't like that these  classes are created
	// for both explorers and reports, when they could be shared and  reduce code.
	public List<TipsReportData> createTipsReport(Date fromDate, Date toDate, User user) {
		Session session = null;

		try {
			session = getSession();

			fromDate = DateUtil.startOfDay(fromDate);
			toDate = DateUtil.endOfDay(toDate);

			Criteria criteria = session.createCriteria(Ticket.class);
			if (user != null)
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, user));
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.isNotNull("g." + Gratuity.PROP_ID));
			criteria.add(Restrictions.ne("g." + Gratuity.PROP_REFUNDED, Boolean.TRUE));
			criteria.addOrder(Order.asc(Ticket.PROP_OWNER));
			criteria.addOrder(Order.asc(Ticket.PROP_CREATE_DATE));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property(Ticket.PROP_ID), "ticketId");
			projections.add(Projections.property(Ticket.PROP_CREATE_DATE), "date");
			projections.add(Projections.property(Ticket.PROP_OWNER), "user");
			projections.add(Projections.property(Ticket.PROP_GRATUITY), "gratuity");

			criteria.setProjection(projections);
			criteria.setResultTransformer(Transformers.aliasToBean(TipsReportData.class));

			return criteria.list();

		} catch (Exception e) {
			throw new PosException(Messages.getString("GratuityDAO.4"), e);
		} finally {
			closeSession(session);
		}
	}
}