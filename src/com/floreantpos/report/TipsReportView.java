package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.dao.GratuityDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.PosLog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;

public class TipsReportView extends TransparentPanel {
    private JButton btnGo = new JButton(POSConstants.GO);
    private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
    private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
    private JPanel reportContainer;

    public TipsReportView() {
        super(new BorderLayout());
        JPanel topPanel = new JPanel(new MigLayout());
        topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow");
        topPanel.add(fromDatePicker);
        topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":"), "grow");
        topPanel.add(toDatePicker);
        topPanel.add(btnGo, "skip 1, al right");
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, 10,10,10));
        centerPanel.add(new JSeparator(), BorderLayout.NORTH);

        reportContainer = new JPanel(new BorderLayout());
            centerPanel.add(reportContainer);

        add(centerPanel);

        btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewReport();
                } catch (Exception e1) {
                    POSMessageDialog.showError(TipsReportView.this, POSConstants.ERROR_MESSAGE, e1);
                }
            }

        });
    }

private void viewReport() {
        Date fromDate = DateUtil.startOfDay(fromDatePicker.getDate());
        Date toDate = DateUtil.endOfDay(toDatePicker.getDate());

        if (fromDate.after(toDate)) {
            POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
            return;
        }

        GratuityDAO dao = new GratuityDAO();
        List<TipsReportData> findPayroll = dao.createTipsReport(fromDate, toDate, null);

        try {
            JasperReport report = ReportUtil.getReport("TipsReport"); //$NON-NLS-1$

            HashMap properties = new HashMap();
            ReportUtil.populateRestaurantProperties(properties);
            properties.put("fromDate", fromDate);
            properties.put("toDate", toDate);
            properties.put("reportDate", new Date());

            TipsReportModel reportModel = new TipsReportModel();
            reportModel.setRows(findPayroll);
            JasperPrint print = JasperFillManager.fillReport(report, properties, new JRTableModelDataSource(reportModel));

            JRViewer viewer = new JRViewer(print);
            reportContainer.removeAll();
            reportContainer.add(viewer);
            reportContainer.revalidate();
        } catch (JRException e) {
            PosLog.error(getClass(), e);
        }
    }
}
