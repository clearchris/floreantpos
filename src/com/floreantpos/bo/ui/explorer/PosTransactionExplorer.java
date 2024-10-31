package com.floreantpos.bo.ui.explorer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.floreantpos.Messages;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class PosTransactionExplorer extends TransparentPanel {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:mm a"); //$NON-NLS-1$

    private JXDatePicker fromDatePicker = new JXDatePicker(DateUtil.startOfDay(new Date()));
    private JXDatePicker toDatePicker = new JXDatePicker(DateUtil.endOfDay(new Date()));
    private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
    private  JButton btnToday = new JButton(Messages.getString("PosTransactionExplorer.10"));
    private JTextField tfTicketId = new JTextField(10);
    private JButton btnGoTicketId = new JButton(com.floreantpos.POSConstants.GO);
    private JTextField tfTerminalId = new JTextField(10);
    private TransparentPanel totalsPanel = new TransparentPanel();
    private JXTable table;
    private PosTransactionExplorerTableModel tableModel;
    private List<PosTransaction> transactions;
    private Date fromDate = null;
    private Date  toDate = null;
    private boolean byTicketId = false;

    public PosTransactionExplorer() {
        setLayout(new BorderLayout());

        table = new JXTable();
        table.setDefaultRenderer(Object.class, new PosTableRenderer());
        tableModel = new PosTransactionExplorerTableModel();

        table.setModel(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addTopPanel();
        add(new JScrollPane(table), BorderLayout.CENTER);
        setDateRange(fromDatePicker.getDate(), toDatePicker.getDate());
        addTotalsPanel();
    }

    private void addTopPanel() {
        JPanel topPanel = new JPanel(new MigLayout());

        btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setDateRange(DateUtil.startOfDay(fromDatePicker.getDate()), DateUtil.endOfDay(toDatePicker.getDate()));
                } catch (Exception e1) {
                    BOMessageDialog.showError(PosTransactionExplorer.this, POSConstants.ERROR_MESSAGE, e1);
                }
            }
        });

        btnToday.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setDateRange(DateUtil.startOfDay(new Date()), DateUtil.endOfDay(new Date()));
                } catch (Exception e1) {
                    BOMessageDialog.showError(PosTransactionExplorer.this, POSConstants.ERROR_MESSAGE, e1);
                }
            }
        });

        btnGoTicketId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setTicketId(tfTicketId.getText());
                } catch (Exception e1) {
                    BOMessageDialog.showError(PosTransactionExplorer.this, POSConstants.ERROR_MESSAGE, e1);
                }
            }
        });

        topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM), "grow"); //$NON-NLS-1$
        topPanel.add(fromDatePicker, "gapright 10"); //$NON-NLS-1$
        topPanel.add(new JLabel(com.floreantpos.POSConstants.TO), "grow"); //$NON-NLS-1$
        topPanel.add(toDatePicker);
        topPanel.add(new JLabel(Messages.getString("PosTransactionExplorer.11")), "grow"); //$NON-NLS-1$
        topPanel.add(tfTerminalId, "gapright 10"); //$NON-NLS-1$
        topPanel.add(btnGo, "width 60!"); //$NON-NLS-1$
        topPanel.add(btnToday, "width 100!"); //$NON-NLS-1$
        topPanel.add(new JLabel(Messages.getString("PosTransactionExplorer.9")), "grow"); //$NON-NLS-1$
        topPanel.add(tfTicketId, "gapright 10"); //$NON-NLS-1$
        topPanel.add(btnGoTicketId, "width 60!"); //$NON-NLS-1$
        add(topPanel, BorderLayout.NORTH);
    }

    public void setDateRange(Date fromDate, Date toDate) {
        fromDatePicker.setDate(fromDate);
        toDatePicker.setDate(toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
        byTicketId = false;
        refresh();
    }

    public void setDateRangeAndTerminal(Date fromDate, Date toDate, Terminal terminal) {
        tfTerminalId.setText(terminal.getId().toString());
        setDateRange(fromDate, toDate);
    }

    public void setTicketId(String ticketId) {
        tfTicketId.setText(ticketId);
        byTicketId = true;
        refresh();
    }

    public void setTicketId(Integer ticketId) {
        setTicketId(ticketId!=null?ticketId.toString():"");
    }

    public void addTotalsPanel() {
        refreshTotalsPanel();
        add(totalsPanel, BorderLayout.SOUTH);
    }

    public void refreshTotalsPanel() {
        totalsPanel.removeAll();
        if(transactions == null) return;
        if(transactions.isEmpty()) return;

        Map<String, Double> subTransactionTotals = new HashMap<>();

        for (PosTransaction transaction : transactions) {
            String subTransactionType = transaction.getTransactionType();
            double amount = transaction.getAmount();

            subTransactionTotals.put(subTransactionType,
                    subTransactionTotals.getOrDefault(subTransactionType, 0.0) + amount);
        }

        Map<String, Map<String, Double>> transactionTotals = new HashMap<>();

        for (PosTransaction transaction : transactions) {
            String transactionType = transaction.getTransactionType();
            String subTransactionType = transaction.getPaymentType();
            double amount = transaction.getAmount();

            transactionTotals
                    .computeIfAbsent(transactionType, k -> new HashMap<>())
                    .merge(subTransactionType, amount, Double::sum);
        }

        String[] columnNames = {
            Messages.getString("PosTransactionExplorer.14"), // Transaction Type
            Messages.getString("PosTransactionExplorer.15"), // Transaction Sub Type
            Messages.getString("PosTransactionExplorer.16")  // Total Amount
        };
        List<Object[]> data = new ArrayList<>();

        for (Map.Entry<String, Map<String, Double>> entry : transactionTotals.entrySet()) {
            String transactionType = entry.getKey();
            for (Map.Entry<String, Double> subEntry : entry.getValue().entrySet()) {
                String subTransactionType = subEntry.getKey();
                Double totalAmount = subEntry.getValue();
                data.add(new Object[] { transactionType, subTransactionType, NumberUtil.formatNumber(totalAmount) });
            }
        }

        Object[][] dataArray = data.toArray(new Object[0][]);
        JTable totalsTable = new JTable(dataArray, columnNames);
        totalsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = totalsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(200); // Adjust the width as needed
        }
        totalsTable.setRowHeight(25);
        totalsTable.setDefaultRenderer(Object.class, new PosTableRenderer());

        //totalsPanel.add(new JScrollPane(totalsTable));
        //totalsPanel.add(totalsTable, BorderLayout.NORTH);

        // Add the table directly to the panel
        totalsPanel.setLayout(new BorderLayout());
        totalsPanel.add(totalsTable.getTableHeader(), BorderLayout.NORTH);
        totalsPanel.add(totalsTable, BorderLayout.CENTER);

        totalsPanel.revalidate();
    }

    class PosTransactionExplorerTableModel extends ListTableModel<PosTransaction> {
        String[] columnNames = {
                Messages.getString("PosTransactionExplorer.0"), // ID
                Messages.getString("PosTransactionExplorer.9"), // Ticket ID
                Messages.getString("PosTransactionExplorer.1"), //  Amount
                Messages.getString("PosTransactionExplorer.2"), // Date
                Messages.getString("PosTransactionExplorer.3"), // User
                Messages.getString("PosTransactionExplorer.4"), // Payment Type
                Messages.getString("PosTransactionExplorer.5"), // Transaction Type
                Messages.getString("PosTransactionExplorer.6"), //  Payment Sub Type
                Messages.getString("PosTransactionExplorer.7"), //  Card Transaction ID
                Messages.getString("PosTransactionExplorer.8"), // Terminal ID
        };

        @Override
        public String[] getColumnNames() {
            return columnNames;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
    private String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            PosTransaction transaction = (PosTransaction) rows.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return String.valueOf(transaction.getId());
                case 1:
                    return transaction.getTicketId();
                case 2:
                    return Double.valueOf(transaction.getAmount());
                case 3:
                    return dateFormat.format(transaction.getTransactionTime());
                case 4:
                    return (transaction.getUser() == null) ? "" : "" + transaction.getUser();
                case 5:
                    return transaction.getRef();
                case 6:
                    return transaction.getTransactionType();
                case 7:
                    return transaction.getPaymentType();
                case 8:
                    return transaction.getCardTransactionId();
                case 9:
                    return transaction.getTerminal();
            }
            return null;
        }
    }

    private void refresh() {
        if (tableModel.getRows() != null) {
            tableModel.getRows().clear();
        }

        if (byTicketId) {
            String ticketId = tfTicketId.getText();
            TicketDAO dao = new TicketDAO();
            Ticket ticket = dao.get(Integer.valueOf(ticketId));
            this.transactions = new ArrayList<>(ticket.getTransactions());
        }
        else {
            PosTransactionDAO dao = new PosTransactionDAO();

            Terminal terminal;
            if(tfTerminalId.getText() != null && !tfTerminalId.getText().isEmpty()) {
                try {
                    TerminalDAO terminalDAO = new TerminalDAO();
                    terminal = terminalDAO.get(Integer.valueOf(tfTerminalId.getText()));
                    if (terminal == null) {
                        POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), Messages.getString("PosTransactionExplorer.50"));
                        return;
                    }
                    this.transactions = (List<PosTransaction>) dao.findTransactions(terminal, PosTransaction.class, fromDate, toDate);
                } catch (Exception e) {
                    POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), Messages.getString("PosTransactionExplorer.50"));
                    return;
                }
            }
            else {
                this.transactions = (List<PosTransaction>) dao.findTransactions(PosTransaction.class, fromDate, toDate);
            }
        }

        tableModel.setRows(transactions);
        table.repaint();
        refreshTotalsPanel();
    }
}