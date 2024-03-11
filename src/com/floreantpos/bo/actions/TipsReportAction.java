package com.floreantpos.bo.actions;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.TipsReportView;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

public class TipsReportAction extends AbstractAction {

    public TipsReportAction() {
        super(Messages.getString("PosMessage.312"));
    }

    public TipsReportAction(String name) {
        super(name);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
        JTabbedPane tabbedPane = window.getTabbedPane();

        TipsReportView reportView = null;
        int index = tabbedPane.indexOfTab(Messages.getString("PosMessage.312"));
        if (index == -1) {
            reportView = new TipsReportView();
            tabbedPane.addTab(Messages.getString("PosMessage.312"), reportView);
        }
        else {
            reportView = (TipsReportView) tabbedPane.getComponentAt(index);
        }
        tabbedPane.setSelectedComponent(reportView);
    }

}
