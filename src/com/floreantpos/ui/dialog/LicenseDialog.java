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
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

public class LicenseDialog extends POSDialog implements ActionListener, WindowListener {
	public static final String PROP_DO_NOT_SHOW_LICENSE = "license.do.not.show"; //$NON-NLS-1$
	public static final String PROP_LICENSE_AGREED = "license.agreed"; //$NON-NLS-1$
	private List<String> btnName;

	public LicenseDialog() {
		super(POSUtil.getBackOfficeWindow(), Messages.getString("LicenseDialog.0")); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());
	}

	@Override
	protected void initUI() {
		TransparentPanel container = new TransparentPanel(new BorderLayout());
		container.setBorder(new EmptyBorder(0,5,5,5));

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("LicenseDialog.1")); //$NON-NLS-1$
		titlePanel.setSeparatorVisible(false);

		JCheckBox cbNoPrompt = new JCheckBox(Messages.getString("LicenseDialog.6")); //$NON-NLS-1$
		JCheckBox cbAgreement = new JCheckBox(Messages.getString("LicenseDialog.7")); //$NON-NLS-1$
		cbAgreement.setSelected(AppConfig.getBoolean(LicenseDialog.PROP_LICENSE_AGREED, Boolean.FALSE));

		JPanel footerChkPanel = new JPanel(new MigLayout("ins 0,fill")); //$NON-NLS-1$
		footerChkPanel.add(cbAgreement, "wrap"); //$NON-NLS-1$
		footerChkPanel.add(cbNoPrompt);

		container.add(titlePanel, BorderLayout.NORTH);

		JEditorPane jEditorPane = new JEditorPane();
		jEditorPane.setBorder(new EmptyBorder(0, 10, 0, 10));
		jEditorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jEditorPane);
		try {
			jEditorPane.setPage(getClass().getResource("/FloreantLicense.html")); //$NON-NLS-1$
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		}
		container.add(scrollPane, BorderLayout.CENTER);

		btnName = Arrays.asList(Messages.getString("LicenseDialog.2"), Messages.getString("LicenseDialog.3"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("LicenseDialog.4"), Messages.getString("LicenseDialog.5"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("LicenseDialog.8"), Messages.getString("LicenseDialog.9")); //$NON-NLS-1$ //$NON-NLS-2$

		JPanel buttonPanel = new JPanel(new MigLayout("ins 0,fill")); //$NON-NLS-1$

		JButton btnOroVsFp = new JButton(btnName.get(5));

		JButton btnOropos = new JButton(btnName.get(0));
		JButton btnBuyPlugin = new JButton(btnName.get(1));
		JButton btnHelp = new JButton(btnName.get(2));
		JButton btnCcpsupport = new JButton(btnName.get(4));

		JButton btnContinue = new JButton(btnName.get(3));
		btnContinue.setBackground(Color.green);

		btnOroVsFp.addActionListener(this);
		btnOropos.addActionListener(this);
		btnBuyPlugin.addActionListener(this);
		btnHelp.addActionListener(this);
		btnCcpsupport.addActionListener(this);
		btnContinue.addActionListener(this);

		buttonPanel.add(btnOropos, "grow"); //$NON-NLS-1$
		buttonPanel.add(btnBuyPlugin, "grow,wrap"); //$NON-NLS-1$
		buttonPanel.add(btnCcpsupport, "grow"); //$NON-NLS-1$
		buttonPanel.add(btnHelp, "grow"); //$NON-NLS-1$

		JPanel footerPanel = new JPanel(new MigLayout("ins 10,fill")); //$NON-NLS-1$
		footerPanel.add(footerChkPanel);
		footerPanel.add(btnOroVsFp, " grow"); //$NON-NLS-1$
		footerPanel.add(buttonPanel, "center, growy"); //$NON-NLS-1$
		footerPanel.add(btnContinue,"right, grow"); //$NON-NLS-1$

		container.add(footerPanel, BorderLayout.SOUTH);

		cbNoPrompt.setEnabled(cbAgreement.isSelected());
		btnContinue.setEnabled(cbAgreement.isSelected());
		
		cbNoPrompt.addItemListener(e -> {
			AppConfig.put(LicenseDialog.PROP_DO_NOT_SHOW_LICENSE,
					ItemEvent.SELECTED == e.getStateChange() && cbAgreement.isSelected());
		});
		
		cbAgreement.addItemListener(e -> {
			AppConfig.put(LicenseDialog.PROP_LICENSE_AGREED, ItemEvent.SELECTED == e.getStateChange());
			cbNoPrompt.setEnabled(ItemEvent.SELECTED == e.getStateChange());
			btnContinue.setEnabled(ItemEvent.SELECTED == e.getStateChange());
			AppConfig.put(LicenseDialog.PROP_DO_NOT_SHOW_LICENSE,
					ItemEvent.SELECTED == e.getStateChange() && cbNoPrompt.isSelected());
		});

		add(container);
		addWindowListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String btn = e.getActionCommand();

		switch (btnName.indexOf(btn)) {
		case 0:
			Application.getInstance().openWebpage(
					"https://shop.orocube.com/?invoice=eyJyZXNlbGxlcklkIjoib3JvY3ViZSIsInN1YnNjcmlwdGlvblR5cGUiOiJ5ZWFyIiwicHJvZHVjdHMiOlt7InRlcm1rZXkiOiIiLCJuYW1lIjoiT1JPUE9TIFBybyIsImlkIjoib3JvcG9zLXBybyJ9XX0="); //$NON-NLS-1$
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
		case 4:
			Application.getInstance().openWebpage("https://pos.orocube.com/payments/?source=floreantpos"); //$NON-NLS-1$
			break;
		case 5:
			Application.getInstance().openWebpage("https://pos.orocube.com/oro-pos-vs-floreant-pos/"); //$NON-NLS-1$
			break;

		default:
			break;
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (AppConfig.getBoolean(LicenseDialog.PROP_LICENSE_AGREED, Boolean.FALSE)) {
			dispose();
			return;
		}
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
