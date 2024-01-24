package com.floreantpos.ui.views.payment;

import com.floreantpos.Messages;
import com.floreantpos.model.GiftCertificate;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import com.floreantpos.util.NumberUtil;

public class GiftCertificateTransactionDialog extends OkCancelOptionDialog {

	private double charge;

	public GiftCertificateTransactionDialog(GiftCertificate giftCertificate, double dueAmount) {
		super();

		setTitle(Messages.getString("GiftCertificateTransactionDialog.0")); //$NON-NLS-1$
		setTitlePaneText(Messages.getString("GiftCertificateTransactionDialog.1"));

		JPanel panel = getContentPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblGiftCertificateNumberDesc = new JLabel(Messages.getString("GiftCertificateTransactionDialog.5")); //$NON-NLS-1$
		panel.add(lblGiftCertificateNumberDesc, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		JLabel lblGiftCertificateNumber = new JLabel(giftCertificate.getNumber());
		panel.add(lblGiftCertificateNumber, "cell 1 0,growx");

		JLabel lblAvailableValueDesc = new JLabel(Messages.getString("GiftCertificateTransactionDialog.8")); //$NON-NLS-1$
		panel.add(lblAvailableValueDesc, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		JLabel lblAvailableValue = new JLabel(NumberUtil.formatNumber(giftCertificate.getCurrentBalance()));
		panel.add(lblAvailableValue, "cell 1 1,growx");

		JLabel lblDueAmountDesc= new JLabel(Messages.getString("GiftCertificateTransactionDialog.2")); //$NON-NLS-1$
		panel.add(lblDueAmountDesc, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		JLabel lblDueAmount = new JLabel(NumberUtil.formatNumber(dueAmount));
		panel.add(lblDueAmount, "cell 1 2,growx");

		JLabel lblRemainingValueDesc = new JLabel(Messages.getString("GiftCertificateTransactionDialog.3")); //$NON-NLS-1$
		panel.add(lblRemainingValueDesc, "cell 0 3,alignx trailing"); //$NON-NLS-1$

		double remainingValue = giftCertificate.getCurrentBalance() - dueAmount;
		JLabel lblRemainingValue = new JLabel(NumberUtil.formatNumber(remainingValue));
		panel.add(lblRemainingValue, "cell 1 3,growx");

		double additionalPayment = dueAmount - giftCertificate.getCurrentBalance();
		if (additionalPayment > 0) {
			JLabel lblAdditionalPaymentDesc = new JLabel(Messages.getString("GiftCertificateTransactionDialog.6")); //$NON-NLS-1$
			panel.add(lblAdditionalPaymentDesc, "cell 0 4,alignx trailing"); //$NON-NLS-1$

			JLabel lblAdditionalPayment = new JLabel(NumberUtil.formatNumber(additionalPayment));
			panel.add(lblAdditionalPayment, "cell 1 4,growx");
		}

		if (remainingValue <= 0){
			JLabel lblRetainCard = new JLabel(Messages.getString("GiftCertificateTransactionDialog.4")); //$NON-NLS-1$
			panel.add(lblRetainCard, "cell 0 5, span 2, alignx center"); //$NON-NLS-1$
		}
		if (giftCertificate.getCurrentBalance() < dueAmount){
			JLabel lblAdditionalPayment = new JLabel(Messages.getString("GiftCertificateTransactionDialog.9")); //$NON-NLS-1$
			panel.add(lblAdditionalPayment, "cell 0 6, span 2, alignx center"); //$NON-NLS-1$
		}

		if (giftCertificate.getCurrentBalance() > dueAmount) charge = dueAmount;
		else charge = giftCertificate.getCurrentBalance();
	}

	@Override
	public void doOk() {

		setCanceled(false);
		dispose();
	}

	public double getCharge() {
		return charge;
	}
}
