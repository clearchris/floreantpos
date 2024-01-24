package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.GiftCertificateExplorer;

public class GiftCertificateExplorerAction extends AbstractAction {

	public GiftCertificateExplorerAction() {
		super(Messages.getString("GiftCertificateExplorerAction.1"));
	}

	public GiftCertificateExplorerAction(String name) {
		super(name);
	}

	public GiftCertificateExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		GiftCertificateExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("GiftCertificateExplorerAction.1"));
		if (index == -1) {
			explorer = new GiftCertificateExplorer();
			tabbedPane.addTab(Messages.getString("GiftCertificateExplorerAction.1"), explorer);
		}
		else {
			explorer = (GiftCertificateExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
}
