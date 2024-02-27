
package com.floreantpos.actions;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.*;
import com.floreantpos.services.TicketService;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.util.POSUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ShowTicketAction extends PosAction {

	private int ticketId = 0;
	private ITicketList ticketList;

	public ShowTicketAction(){
		super();
	}

	public ShowTicketAction(int ticketId) {
		super(Integer.toString(ticketId));
		this.ticketId = ticketId;
	}

	public ShowTicketAction(ITicketList tickets) {
		this.ticketList = tickets;
	}

	@Override
	public void execute() {
		return;
	}

	@Override
	public void execute(ActionEvent actionEvent) {
		try {
			if (ticketId == 0) {
				int ticketId = NumberSelectionDialog2.takeIntInput(Messages.getString("SwitchboardView.0"));
				if (ticketId == -1) {
					return;
				}
			}

			List<Ticket> ticketsToShow = new ArrayList<>();
			Ticket ticket = TicketService.getTicket(ticketId);
			ticketsToShow.add(ticket);

			OrderInfoView view = new OrderInfoView(ticketsToShow);
			// ticketList is null, seems to be used for an oroCube plugin
			OrderInfoDialog dialog = new OrderInfoDialog(
					(JFrame) SwingUtilities.getRoot((Component) actionEvent.getSource()), view, ticketList);
			Dimension dimension = Application.getPosWindow().getSize();
			dialog.setSize(PosUIManager.getSize(400), (int)dimension.getHeight()-100);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);
		} catch (PosException e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}

}