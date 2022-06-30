package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

public class LicenseDialog extends POSDialog implements ActionListener {
	public static final String DO_NOT_SHOW_LICENSE = "do.not.show.license"; //$NON-NLS-1$
	private List<String> btnName;

	public LicenseDialog() {
		super(POSUtil.getBackOfficeWindow(), "License"); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());
	}

	@Override
	protected void initUI() {
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel contentPanel = new JPanel(new MigLayout("fill,wrap")); //$NON-NLS-1$
		contentPanel.setBackground(Color.white);
		contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
				new EmptyBorder(10, 20, 20, 20)));

		JEditorPane jEditorPane = new JEditorPane();
		jEditorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jEditorPane);

		try {
			jEditorPane.setPage(getClass().getClassLoader().getResource("FloreantLicense.html")); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
		container.add(scrollPane, BorderLayout.CENTER);
		
		btnName = Arrays.asList(Messages.getString("LicenseDialog.2"), Messages.getString("LicenseDialog.3"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("LicenseDialog.4"), Messages.getString("LicenseDialog.5"));  //$NON-NLS-1$ //$NON-NLS-2$

		JPanel buttonPanel = new JPanel(new MigLayout("fillx")); //$NON-NLS-1$
		JCheckBox cbNoPrompt = new JCheckBox(Messages.getString("LicenseDialog.7")); //$NON-NLS-1$
		cbNoPrompt.addItemListener(e -> {
			AppConfig.put(DO_NOT_SHOW_LICENSE, ItemEvent.SELECTED == e.getStateChange());
		});
		JButton btnOropos = new JButton(btnName.get(0));
		JButton btnBuyPlugin = new JButton(btnName.get(1));
		JButton btnHelp = new JButton(btnName.get(2));
		JButton btnClose = new JButton(btnName.get(3));

		btnOropos.addActionListener(this);
		btnBuyPlugin.addActionListener(this);
		btnHelp.addActionListener(this);
		btnClose.addActionListener(this);

		buttonPanel.add(new JSeparator(), "growx,cell 0 0,spanx,wrap,gapbottom 10"); //$NON-NLS-1$
		buttonPanel.add(cbNoPrompt, "cell 0 1"); //$NON-NLS-1$

		buttonPanel.add(btnOropos, "cell 1 1, split 5"); //$NON-NLS-1$
		buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy"); //$NON-NLS-1$
		buttonPanel.add(btnBuyPlugin);
		buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy"); //$NON-NLS-1$
		buttonPanel.add(btnHelp);
		buttonPanel.add(btnClose, "cell 2 1, right"); //$NON-NLS-1$

		container.add(buttonPanel, BorderLayout.SOUTH);

		add(container);
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

}
