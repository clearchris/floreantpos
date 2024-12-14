package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ServiceChargeExplorer;

public class ServiceChargeExplorerAction extends AbstractAction {

    public ServiceChargeExplorerAction() {
        super(Messages.getString("ServiceChargeExplorerAction.0")); //$NON-NLS-1$
    }

    public ServiceChargeExplorerAction(String name) {
        super(name);
    }

    public ServiceChargeExplorerAction(String name, Icon icon) {
        super(name, icon);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

            ServiceChargeExplorer explorer = null;
            JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
            int index = tabbedPane.indexOfTab(Messages .getString("ServiceChargeExplorerAction.1")); //$NON-NLS-1$
            if (index == -1) {
                explorer = new ServiceChargeExplorer();

                tabbedPane.addTab(Messages.getString("ServiceChargeExplorerAction.1"), explorer); //$NON-NLS-1$
            }
            else {
                explorer = (ServiceChargeExplorer) tabbedPane.getComponentAt(index);
            }
            tabbedPane.setSelectedComponent(explorer);
        } catch (Exception x) {
            BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
        }
    }
}
