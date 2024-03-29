/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.actions;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;

public abstract class PosAction extends AbstractAction {
	private boolean visible = true;
	protected UserPermission requiredPermission;
	protected User authorizedUser;
	
	public PosAction() {
		
	}

	public PosAction(String name) {
		super("<html><center>"+name);
	}

	public PosAction(Icon icon) {
		super(null, icon);
	}

	public PosAction(String name, Icon icon) {
		super("<html><center>"+name, icon);
	}

	public PosAction(String name, UserPermission requiredPermission) {
		super("<html><center>"+name);

		this.requiredPermission = requiredPermission;
	}
	
	public PosAction(Icon icon, UserPermission requiredPermission) {
		super(null, icon);
		
		this.requiredPermission = requiredPermission;
	}

	public UserPermission getRequiredPermission() {
		return requiredPermission;
	}

	public void setRequiredPermission(UserPermission requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		User user = Application.getCurrentUser();

		if (requiredPermission == null) {
			execute(actionEvent);
			return;
		}

		if (!user.hasPermission(requiredPermission)) {
			String password = PasswordEntryDialog.show((Component) actionEvent.getSource(), Messages.getString("PosAction.0")); //$NON-NLS-1$
			if(StringUtils.isEmpty(password)) {
				return;
			}
			
			User user2 = UserDAO.getInstance().findUserBySecretKey(password);
			if(user2 == null) {
				POSMessageDialog.showError((Component) actionEvent.getSource(), Messages.getString("PosAction.1")); //$NON-NLS-1$
			}
			else {
				setAuthorizedUser(user2);
				if(!user2.hasPermission(requiredPermission)) {
					POSMessageDialog.showError((Component) actionEvent.getSource(), Messages.getString("PosAction.2")); //$NON-NLS-1$
				}
				else {
					execute(actionEvent);
				}
			}
			
			//POSMessageDialog.showError(Application.getPosWindow(), "You do not have permission to execute this action");
			return;
		}
		setAuthorizedUser(user);
		execute(actionEvent);
	}

	public abstract void execute();

	/* 	If an action is called from anywhere but the root window,
		it's best to have the ActionEvent available so the parent can be set.
	 */
	// TODO: Test execute(ActionEvent) and consider moving all actions to this signature
	public void execute(ActionEvent actionEvent){
		execute();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public User getAuthorizedUser() {
		if (this.authorizedUser == null) {
			return Application.getCurrentUser();
		}
		return this.authorizedUser;
	}

	public void setAuthorizedUser(User authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	//	public boolean isAllowAdministrator() {
	//		return allowAdministrator;
	//	}
	//
	//	public void setAllowAdministrator(boolean allowAdministrator) {
	//		this.allowAdministrator = allowAdministrator;
	//	}
}
