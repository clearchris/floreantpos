package com.floreantpos.ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketListViewTableCellRenderer extends PosTableRenderer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy h:mm");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel jLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Date) {
            setText(dateFormat.format((Date) value));
        }
        return jLabel;
    }
 }
