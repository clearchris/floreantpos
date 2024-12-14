package com.floreantpos.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.swing.ListTableModel;

public class ServiceChargeReportModel extends ListTableModel {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a EEE");
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ServiceChargeReportModel() {
        super(new String[]{"ticketId", "userId", "userName", "date", "amount"});
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ServiceChargeReportData data = (ServiceChargeReportData) rows.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return data.getTicketId();
            case 1:
                return data.getUserId();
            case 2:
                return data.getUserName();
            case 3:
                return dateFormat.format(data.getDate());
            case 4:
                return data.getAmount();
        }
        return null;
    }
}
