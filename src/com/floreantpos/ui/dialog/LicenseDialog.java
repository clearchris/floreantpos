package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

public class LicenseDialog extends POSDialog implements ActionListener, WindowListener {
	public static final String PROP_DO_NOT_SHOW_LICENSE = "license.do.not.show"; //$NON-NLS-1$
	public static final String PROP_LICENSE_AGREED = "license.agreed"; //$NON-NLS-1$
	private List<String> btnName;

	public LicenseDialog() {
		super(POSUtil.getBackOfficeWindow(), Messages.getString("LicenseDialog.1")); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());
	}

	@Override
	protected void initUI() {
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(new EmptyBorder(0, 15, 15, 15));

		JPanel contentPanel = new JPanel(new MigLayout("fill,wrap")); //$NON-NLS-1$
		JCheckBox cbNoPrompt = new JCheckBox(Messages.getString("LicenseDialog.6")); //$NON-NLS-1$
		
		JCheckBox cbAgreement = new JCheckBox(Messages.getString("LicenseDialog.7")); //$NON-NLS-1$
		cbAgreement.setSelected(AppConfig.getBoolean(LicenseDialog.PROP_LICENSE_AGREED, Boolean.FALSE));
		
		cbNoPrompt.addItemListener(e -> {
			AppConfig.put(LicenseDialog.PROP_DO_NOT_SHOW_LICENSE, ItemEvent.SELECTED == e.getStateChange()
					&& cbAgreement.isSelected());
		});
		
		contentPanel.add(cbNoPrompt, "right"); //$NON-NLS-1$
		container.add(contentPanel, BorderLayout.NORTH);
		
		JEditorPane jEditorPane = new JEditorPane();
		jEditorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jEditorPane);
		try {
			jEditorPane.setPage(getClass().getResource("/FloreantLicense.html")); //$NON-NLS-1$
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		}
		container.add(scrollPane, BorderLayout.CENTER);
		
		btnName = Arrays.asList(Messages.getString("LicenseDialog.2"), Messages.getString("LicenseDialog.3"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("LicenseDialog.4"), Messages.getString("LicenseDialog.5"));  //$NON-NLS-1$ //$NON-NLS-2$

		JPanel buttonPanel = new JPanel(new MigLayout("fillx")); //$NON-NLS-1$
		
		JButton btnOropos = new JButton(btnName.get(0));
		JButton btnBuyPlugin = new JButton(btnName.get(1));
		JButton btnHelp = new JButton(btnName.get(2));
		JButton btnContinue = new JButton(btnName.get(3));
		btnContinue.setEnabled(cbAgreement.isSelected());
		btnContinue.setBackground(Color.green);

		btnOropos.addActionListener(this);
		btnBuyPlugin.addActionListener(this);
		btnHelp.addActionListener(this);
		btnContinue.addActionListener(this);

		buttonPanel.add(new JSeparator(), "growx,cell 0 0,spanx,wrap,gapbottom 10"); //$NON-NLS-1$
		buttonPanel.add(cbAgreement, "cell 0 1"); //$NON-NLS-1$

		buttonPanel.add(btnOropos, "cell 1 1, split 5"); //$NON-NLS-1$
		buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy"); //$NON-NLS-1$
		buttonPanel.add(btnBuyPlugin);
		buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy"); //$NON-NLS-1$
		buttonPanel.add(btnHelp);
		buttonPanel.add(btnContinue, "cell 2 1, right"); //$NON-NLS-1$

		container.add(buttonPanel, BorderLayout.SOUTH);
		
		cbAgreement.addItemListener(e -> {
			AppConfig.put(LicenseDialog.PROP_LICENSE_AGREED, ItemEvent.SELECTED == e.getStateChange());
			btnContinue.setEnabled(ItemEvent.SELECTED == e.getStateChange());
			AppConfig.put(LicenseDialog.PROP_DO_NOT_SHOW_LICENSE, ItemEvent.SELECTED == e.getStateChange()
					&& cbNoPrompt.isSelected());
		});
		
		add(container);
		addWindowListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String btn = e.getActionCommand();
		
		switch (btnName.indexOf(btn)) {
		case 0:
			Application.getInstance().openWebpage("https://www.orocube.com/"); //$NON-NLS-1$
			break;
		case 1:
			Application.getInstance().openWebpage("https://shop.orocube.com/floreant/"); //$NON-NLS-1$
			break;
		case 2:
			Application.getInstance().openWebpage("https://guide.orocube.com/"); //$NON-NLS-1$
			break;
		case 3:
			dispose();
			break;

		default:
			break;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		if (AppConfig.getBoolean(LicenseDialog.PROP_LICENSE_AGREED, Boolean.FALSE)) {
			dispose();
			return;
		}
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
