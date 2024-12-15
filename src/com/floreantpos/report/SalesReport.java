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
package com.floreantpos.report;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.dao.GenericDAO;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.apache.commons.collections4.set.UnmodifiableSortedSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.util.CurrencyUtil;

public class SalesReport extends Report {
	private SalesReportModel itemReportModel;
	private SalesReportModel modifierReportModel;

	public SalesReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		JasperReport itemReport = ReportUtil.getReport("sales_sub_report"); //$NON-NLS-1$
		JasperReport modifierReport = ReportUtil.getReport("sales_sub_report"); //$NON-NLS-1$

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", Messages.getString("SalesReport.3")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("reportTime", ReportService.formatFullDate(new Date())); //$NON-NLS-1$
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate())); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("terminalName", getTerminal() == null ? com.floreantpos.POSConstants.ALL : getTerminal().getName()); //$NON-NLS-1$
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel)); //$NON-NLS-1$
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel)); //$NON-NLS-1$
		map.put("currency", Messages.getString("SalesReport.8") + CurrencyUtil.getCurrencyName() + " (" + CurrencyUtil.getCurrencySymbol() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		map.put("itemTotalQuantity", itemReportModel.getTotalQuantity()); //$NON-NLS-1$
		map.put("itemTotal", itemReportModel.getTotalAsString()); //$NON-NLS-1$
		map.put("itemGrossTotal", itemReportModel.getGrossTotalAsDouble()); //$NON-NLS-1$
		map.put("itemDiscountTotal", itemReportModel.getDiscountTotalAsString()); //$NON-NLS-1$
		map.put("itemTaxTotal", itemReportModel.getTaxTotalAsString()); //$NON-NLS-1$
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString()); //$NON-NLS-1$
		map.put("modifierTotalQuantity", modifierReportModel.getTotalQuantity()); //$NON-NLS-1$
		map.put("modifierGrossTotal", modifierReportModel.getGrossTotalAsDouble()); //$NON-NLS-1$
		map.put("modifierTaxTotal", modifierReportModel.getTaxTotalAsString()); //$NON-NLS-1$
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString()); //$NON-NLS-1$
		map.put("modifierTotal", modifierReportModel.getTotalAsString()); //$NON-NLS-1$
		map.put("itemReport", itemReport); //$NON-NLS-1$
		map.put("modifierReport", modifierReport); //$NON-NLS-1$

		JasperReport masterReport = ReportUtil.getReport("sales_report"); //$NON-NLS-1$

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new JRViewer(print);
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		GenericDAO dao = new GenericDAO();
		Session session = null;

		try {
			session = dao.getSession();

			// Criteria for TicketItem
			Criteria criteria = session.createCriteria(TicketItem.class, "item");
			criteria.createCriteria("ticket", "t");

			if (!isIncludedFreeItems()) {
				criteria.add(Restrictions.ne("item." + TicketItem.PROP_UNIT_PRICE, 0.0));
			}
			if (getMenuGroup() != null && getMenuGroup() instanceof MenuGroup) {
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_GROUP_NAME, getMenuGroup().getName()));
			}
			criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, date1));
			criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, date2));
			criteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE));
			criteria.add(Restrictions.eq("t." + Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq("t." + Ticket.PROP_REFUNDED, Boolean.FALSE));
			criteria.add(Restrictions.eq("t." + Ticket.PROP_CLOSED, Boolean.TRUE));
			criteria.addOrder(Order.asc("item." + TicketItem.PROP_NAME));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.groupProperty("item." + TicketItem.PROP_ITEM_ID), "uniqueId");
			projections.add(Projections.groupProperty("item." + TicketItem.PROP_NAME), "name");
			projections.add(Projections.groupProperty("item." + TicketItem.PROP_UNIT_PRICE), "price");
			projections.add(Projections.groupProperty("item." + TicketItem.PROP_TAX_RATE), "taxRate");
			//TODO: fractional items not handled - item quantity vs count
			projections.add(Projections.sum("item." + TicketItem.PROP_ITEM_COUNT), "quantity");
			projections.add(Projections.sum("item." + TicketItem.PROP_TOTAL_AMOUNT_WITHOUT_MODIFIERS), "grossTotal");
			projections.add(Projections.sum("item." + TicketItem.PROP_DISCOUNT_AMOUNT), "discount");
			projections.add(Projections.sum("item." + TicketItem.PROP_TAX_AMOUNT_WITHOUT_MODIFIERS), "taxTotal");
			projections.add(Projections.sum("item." + TicketItem.PROP_SUBTOTAL_AMOUNT_WITHOUT_MODIFIERS), "total");

			criteria.setProjection(projections);
			criteria.setResultTransformer(Transformers.aliasToBean(ReportItem.class));

			List<ReportItem> itemList = criteria.list();

			itemReportModel = new SalesReportModel();
			itemReportModel.setItems(itemList);
			itemReportModel.calculateTotalQuantity();
			itemReportModel.calculateDiscountTotal();
			itemReportModel.calculateGrossTotal();
			itemReportModel.calculateTaxTotal();
			itemReportModel.calculateGrandTotal();
			itemReportModel.calculateTotal();

			// Repeat the same process for modifiers
			Criteria modifierCriteria = session.createCriteria(TicketItemModifier.class, "modifier");
			modifierCriteria.createCriteria("ticketItem", "item");
			modifierCriteria.createCriteria("item.ticket", "t");

			if (!isIncludedFreeItems()) {
				modifierCriteria.add(Restrictions.ne("modifier." + TicketItemModifier.PROP_UNIT_PRICE, 0.0));
			}
			modifierCriteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, date1));
			modifierCriteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, date2));
			modifierCriteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE));
			modifierCriteria.add(Restrictions.eq("t." + Ticket.PROP_VOIDED, Boolean.FALSE));
			modifierCriteria.add(Restrictions.eq("t." + Ticket.PROP_REFUNDED, Boolean.FALSE));
			modifierCriteria.add(Restrictions.eq("t." + Ticket.PROP_CLOSED, Boolean.TRUE));
			modifierCriteria.addOrder(Order.asc("modifier." + TicketItemModifier.PROP_NAME));

			ProjectionList modifierProjections = Projections.projectionList();
			modifierProjections.add(Projections.groupProperty("modifier." + TicketItemModifier.PROP_MODIFIER_ID), "uniqueId");
			modifierProjections.add(Projections.groupProperty("modifier." + TicketItemModifier.PROP_NAME), "name");
			modifierProjections.add(Projections.groupProperty("modifier." + TicketItemModifier.PROP_UNIT_PRICE), "price");
			modifierProjections.add(Projections.groupProperty("modifier." + TicketItemModifier.PROP_TAX_RATE), "taxRate");
			modifierProjections.add(Projections.sum("modifier." + TicketItemModifier.PROP_ITEM_COUNT), "quantity");
			modifierProjections.add(Projections.sum("modifier." + TicketItemModifier.PROP_TOTAL_AMOUNT), "grossTotal");
			modifierProjections.add(Projections.sum("modifier." + TicketItemModifier.PROP_TAX_AMOUNT), "taxTotal");
			modifierProjections.add(Projections.sum("modifier." + TicketItemModifier.PROP_SUB_TOTAL_AMOUNT), "total");

			modifierCriteria.setProjection(modifierProjections);
			modifierCriteria.setResultTransformer(Transformers.aliasToBean(ReportItem.class));

			List<ReportItem> modifierList = modifierCriteria.list();

			modifierReportModel = new SalesReportModel();
			modifierReportModel.setItems(modifierList);
			modifierReportModel.calculateTotalQuantity();
			modifierReportModel.calculateGrossTotal();
			modifierReportModel.calculateTaxTotal();
			modifierReportModel.calculateGrandTotal();
			modifierReportModel.calculateTotal();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}


}
