package com.floreantpos.actions;

import com.floreantpos.main.Application;
import javax.swing.*;

public class GlassWrapperAction extends PosAction {
	private PosAction originalAction;

	public GlassWrapperAction(PosAction originalAction) {
		this.originalAction = originalAction;

		if (originalAction.getValue(Action.NAME) != null) {
			putValue(Action.NAME, (String) originalAction.getValue(Action.NAME));
		}
		if (originalAction.getValue(Action.SMALL_ICON) != null) {
			putValue(Action.SMALL_ICON, (Icon) originalAction.getValue(Action.SMALL_ICON));
		}
	}

	@Override
	public void execute() {
		try {
			Application.getPosWindow().setGlassPaneVisible(true);
			originalAction.execute();
		} finally {
			Application.getPosWindow().setGlassPaneVisible(false);
		}
	}
}
