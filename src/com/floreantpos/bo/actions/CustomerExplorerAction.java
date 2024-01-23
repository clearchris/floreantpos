package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.customer.CustomerExplorer;

public class CustomerExplorerAction extends AbstractAction {

	public CustomerExplorerAction() {
		super(Messages.getString("CustomerExplorerAction.1"));
	}

	public CustomerExplorerAction(String name) {
		super(name);
	}

	public CustomerExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		CustomerExplorer explorer;
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("CustomerExplorerAction.1"));
		if (index == -1) {
			explorer = new CustomerExplorer();
			tabbedPane.addTab(Messages.getString("CustomerExplorerAction.1"), explorer);
		}
		else explorer = (CustomerExplorer) tabbedPane.getComponentAt(index);
		tabbedPane.setSelectedComponent(explorer);
	}
}
