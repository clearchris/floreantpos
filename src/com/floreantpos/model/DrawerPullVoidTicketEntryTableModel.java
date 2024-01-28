package com.floreantpos.model;

import com.floreantpos.swing.ListTableModel;

public class DrawerPullVoidTicketEntryTableModel extends ListTableModel {

	public DrawerPullVoidTicketEntryTableModel() {
		setColumnNames(new String[] { "code", "reason", "wast", "qty", "amount" });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		DrawerPullVoidTicketEntry data = (DrawerPullVoidTicketEntry) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return String.valueOf(data.getCode());

			case 1:
				return data.getReason();

			case 2:
				return data.getHast(); // ? "Y" : "N"; //$NON-NLS-1$ //$NON-NLS-2$

			case 3:
				return String.valueOf(data.getQuantity());

			case 4:
				return data.getAmount();
		}

		return null;
	}

}