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
package com.floreantpos.extension;

import java.awt.Container;

import javax.swing.JPanel;

import com.floreantpos.ITicketList;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PaginatedTableModel;
import com.floreantpos.ui.RefreshableView;

public abstract class OnlineOrderPlugin extends AbstractFloreantPlugin {

	public abstract POSToggleButton initFilterButton(Container contentPane, ITicketList ticketLists);

	public abstract void loadTickets(PaginatedTableModel ticketListTableModel);

	public abstract void initOrderInfoActionButtons(JPanel panel, ITicketList iTicketList, RefreshableView refreshableView);

	public abstract void initSwitchboardActionButtons(JPanel onlineOrdersActionButtonPanel, ITicketList iTicketList, RefreshableView refreshableView);
}
