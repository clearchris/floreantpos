package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.GiftCertificate;
import com.floreantpos.model.dao.GiftCertificateDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.POSUtil;

public class GiftCertificateExplorer extends TransparentPanel {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a"); //$NON-NLS-1$

	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);

	private JXTable table;
	private GiftCertificateExplorerTableModel tableModel;
	private List<GiftCertificate> giftCertificates;

	public GiftCertificateExplorer() {
		setLayout(new BorderLayout());

		table = new JXTable();
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		tableModel = new GiftCertificateExplorerTableModel();

		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowHeight(25);

		addTopPanel();
		add(new JScrollPane(table), BorderLayout.CENTER);
		addButtonPanel();

		refresh();
	}

	private void addTopPanel() {
		JPanel topPanel = new JPanel(new MigLayout());

		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (Exception e1) {
					BOMessageDialog.showError(GiftCertificateExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});

		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM), "grow"); //$NON-NLS-1$
		topPanel.add(fromDatePicker, "gapright 10"); //$NON-NLS-1$
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO), "grow"); //$NON-NLS-1$
		topPanel.add(toDatePicker);
		topPanel.add(btnGo, "width 60!"); //$NON-NLS-1$
		add(topPanel, BorderLayout.NORTH);
	}

	private void addButtonPanel() {
		JButton btnVoid = new JButton(POSConstants.DELETE);
		btnVoid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0) {
						POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(), POSConstants.SELECT_ONE_TICKET_TO_VOID);
						return;
					}

					index = table.convertRowIndexToModel(index);
					List<GiftCertificate> giftCertificates = new ArrayList<GiftCertificate>();
					GiftCertificate giftCertificate = tableModel.getRows().get(index);
					giftCertificates.add(giftCertificate);

					if (POSMessageDialog.showYesNoQuestionDialog(GiftCertificateExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != JOptionPane.YES_OPTION) {
						return;
					}

					//TODO GiftCertificateDAO.getInstance().deleteGiftCertificates(giftCertificates);
					// tableModel.deleteItem(index);
					table.repaint();
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton btnVoidAll = new JButton(POSConstants.DELETE_ALL);
		btnVoidAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<GiftCertificate> giftCertificates = tableModel.getRows();

					if (giftCertificates.isEmpty()) {
						return;
					}

					if (POSMessageDialog.showYesNoQuestionDialog(GiftCertificateExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE_ALL) != JOptionPane.YES_OPTION) {
						return;
					}

					//TODO GiftCertificateDAO.getInstance().deleteGiftCertificates(giftCertificates);
					refresh();

				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(btnVoid);
		panel.add(btnVoidAll);
		add(panel, BorderLayout.SOUTH);
	}

	class GiftCertificateExplorerTableModel extends ListTableModel<GiftCertificate> {
		String[] columnNames = { POSConstants.ID,
				POSConstants.GIFT_CERTIFICATE.toUpperCase(),
				POSConstants.PIN.toUpperCase(),
				POSConstants.FACE_VALUE.toUpperCase(),
				POSConstants.CURRENT_BALANCE.toUpperCase(),
				POSConstants.CREATE_DATE.toUpperCase(),
				POSConstants.SOLD_DATE.toUpperCase(),
				POSConstants.TRANSACTION.toUpperCase(),
				POSConstants.TICKET.toUpperCase(),
				POSConstants.ITEM.toUpperCase(),
				POSConstants.EXPIRY_DATE.toUpperCase(),
				POSConstants.CREATED_BY.toUpperCase() };

		@Override
		public String[] getColumnNames() {
			return columnNames;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		// controls table sorting algo
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnNames.length <= columnIndex || columnIndex < 0 || tableModel.getRows() == null || tableModel.getRowCount() < 1) {
				return Object.class;
			}
			if (getValueAt(0, columnIndex) == null ) return Object.class;
			return getValueAt(0, columnIndex).getClass();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {

			GiftCertificate giftCertificate = (GiftCertificate) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(giftCertificate.getId());
				case 1:
					return giftCertificate.getNumber();
				case 2:
					if (giftCertificate.getPin() != null) {
						return giftCertificate.getPin();
					}
					return "";
				case 3:
					return Double.valueOf(giftCertificate.getFaceValue());
				case 4:
					return Double.valueOf(giftCertificate.getCurrentBalance());
				case 5:
					return formatDate(giftCertificate.getCreateDate());
				case 6:
					return formatDate(giftCertificate.getSoldDate());
				case 7:
					return giftCertificate.getSoldTransaction();
				case 8:
					return giftCertificate.getTicketId();
				case 9:
					return giftCertificate.getTicketItemId();
				case 10:
					return formatDate(giftCertificate.getExpiryDate());
				case 11:
					return (giftCertificate.getUser()==null)? "": ""+giftCertificate.getUser();
			}
			return null;
		}
	}

	private String formatDate(Date date){
		if(date == null) return "";
		return dateFormat.format(date);
	}

	private void refresh() {
		if (tableModel.getRows() != null) {
			tableModel.getRows().clear();
		}

		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();

		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);

		GiftCertificateDAO dao = new GiftCertificateDAO();
		giftCertificates = dao.getGiftCertificatesByDate(fromDate, toDate);
		tableModel.setRows(giftCertificates);
		table.repaint();
	}
}
