package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.PosTransactionExplorer;
import com.floreantpos.model.Terminal;

public class PosTransactionExplorerAction extends AbstractAction {

    public PosTransactionExplorerAction() {
        super(Messages.getString("PosTransactionExplorerAction.0"));
    }

    public PosTransactionExplorerAction(Integer ticketId) {
        super(Messages.getString("PosTransactionExplorerAction.0"));
        putValue("TicketID", ticketId);
    }
    public PosTransactionExplorerAction(Date fromDate, Date toDate, Terminal terminal) {
        super(Messages.getString("PosTransactionExplorerAction.0"));
        putValue("FromDate", fromDate);
        putValue("ToDate", toDate);
        putValue("Terminal", terminal);
    }

    public PosTransactionExplorerAction(String name) {
        super(name);
    }

    public PosTransactionExplorerAction(String name, Icon icon) {
        super(name, icon);
    }

    public void actionPerformed(ActionEvent e) {
        BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

        PosTransactionExplorer explorer = null;
        JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
        int index = tabbedPane.indexOfTab(Messages.getString("PosTransactionExplorerAction.0"));
        if (index == -1) {
            explorer = new PosTransactionExplorer();
            tabbedPane.addTab(Messages.getString("PosTransactionExplorerAction.0"), explorer);
        }
        else {
            explorer = (PosTransactionExplorer) tabbedPane.getComponentAt(index);
        }
        tabbedPane.setSelectedComponent(explorer);
        Integer ticketId = (Integer) getValue("TicketID");
        if(ticketId != null) {
            explorer.setTicketId(ticketId);
        }
        Date fromDate = (Date) getValue("FromDate");
        Date toDate = (Date) getValue("ToDate");
        if(fromDate != null && toDate != null) {
            explorer.setDateRange(fromDate, toDate);
        }
    }
}
