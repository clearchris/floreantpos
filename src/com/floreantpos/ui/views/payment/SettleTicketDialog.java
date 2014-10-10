package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TicketDetailView;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.POSUtil;

public class SettleTicketDialog extends POSDialog implements CardInputListener {
	public static final String LOYALTY_DISCOUNT_PERCENTAGE = "loyalty_discount_percentage";
	public static final String LOYALTY_POINT = "loyalty_point";
	public static final String LOYALTY_COUPON = "loyalty_coupon";
	public static final String LOYALTY_DISCOUNT = "loyalty_discount";
	public static final String LOYALTY_ID = "loyalty_id";

	public final static String VIEW_NAME = "PAYMENT_VIEW";

	private String previousViewName = SwitchboardView.VIEW_NAME;

	private com.floreantpos.swing.TransparentPanel leftPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	private com.floreantpos.swing.TransparentPanel rightPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());

	private TicketDetailView ticketDetailView;
	private PaymentView paymentView;
	//protected List<Ticket> ticketsToSettle;

	private Ticket ticket;

	private double tenderAmount;

	private String cardName;

	public SettleTicketDialog() {
		super(Application.getPosWindow(), true);
		setTitle("Settle ticket");

		getContentPane().setLayout(new BorderLayout(5, 5));

		ticketDetailView = new TicketDetailView();
		paymentView = new PaymentView(this);

		leftPanel.add(ticketDetailView);
		rightPanel.add(paymentView);

		getContentPane().add(leftPanel, BorderLayout.CENTER);
		getContentPane().add(rightPanel, BorderLayout.EAST);
	}

	//	public void setCurrentTicket(Ticket currentTicket) {
	//		ticketsToSettle = new ArrayList<Ticket>();
	//		ticketsToSettle.add(currentTicket);
	//
	//		ticketDetailView.setTickets(getTicketsToSettle());
	//		paymentView.updateView();
	//	}

	private void updateModel() {
		if (ticket == null) {
			return;
		}

		ticket.calculatePrice();
	}

	public void doApplyCoupon() {// GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			if (ticket == null)
				return;

			if (ticket.getCouponAndDiscounts() != null && ticket.getCouponAndDiscounts().size() > 0) {
				POSMessageDialog.showError(com.floreantpos.POSConstants.DISCOUNT_COUPON_LIMIT_);
				return;
			}

			CouponAndDiscountDialog dialog = new CouponAndDiscountDialog();
			dialog.setTicket(ticket);
			dialog.initData();
			dialog.open();
			if (!dialog.isCanceled()) {
				TicketCouponAndDiscount coupon = dialog.getSelectedCoupon();
				ticket.addTocouponAndDiscounts(coupon);

				updateModel();

				OrderController.saveOrder(ticket);
				ticketDetailView.updateView();
				paymentView.updateView();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnApplyCoupondoApplyCoupon

	public void doTaxExempt(boolean taxExempt) {// GEN-FIRST:event_doTaxExempt
		if (ticket == null)
			return;

		boolean setTaxExempt = taxExempt;
		if (setTaxExempt) {
			int option = JOptionPane.showOptionDialog(this, POSConstants.CONFIRM_SET_TAX_EXEMPT, POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			ticket.setTaxExempt(true);
			ticket.calculatePrice();
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
		else {
			ticket.setTaxExempt(false);
			ticket.calculatePrice();
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}

		ticketDetailView.updateView();
		paymentView.updateView();
	}// GEN-LAST:event_doTaxExempt

	public void doSetGratuity() {
		if (ticket == null)
			return;

		GratuityInputDialog d = new GratuityInputDialog();
		d.setSize(300, 500);
		d.setResizable(false);
		d.open();

		if (d.isCanceled()) {
			return;
		}

		double gratuityAmount = d.getGratuityAmount();
		Gratuity gratuity = new Gratuity();
		gratuity.setAmount(gratuityAmount);

		ticket.setGratuity(gratuity);
		ticket.calculatePrice();
		OrderController.saveOrder(ticket);

		ticketDetailView.updateView();
		paymentView.updateView();
	}

	public void doViewDiscounts() {// GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {

			if (ticket == null)
				return;

			DiscountListDialog dialog = new DiscountListDialog(Arrays.asList(ticket));
			dialog.open();

			if (!dialog.isCanceled() && dialog.isModified()) {
				updateModel();

				TicketDAO.getInstance().saveOrUpdate(ticket);

				ticketDetailView.updateView();
				paymentView.updateView();
			}

		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnViewDiscountsdoViewDiscounts

	public void doSettle() {
		try {
			if (ticket == null)
				return;

			tenderAmount = paymentView.getTenderedAmount();

			if (ticket.getType() == TicketType.BAR_TAB) {
				doSettleBarTabTicket(ticket);
				return;
			}

			PaymentTypeSelectionDialog dialog = new PaymentTypeSelectionDialog();
			dialog.setResizable(false);
			dialog.pack();
			dialog.open();
			if (dialog.isCanceled()) {
				return;
			}

			PaymentType paymentType = dialog.getSelectedPaymentType();
			cardName = paymentType.getDisplayString();

			switch (paymentType) {
				case CASH:
					ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
					confirmPayDialog.setAmount(tenderAmount);
					confirmPayDialog.open();

					if (confirmPayDialog.isCanceled()) {
						return;
					}

					CashTransaction transaction = new CashTransaction();
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					setTransactionAmounts(transaction);
					
					settleTicket(transaction);
					break;

				case CREDIT_VISA:
				case CREDIT_MASTER_CARD:
				case CREDIT_AMEX:
				case CREDIT_DISCOVERY:
					payUsingCard(cardName, tenderAmount);
					break;

				case DEBIT_VISA:
				case DEBIT_MASTER_CARD:
					payUsingCard(cardName, tenderAmount);
					break;

				default:
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doSettleBarTabTicket(Ticket ticket) {
		ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
		confirmPayDialog.setAmount(tenderAmount);
		confirmPayDialog.open();

		if (confirmPayDialog.isCanceled()) {
			return;
		}

		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);
		waitDialog.setVisible(true);

		try {
			String transactionId = ticket.getProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID);
			double advanceAmount = Double.parseDouble(ticket.getProperty(Ticket.PROPERTY_ADVANCE_PAYMENT));
			
			CreditCardTransaction transaction = new CreditCardTransaction();
			transaction.setTicket(ticket);
			transaction.setCardType(ticket.getProperty(Ticket.PROPERTY_CARD_NAME));
			transaction.setCaptured(false);
			transaction.setCardMerchantGateway(MerchantGateway.AUTHORIZE_NET.name());
			
			CardReader cardReader = CardReader.valueOf(ticket.getProperty(Ticket.PROPERTY_CARD_READER));
			
			if(cardReader == CardReader.SWIPE) {
				transaction.setCardEntryType(CardReader.SWIPE.name());
				transaction.setCardTrack(ticket.getProperty(Ticket.PROPERTY_CARD_TRACKS));
				transaction.setCardTransactionId(transactionId);
			}
			else if(cardReader == CardReader.MANUAL) {
				transaction.setCardEntryType(CardReader.MANUAL.name());
				transaction.setCardTransactionId(transactionId);
				transaction.setCardNumber(ticket.getProperty(Ticket.PROPERTY_CARD_NUMBER));
				transaction.setCardExpiryMonth(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_MONTH));
				transaction.setCardExpiryYear(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_YEAR));
			}
			else {
				transaction.setCardEntryType(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setAuthorizationCode(ticket.getProperty(Ticket.PROPERTY_CARD_AUTH_CODE));
			}
			
			setTransactionAmounts(transaction);
			
			if (tenderAmount > advanceAmount) {
				AuthorizeDotNetProcessor.voidAmount(transactionId, advanceAmount);
			}
			
			AuthorizeDotNetProcessor.authorizeAmount(transaction);

			settleTicket(transaction);
			
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		} finally {
			waitDialog.setVisible(false);
		}
	}

	public void settleTicket(PosTransaction transaction) {
		try {
			final double dueAmount = ticket.getDueAmount();
			
			confirmLoyaltyDiscount(ticket);

			PosTransactionService transactionService = PosTransactionService.getInstance();
			transactionService.settleTicket(ticket, transaction);

			//FIXME
			printTicket(ticket, transaction);

			showTransactionCompleteMsg(dueAmount, transaction.getTenderAmount(), ticket, transaction);

			if (ticket.getDueAmount() > 0.0) {
				int option = JOptionPane.showConfirmDialog(Application.getPosWindow(), POSConstants.CONFIRM_PARTIAL_PAYMENT, POSConstants.MDS_POS,
						JOptionPane.YES_NO_OPTION);
				
				if (option != JOptionPane.YES_OPTION) {
					setCanceled(false);
					dispose();
				}
				
				setTicket(ticket);
			}
			else {
				setCanceled(false);
				dispose();
			}
		} catch (UnknownHostException e) {
			POSMessageDialog.showError("My Kala discount server connection error");
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showTransactionCompleteMsg(final double dueAmount, final double tenderedAmount, Ticket ticket, PosTransaction transaction) {
		TransactionCompletionDialog dialog = TransactionCompletionDialog.getInstance();
		dialog.setTickets(Arrays.asList(ticket));
		dialog.setTenderedAmount(tenderedAmount);
		dialog.setTotalAmount(dueAmount);
		dialog.setPaidAmount(transaction.getAmount());
		dialog.setDueAmount(ticket.getDueAmount());
		
		if(tenderedAmount > transaction.getAmount()) {
			dialog.setChangeAmount(tenderedAmount - transaction.getAmount());
		}
		else {
			dialog.setChangeAmount(0);
		}
		
		// dialog.setGratuityAmount(gratuityAmount);
		dialog.updateView();
		dialog.pack();
		dialog.open();
	}

	public void confirmLoyaltyDiscount(Ticket ticket) throws IOException, MalformedURLException {
		try {
			if (ticket.hasProperty(LOYALTY_ID)) {
				String url = buildLoyaltyApiURL(ticket, ticket.getProperty(LOYALTY_ID));
				url += "&paid=1";

				IOUtils.toString(new URL(url).openStream());
			}
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

	private void printTicket(Ticket ticket, PosTransaction transaction) {
		try {
			if (ticket.needsKitchenPrint()) {
				JReportPrintService.printTicketToKitchen(ticket);
			}

			JReportPrintService.printTicket(ticket, transaction);
		} catch (Exception ee) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.PRINT_ERROR, ee);
		}
	}

	private void payUsingCard(String cardName, final double tenderedAmount) throws Exception {
		if (!CardConfig.getMerchantGateway().isCardTypeSupported(cardName)) {
			POSMessageDialog.showError("<html>Card <b>" + cardName + "</b> not supported.</html>");
			return;
		}

		CardReader cardReader = CardConfig.getCardReader();
		switch (cardReader) {
			case SWIPE:
				SwipeCardDialog swipeCardDialog = new SwipeCardDialog(this);
				swipeCardDialog.pack();
				swipeCardDialog.open();
				break;

			case MANUAL:
				ManualCardEntryDialog dialog = new ManualCardEntryDialog(this);
				dialog.pack();
				dialog.open();
				break;

			case EXTERNAL_TERMINAL:
				AuthorizationCodeDialog authorizationCodeDialog = new AuthorizationCodeDialog(this);
				authorizationCodeDialog.pack();
				authorizationCodeDialog.open();
				break;

			default:
				break;
		}

	}

	public void updatePaymentView() {
		paymentView.updateView();
	}

	public String getPreviousViewName() {
		return previousViewName;
	}

	public void setPreviousViewName(String previousViewName) {
		this.previousViewName = previousViewName;
	}

	public TicketDetailView getTicketDetailView() {
		return ticketDetailView;
	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void cardInputted(CardInputter inputter) {
		//authorize only, do not capture
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);

		try {
			waitDialog.setVisible(true);

			if (inputter instanceof SwipeCardDialog) {
				SwipeCardDialog swipeCardDialog = (SwipeCardDialog) inputter;
				String cardString = swipeCardDialog.getCardString();

				if (StringUtils.isEmpty(cardString) || cardString.length() < 16) {
					throw new RuntimeException("Invalid card string");
				}

				ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
				confirmPayDialog.setAmount(tenderAmount);
				confirmPayDialog.open();

				if (confirmPayDialog.isCanceled()) {
					return;
				}

				if (CardConfig.getMerchantGateway() == MerchantGateway.AUTHORIZE_NET) {
					CreditCardTransaction transaction = new CreditCardTransaction();
					transaction.setTicket(ticket);
					transaction.setCardType(cardName);
					transaction.setCardTrack(cardString);
					transaction.setCaptured(false);
					transaction.setCardMerchantGateway(MerchantGateway.AUTHORIZE_NET.name());
					transaction.setCardEntryType(CardReader.SWIPE.name());
					setTransactionAmounts(transaction);
					
					AuthorizeDotNetProcessor.authorizeAmount(transaction);
					
					settleTicket(transaction);
				}
			}
			else if (inputter instanceof ManualCardEntryDialog) {
				ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;
				
				CreditCardTransaction transaction = new CreditCardTransaction();
				transaction.setTicket(ticket);
				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(MerchantGateway.AUTHORIZE_NET.name());
				transaction.setCardEntryType(CardReader.MANUAL.name());
				transaction.setCardNumber(mDialog.getCardNumber());
				transaction.setCardExpiryMonth(mDialog.getExpMonth());
				transaction.setCardExpiryYear(mDialog.getExpYear());
				setTransactionAmounts(transaction);
				
				AuthorizeDotNetProcessor.authorizeAmount(transaction);
				
				settleTicket(transaction);
			}
			else if (inputter instanceof AuthorizationCodeDialog) {
				AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
				String authorizationCode = authDialog.getAuthorizationCode();
				if (StringUtils.isEmpty(authorizationCode)) {
					throw new PosException("Invalid authorization code");
				}
				
				CreditCardTransaction transaction = new CreditCardTransaction();
				transaction.setTicket(ticket);
				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardEntryType(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setAuthorizationCode(authorizationCode);
				setTransactionAmounts(transaction);
				
				settleTicket(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
	}

	private void setTransactionAmounts(PosTransaction transaction) {
		transaction.setTenderAmount(tenderAmount);
		
		if(tenderAmount >= ticket.getDueAmount()) {
			transaction.setAmount(ticket.getDueAmount());
		}
		else {
			transaction.setAmount(tenderAmount);
		}
	}

	public boolean hasMyKalaId() {
		if (ticket == null)
			return false;

		if (ticket.hasProperty(LOYALTY_ID)) {
			return true;
		}

		return false;
	}

	public void submitMyKalaDiscount() {
		if (ticket.hasProperty(LOYALTY_ID)) {
			POSMessageDialog.showError("Loyalty discount already added.");
			return;
		}

		try {
			String loyaltyid = JOptionPane.showInputDialog("Enter loyalty id:");

			if (StringUtils.isEmpty(loyaltyid)) {
				return;
			}

			ticket.addProperty(LOYALTY_ID, loyaltyid);

			String transactionURL = buildLoyaltyApiURL(ticket, loyaltyid);

			String string = IOUtils.toString(new URL(transactionURL).openStream());

			JsonReader reader = Json.createReader(new StringReader(string));
			JsonObject object = reader.readObject();
			JsonArray jsonArray = (JsonArray) object.get("discounts");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = (JsonObject) jsonArray.get(i);
				addCoupon(ticket, jsonObject);
			}

			updateModel();

			OrderController.saveOrder(ticket);

			POSMessageDialog.showMessage("Congrations! you have discounts from Kala Loyalty Check discounts list for more.");

			ticketDetailView.updateView();
			paymentView.updateView();

			//			if (string.contains("\"success\":false")) {
			//				POSMessageDialog.showError("Coupon already used.");
			//			}
		} catch (Exception e) {
			POSMessageDialog.showError("Error setting My Kala discount.", e);
		}
	}

	public String buildLoyaltyApiURL(Ticket ticket, String loyaltyid) {
		Restaurant restaurant = Application.getInstance().getRestaurant();

		String transactionURL = "http://cloud.floreantpos.org/tri2/kala_api?";
		transactionURL += "kala_id=" + loyaltyid;
		transactionURL += "&store_id=" + restaurant.getUniqueId();
		transactionURL += "&store_name=" + POSUtil.encodeURLString(restaurant.getName());
		transactionURL += "&store_zip=" + restaurant.getZipCode();
		transactionURL += "&terminal=" + ticket.getTerminal().getId();
		transactionURL += "&server=" + POSUtil.encodeURLString(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
		transactionURL += "&" + ticket.toURLForm();

		return transactionURL;
	}

	private void addCoupon(Ticket ticket, JsonObject jsonObject) {
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			JsonNumber jsonNumber = jsonObject.getJsonNumber(key);
			double doubleValue = jsonNumber.doubleValue();

			TicketCouponAndDiscount coupon = new TicketCouponAndDiscount();
			coupon.setName(key);
			coupon.setType(CouponAndDiscount.FIXED_PER_ORDER);
			coupon.setValue(doubleValue);

			ticket.addTocouponAndDiscounts(coupon);
		}
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;

		ticketDetailView.setTickets(Arrays.asList(ticket));
		paymentView.updateView();
	}
}