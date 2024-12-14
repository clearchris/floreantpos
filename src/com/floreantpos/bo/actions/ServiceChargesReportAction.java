package com.floreantpos.bo.actions;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.ServiceChargeReportView;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

public class ServiceChargesReportAction extends AbstractAction {

    public ServiceChargesReportAction() {
        super(Messages.getString("ServiceChargesReportAction.0"));
    }

    public ServiceChargesReportAction(String name) {
        super(name);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
        JTabbedPane tabbedPane = window.getTabbedPane();

        ServiceChargeReportView reportView = null;
        int index = tabbedPane.indexOfTab(Messages.getString("ServiceChargesReportAction.0"));
        if (index == -1) {
            reportView = new ServiceChargeReportView();
            tabbedPane.addTab(Messages.getString("ServiceChargesReportAction.0"), reportView);
        }
        else {
            reportView = (ServiceChargeReportView) tabbedPane.getComponentAt(index);
        }
        tabbedPane.setSelectedComponent(reportView);
    }
}

