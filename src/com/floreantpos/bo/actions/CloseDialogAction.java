package com.floreantpos.bo.actions;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseDialogAction extends AbstractAction {

	public CloseDialogAction() {
		super(POSConstants.CLOSE);
	}

	public CloseDialogAction(String name) {
		super(name);
	}

	public CloseDialogAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		window.close();
	}

}
