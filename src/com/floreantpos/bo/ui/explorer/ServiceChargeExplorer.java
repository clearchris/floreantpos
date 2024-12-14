package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import com.floreantpos.Messages;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.forms.ServiceChargeForm;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.ServiceCharge;
import com.floreantpos.model.dao.ServiceChargeDAO;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.POSUtil;

public class ServiceChargeExplorer extends TransparentPanel {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a"); //$NON-NLS-1$

    private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
    private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
    private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);

    private JXTable table;
    private ServiceChargeExplorerTableModel tableModel;
    private List<ServiceCharge> serviceCharges;

    public ServiceChargeExplorer() {
        setLayout(new BorderLayout());

        table = new JXTable();
        table.setDefaultRenderer(Object.class, new PosTableRenderer());
        tableModel = new ServiceChargeExplorerTableModel();

        table.setModel(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);
        addButtonPanel();

        refresh();
    }

    private void addButtonPanel() {

        JButton btnDelete = new JButton(POSConstants.DELETE);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = table.getSelectedRow();
                    if (index < 0) {
                        POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(), Messages.getString("ServiceChargeExplorer.2")); //$NON-NLS-1$
                        return;
                    }

                    index = table.convertRowIndexToModel(index);
                    ServiceCharge serviceCharge = tableModel.getRows().get(index);
                    if (POSMessageDialog.showYesNoQuestionDialog(ServiceChargeExplorer.this,
                            Messages.getString("ServiceChargeExplorer.3"), POSConstants.DELETE) != JOptionPane.YES_OPTION) {
                        return;
                    }

                    ServiceChargeDAO.getInstance().delete(serviceCharge);
                    tableModel.deleteItem(index);
                    table.repaint();
                } catch (Exception x) {
                    BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
                }
            }
        });

        JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = table.getSelectedRow();
                    if (index < 0) {
                        POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(), Messages.getString("ServiceChargeExplorer.1")); //$NON-NLS-1$
                        return;
                    }
                    index = table.convertRowIndexToModel(index);
                    ServiceCharge serviceCharge = tableModel.getRows().get(index);

                    ServiceChargeForm editor = new ServiceChargeForm();
                    editor.setBean(serviceCharge);
                    BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
                    dialog.open();
                    if (dialog.isCanceled())
                        return;
                    refresh();
                } catch (Throwable x) {
                    BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
                }
            }
        });

        JButton newButton = new JButton(com.floreantpos.POSConstants.ADD);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ServiceChargeForm editor = new ServiceChargeForm();
                    editor.createNew();
                    BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
                    dialog.open();
                    if (dialog.isCanceled())
                        return;
                    refresh();
                } catch (Throwable x) {
                    BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
                }
            }
        });

        TransparentPanel panel = new TransparentPanel();
        panel.add(btnDelete);
        panel.add(editButton);
        panel.add(newButton);
        add(panel, BorderLayout.SOUTH);
    }

    class ServiceChargeExplorerTableModel extends ListTableModel<ServiceCharge> {
        String[] columnNames = {
                ServiceCharge.PROP_ID.toUpperCase(),
                ServiceCharge.PROP_NAME.toUpperCase(),
                ServiceCharge.PROP_RATE.toUpperCase()
        };

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

        public Object getValueAt(int rowIndex, int columnIndex) {

            ServiceCharge serviceCharge = (ServiceCharge) rows.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return String.valueOf(serviceCharge.getId());
                case 1:
                    return serviceCharge.getName();
                case 2:
                    return Double.valueOf(serviceCharge.getRate());
                default:
                    return null;
            }
        }
    }

    public void initData()  {
        ServiceChargeDAO dao = new ServiceChargeDAO();
        List<ServiceCharge> serviceChargeList = dao.findAll();
        tableModel.setRows(serviceChargeList);
    }

    private void refresh() {
        if (tableModel.getRows() != null) {
            tableModel.getRows().clear();
        }
        initData();
        table.repaint();
    }
}
