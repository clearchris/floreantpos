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
/*
 * CashDrawerReportDialog.java
 *
 * Created on August 24, 2006, 11:20 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import net.sf.jasperreports.engine.JasperPrint;
import org.apache.ecs.Document;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.DrawerPullVoidTicketEntry;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.dao.CashDrawerDAO;
import com.floreantpos.print.DrawerpullReportService;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar mshahriar@gmail.com
 */
public class DrawerPullReportDialog extends POSDialog {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy"); //$NON-NLS-1$
	private DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

	private DrawerPullReport drawerPullReport;
	private Terminal terminal;

	public DrawerPullReportDialog() {
		super(Application.getPosWindow(), true);
		initComponents();
	}

	public DrawerPullReportDialog(JDialog parent, boolean modal) {
		super(Application.getPosWindow(), true);

		initComponents();
	}

	public void initialize() throws Exception {


		taReport.setContentType("text/html"); //$NON-NLS-1$
		taReport.setEditable(false);
		taReport.setMargin(new Insets(0, 10, 0, 10));
		taReport.setText(createReport());
		taReport.setCaretPosition(0);

		taReport.setPreferredSize(PosUIManager.getSize(360, 100));
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		setLayout(new BorderLayout(5, 5));

		titlePanel1 = new TitlePanel();
		add(titlePanel1, BorderLayout.NORTH);

		taReport = new JEditorPane();
		taReport.setContentType("text/html"); //$NON-NLS-1$

		//PosScrollPane scrollPane = new PosScrollPane(taReport);
		terminal = Application.getInstance().refreshAndGetTerminal();
		try {
			drawerPullReport = DrawerpullReportService.buildDrawerPullReport();
			drawerPullReport.setAssignedUser(terminal.getAssignedUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		jrViewer = new net.sf.jasperreports.swing.JRViewer(showReport());
		PosScrollPane scrollPane = new PosScrollPane(jrViewer);
		add(scrollPane);

		JPanel buttonPanel = new JPanel(new MigLayout("fill", "", "[fill, grow][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		buttonPanel.add(new JSeparator(), "grow,span,wrap"); //$NON-NLS-1$
		buttonPanel.add(btnPrint = new PosButton(Messages.getString("DrawerPullReportDialog.8")), "grow"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonPanel.add(btnFinish = new PosButton(Messages.getString("DrawerPullReportDialog.0")), "grow"); //$NON-NLS-1$ //$NON-NLS-2$

		add(buttonPanel, BorderLayout.SOUTH);

		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCloseDialog();
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrintReport();
			}

		});
	}// </editor-fold>//GEN-END:initComponents

	//    private void doResetCashDrawer() {//GEN-FIRST:event_btnResetCashDrawerActionPerformed
	//    	int option = POSMessageDialog.showYesNoQuestionDialog(this, "Sure reset cash drawer?", "Confirm");
	//    	if(option != JOptionPane.YES_OPTION) return;
	//    	
	//    	Application app = Application.getInstance();
	//    	Terminal terminal = app.getTerminal();
	//    	
	//    	double drawerBalance = NumberSelectionDialog2.takeDoubleInput("Enter amount", "Please enter drawer balance", terminal.getOpeningBalance());
	//    	if(Double.isNaN(drawerBalance)) {
	//    		return;
	//    	}
	//    	
	//    	User user = Application.getCurrentUser();
	//    	
	//    	TerminalDAO dao = new TerminalDAO();
	//    	try {
	//			dao.resetCashDrawer(drawerPullReport, terminal, user, drawerBalance);
	//			POSMessageDialog.showMessage(this, "Drawer resetted");
	//			doCloseDialog();
	//		} catch (Exception e) {
	//			POSMessageDialog.showError("Cannot save", e);
	//		}
	//    }//GEN-LAST:event_btnResetCashDrawerActionPerformed

	private void doCloseDialog() {//GEN-FIRST:event_btnFinishActionPerformed
		dispose();
	}//GEN-LAST:event_btnFinishActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.PosButton btnPrint;
	//    private com.floreantpos.swing.PosButton btnResetCashDrawer;
	private javax.swing.JEditorPane taReport;
	private net.sf.jasperreports.swing.JRViewer jrViewer;

	private com.floreantpos.ui.TitlePanel titlePanel1;

	// End of variables declaration//GEN-END:variables

	void createReportHeader(Document document) {
		P p = new P();
		//p.addElement(new Font().addAttribute("size", "7"));
		p.addAttribute("align", "center"); //$NON-NLS-1$ //$NON-NLS-2$
		p.addElement("==================================="); //$NON-NLS-1$
		p.addElement(new BR());
		p.addElement(Messages.getString("DrawerPullReportDialog.15")); //$NON-NLS-1$
		p.addElement(new BR());
		p.addElement(Messages.getString("DrawerPullReportDialog.16") + Application.getInstance().getTerminal().getName()); //$NON-NLS-1$
		p.addElement(new BR());
		p.addElement("==================================="); //$NON-NLS-1$
		document.appendBody(p);
	}

	void createSectionHeader(Document document, String headerText) {
		P p = new P();
		p.addAttribute("align", "center"); //$NON-NLS-1$ //$NON-NLS-2$
		p.addElement(headerText);
		p.addElement(new HR());
		document.appendBody(p);
	}

	void addSeparator(Document document) {
		P p = new P();
		HR hr = new HR();
		hr.addAttribute("style", "border: dashed;"); //$NON-NLS-1$ //$NON-NLS-2$
		p.addElement(hr);
		document.appendBody(p);
	}

	void addTableSeparator(Table table) {
		TR tr = new TR();
		TD td = new TD();
		td.addAttribute("colspan", 2); //$NON-NLS-1$
		td.addAttribute("align", "right"); //$NON-NLS-1$ //$NON-NLS-2$
		HR hr = new HR();
		hr.addAttribute("style", "border: dashed;"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(hr);
		tr.addElement(td);
		table.addElement(tr);
	}

	void addExceptionTableSeparator(Table table) {
		TR tr = new TR();
		TD td = new TD();
		td.addAttribute("colspan", 5); //$NON-NLS-1$
		td.addAttribute("align", "right"); //$NON-NLS-1$ //$NON-NLS-2$
		HR hr = new HR();
		hr.addAttribute("style", "border: dashed;"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(hr);
		tr.addElement(td);
		table.addElement(tr);
	}

	void addTableRow(Table table, String column1, String coulmn2) {
		TR tr = new TR();
		tr.addElement(new TD().addElement(column1));
		tr.addElement(new TD().addAttribute("align", "right").addElementToRegistry(coulmn2)); //$NON-NLS-1$ //$NON-NLS-2$
		table.addElement(tr);
	}

	void addDiscountTableRow(Table table, String column1, String coulmn2) {
		TR tr = new TR();
		tr.addElement(new TD().addAttribute("style", "padding-left: 50px;").addElementToRegistry(column1)); //$NON-NLS-1$ //$NON-NLS-2$
		tr.addElement(new TD().addAttribute("align", "right").addElementToRegistry(coulmn2)); //$NON-NLS-1$ //$NON-NLS-2$
		table.addElement(tr);
	}

	void addExceptionTableRow(Table table, String column1, String coulmn2, String coulmn3, String column5) {
		TR tr = new TR();
		TD td = new TD();
		td.addAttribute("valign", "top"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(column1);
		tr.addElement(td);

		td = new TD();
		td.addAttribute("valign", "top"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(coulmn2);
		tr.addElement(td);

		td = new TD();
		td.addAttribute("valign", "top"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addAttribute("align", "right"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(coulmn3);
		tr.addElement(td);

		td = new TD();
		td.addAttribute("valign", "top"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addAttribute("align", "right"); //$NON-NLS-1$ //$NON-NLS-2$
		td.addElement(column5);
		tr.addElement(td);

		table.addElement(tr);
	}

	public String createReport() throws Exception {
		Document document = new Document();

		Table table = null;

		createReportHeader(document);

		P p = new P();
		p.addElement(Messages.getString("DrawerPullReportDialog.1") + dateFormat.format(new Date())); //$NON-NLS-1$
		document.appendBody(p);

		createSectionHeader(document, Messages.getString("DrawerPullReportDialog.2")); //$NON-NLS-1$
		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "&nbsp;" + Messages.getString("DrawerPullReportDialog.3"), decimalFormat.format(drawerPullReport.getNetSales())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.5"), decimalFormat.format(drawerPullReport.getSalesTax())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + Messages.getString("SALES_DELIVERY_CHARGE"), decimalFormat.format(drawerPullReport.getSalesDeliveryCharge())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "=" + Messages.getString("DrawerPullReportDialog.7"), decimalFormat.format(drawerPullReport.getTotalRevenue())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.10"), decimalFormat.format(drawerPullReport.getChargedTips())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableSeparator(table);
		addTableRow(table, "=" + Messages.getString("DrawerPullReportDialog.12"), decimalFormat.format(drawerPullReport.getGrossReceipts())); //$NON-NLS-1$ //$NON-NLS-2$
		document.appendBody(table);

		document.appendBody(new BR());

		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "-" + "CASH RECEIPTS" + " (" + drawerPullReport.getCashReceiptCount() + ")", //$NON-NLS-1$//$NON-NLS-3$
				decimalFormat.format(drawerPullReport.getCashReceiptAmount()));
		addTableRow(table, "-" + "CREDIT CARDS" + " (" + drawerPullReport.getCreditCardReceiptCount() + ")", //$NON-NLS-1$//$NON-NLS-3$//$NON-NLS-4$
				decimalFormat.format(drawerPullReport.getCreditCardReceiptAmount()));
		addTableRow(table, "-" + "DEBIT CARDS" + " (" + drawerPullReport.getDebitCardReceiptCount() + ")", //$NON-NLS-1$//$NON-NLS-3$//$NON-NLS-4$
				decimalFormat.format(drawerPullReport.getDebitCardReceiptAmount()));
		addTableRow(table, "-" + "GIFT RETURNS" + " (" + drawerPullReport.getGiftCertReturnCount() + ")", //$NON-NLS-1$//$NON-NLS-3$//$NON-NLS-4$
				decimalFormat.format(drawerPullReport.getGiftCertReturnAmount()));
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.23"), decimalFormat.format(drawerPullReport.getGiftCertChangeAmount())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.25"), decimalFormat.format(drawerPullReport.getCashBack())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + "REFUND" + " (" + drawerPullReport.getRefundReceiptCount() + ")", decimalFormat.format(drawerPullReport.getRefundAmount())); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
		addTableSeparator(table);
		addTableRow(table, "=" + Messages.getString("DrawerPullReportDialog.29"), decimalFormat.format(drawerPullReport.getReceiptDifferential())); //$NON-NLS-1$ //$NON-NLS-2$
		document.appendBody(table);

		document.appendBody(new BR());

		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.31"), decimalFormat.format(drawerPullReport.getChargedTips())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "-" + Messages.getString("DrawerPullReportDialog.33"), decimalFormat.format(drawerPullReport.getTipsPaid())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableSeparator(table);
		addTableRow(table, "=" + Messages.getString("DrawerPullReportDialog.35"), decimalFormat.format(drawerPullReport.getTipsDifferential())); //$NON-NLS-1$ //$NON-NLS-2$
		document.appendBody(table);

		document.appendBody(new BR());

		createSectionHeader(document, Messages.getString("DrawerPullReportDialog.36")); //$NON-NLS-1$
		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "CASH" + " (" + drawerPullReport.getCashReceiptCount() + ")", decimalFormat.format(drawerPullReport.getCashReceiptAmount())); //$NON-NLS-2$ //$NON-NLS-3$
		//addTableRow(table, "CASH TAX", decimalFormat.format(drawerPullReport.getCashTax()));
		addTableRow(table, "-" + Messages.getString("DrawerPullReportDialog.39"), decimalFormat.format(drawerPullReport.getTipsPaid())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "-" + "PAY OUT" + "       (" + drawerPullReport.getPayOutCount() + ")", decimalFormat.format(drawerPullReport.getPayOutAmount())); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
		addTableRow(table, "-" + Messages.getString("DrawerPullReportDialog.43"), decimalFormat.format(drawerPullReport.getCashBack())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "-" + "REFUND" + " (" + drawerPullReport.getRefundReceiptCount() + ")", decimalFormat.format(drawerPullReport.getRefundAmount())); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
		addTableRow(table, "+" + Messages.getString("DrawerPullReportDialog.47"), decimalFormat.format(terminal.getOpeningBalance())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, "-" + "DRAWER BLEED" + "  (" + drawerPullReport.getDrawerBleedCount() + ")", //$NON-NLS-1$//$NON-NLS-3$//$NON-NLS-4$
				decimalFormat.format(drawerPullReport.getDrawerBleedAmount()));
		addTableSeparator(table);
		addTableRow(table, "=" + Messages.getString("DrawerPullReportDialog.51"), decimalFormat.format(drawerPullReport.getDrawerAccountable())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, ">" + Messages.getString("DrawerPullReportDialog.53"), decimalFormat.format(drawerPullReport.getCashToDeposit())); //$NON-NLS-1$ //$NON-NLS-2$
		addTableSeparator(table);
		if (TerminalConfig.isEnabledMultiCurrency()) {
			CashDrawer cashDrawer = CashDrawerDAO.getInstance().findByTerminal(Application.getInstance().getTerminal());
			if (cashDrawer != null) {
				if (cashDrawer.getCurrencyBalanceList() != null) {
					for (CurrencyBalance currencyBalance : cashDrawer.getCurrencyBalanceList()) {
						addTableRow(table, currencyBalance.getCurrency().getName() + "", "" + decimalFormat.format(currencyBalance.getBalance())); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
		document.appendBody(table);

		createSectionHeader(document, Messages.getString("DrawerPullReportDialog.54")); //$NON-NLS-1$
		createSectionHeader(document, Messages.getString("DrawerPullReportDialog.55")); //$NON-NLS-1$
		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addExceptionTableRow(table, Messages.getString("DrawerPullReportDialog.99"), Messages.getString("DrawerPullReportDialog.100"), //$NON-NLS-1$//$NON-NLS-2$
				Messages.getString("DrawerPullReportDialog.101"), Messages.getString("DrawerPullReportDialog.102")); //$NON-NLS-1$ //$NON-NLS-2$
		addExceptionTableSeparator(table);

		//CONDITIONAL
		Set<DrawerPullVoidTicketEntry> voidTickets = drawerPullReport.getVoidTickets();
		if (voidTickets != null) {
			for (DrawerPullVoidTicketEntry entry : voidTickets) {
				addExceptionTableRow(table, String.valueOf(entry.getCode()), entry.getReason(), entry.getHast(), NumberUtil.formatNumber(entry.getAmount()));
			}
		}
		addExceptionTableSeparator(table);
		document.appendBody(table);

		table = new Table();
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, Messages.getString("DrawerPullReportDialog.105"), decimalFormat.format(drawerPullReport.getTotalVoidWst())); //$NON-NLS-1$
		addTableRow(table, Messages.getString("DrawerPullReportDialog.106"), decimalFormat.format(drawerPullReport.getTotalVoid())); //$NON-NLS-1$

		document.appendBody(table);

		createSectionHeader(document, Messages.getString("DrawerPullReportDialog.107")); //$NON-NLS-1$
		table = new Table();
		document.appendBody(table);
		table.addAttribute("width", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		addTableRow(table, Messages.getString("DrawerPullReportDialog.110"), ""); //$NON-NLS-1$ //$NON-NLS-2$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.112"), String.valueOf(drawerPullReport.getTotalDiscountCount())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.113"), NumberUtil.formatNumber(drawerPullReport.getTotalDiscountAmount())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.114"), NumberUtil.formatNumber(drawerPullReport.getTotalDiscountSales())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.115"), String.valueOf(drawerPullReport.getTotalDiscountGuest())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.116"), String.valueOf(drawerPullReport.getTotalDiscountPartySize())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.117"), String.valueOf(drawerPullReport.getTotalDiscountCheckSize())); //$NON-NLS-1$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.118"), String.valueOf(" ")); //$NON-NLS-1$ //$NON-NLS-2$
		addDiscountTableRow(table, Messages.getString("DrawerPullReportDialog.120"), String.valueOf(" ")); //$NON-NLS-1$ //$NON-NLS-2$

		return document.toString();
	}

	public void setTitle(String title) {
		titlePanel1.setTitle(title);
		super.setTitle(title);
	}

	public JasperPrint showReport() {
		try {
			return PosPrintService.createDrawerPullReport(drawerPullReport, terminal, false);
		} catch (PosException exception) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), exception.getMessage());
		} catch (Exception ex) {
			POSMessageDialog.showError(DrawerPullReportDialog.this, Messages.getString("DrawerPullReportDialog.122") + ex.getMessage()); //$NON-NLS-1$
			PosLog.error(getClass(), ex);
		}
		return null;
	}

	private void doPrintReport() {
		try {
			PosPrintService.printDrawerPullReport(drawerPullReport, terminal, false);
		} catch (PosException exception) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), exception.getMessage());
		} catch (Exception ex) {
			POSMessageDialog.showError(DrawerPullReportDialog.this, Messages.getString("DrawerPullReportDialog.122") + ex.getMessage()); //$NON-NLS-1$
			PosLog.error(getClass(), ex);
		}
	}
}
