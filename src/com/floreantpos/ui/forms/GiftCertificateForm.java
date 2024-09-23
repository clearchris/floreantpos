package com.floreantpos.ui.forms;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.GiftCertificate;
import com.floreantpos.model.dao.GiftCertificateDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.main.Application;

public class GiftCertificateForm extends BeanEditor<GiftCertificate> {
    private JTextField tfNumber;
    private JTextField tfPin;
    private JTextField tfFaceValue;
    private JTextField tfCurrentBalance;
    private JTextField tfCreateDate;
    private JTextField tfSoldDate;
    private JTextField tfExpiryDate;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GiftCertificateForm() {
        createGiftCertificateForm();
    }

    private void createGiftCertificateForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(true);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new MigLayout("insets 10 10 10 10", "[][][][]", "[][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        inputPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("GiftCertificateForm.10")));

        JLabel lblNumber = new JLabel(Messages.getString("GiftCertificateForm.1") );
        tfNumber = new JTextField(30);

        JLabel lblPin = new JLabel(Messages.getString("GiftCertificateForm.2"));
        tfPin = new JTextField(30);

        JLabel lblFaceValue = new JLabel(Messages.getString("GiftCertificateForm.3"));
        tfFaceValue = new JTextField(30);

        JLabel lblCurrentBalance = new JLabel(Messages.getString("GiftCertificateForm.4"));
        tfCurrentBalance = new JTextField(30);

        JLabel lblCreateDate = new JLabel(Messages.getString("GiftCertificateForm.5"));
        tfCreateDate = new JTextField(30);

        JLabel lblSoldDate = new JLabel(Messages.getString("GiftCertificateForm.6"));
        tfSoldDate = new JTextField(30);

        JLabel lblExpiryDate = new JLabel(Messages.getString("GiftCertificateForm.7"));
        tfExpiryDate = new JTextField(30);

        inputPanel.add(lblNumber, "cell 0 0,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfNumber, "cell 1 0"); //$NON-NLS-1$

        inputPanel.add(lblPin, "cell 0 1,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfPin, "cell 1 1"); //$NON-NLS-1$

        inputPanel.add(lblFaceValue, "cell 0 2,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfFaceValue, "cell 1 2"); //$NON-NLS-1$

        inputPanel.add(lblCurrentBalance, "cell 0 3,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfCurrentBalance, "cell 1 3"); //$NON-NLS-1$

        inputPanel.add(lblCreateDate, "cell 0 4,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfCreateDate, "cell 1 4"); //$NON-NLS-1$

        inputPanel.add(lblSoldDate, "cell 0 5,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfSoldDate, "cell 1 5"); //$NON-NLS-1$

        inputPanel.add(lblExpiryDate, "cell 0 6,alignx right"); //$NON-NLS-1$
        inputPanel.add(tfExpiryDate, "cell 1 6"); //$NON-NLS-1$

        add(inputPanel, BorderLayout.CENTER);

        setFieldsEnable(true);
        setFieldsEditable(true);
    }

    @Override
    public void setFieldsEnable(boolean enable) {
        tfNumber.setEnabled(enable);
        tfPin.setEnabled(enable);
        tfFaceValue.setEnabled(enable);
        tfCurrentBalance.setEnabled(enable);
        tfCreateDate.setEnabled(enable);
        tfSoldDate.setEnabled(enable);
        tfExpiryDate.setEnabled(enable);
    }

    public void setFieldsEditable(boolean editable) {
        tfNumber.setEditable(editable);
        tfPin.setEditable(editable);
        tfFaceValue.setEditable(editable);
        tfCurrentBalance.setEditable(editable);
        tfCreateDate.setEditable(editable);
        tfSoldDate.setEditable(editable);
        tfExpiryDate.setEditable(editable);
    }

    @Override
    public void createNew() {
        GiftCertificate giftCertificate = new GiftCertificate();
        Date date = new Date();
        giftCertificate.setCreateDate(date);
        giftCertificate.setSoldDate(date);
        giftCertificate.setCurrentBalance(0.0);
        giftCertificate.setFaceValue(0.0);
        giftCertificate.setUser(Application.getCurrentUser().getAutoId());
        setBean(giftCertificate);
    }

    @Override
    public boolean save() {
        try {
            if (!updateModel())
                return false;
            GiftCertificate giftCertificate = (GiftCertificate) getBean();
            GiftCertificateDAO.getInstance().saveOrUpdate(giftCertificate);
            updateView();
            return true;
        } catch (IllegalModelStateException e) {
        } catch (StaleObjectStateException e) {
            BOMessageDialog.showError(this, Messages.getString("GiftCertificateForm.50")); //$NON-NLS-1$
        }
        return false;
    }

    @Override
    protected void updateView() {
        GiftCertificate giftCertificate = (GiftCertificate) getBean();
        if (giftCertificate == null) {
            return;
        }
        tfNumber.setText(giftCertificate.getNumber());
        tfPin.setText(giftCertificate.getPin());
        tfFaceValue.setText(String.valueOf(giftCertificate.getFaceValue()!=null?giftCertificate.getFaceValue():0));
        tfCurrentBalance.setText(String.valueOf(giftCertificate.getCurrentBalance()!=null?giftCertificate.getCurrentBalance():0));
        tfCreateDate.setText(dateFormat.format(giftCertificate.getCreateDate()));
        tfSoldDate.setText(dateFormat.format(giftCertificate.getSoldDate()));
        if(giftCertificate.getExpiryDate() != null && !giftCertificate.getExpiryDate().toString().isEmpty())
            tfExpiryDate.setText(dateFormat.format(giftCertificate.getExpiryDate()));
        else
            tfExpiryDate.setText("");
    }

    @Override
    protected boolean updateModel() throws IllegalModelStateException {
        String number = tfNumber.getText();
        String pin = tfPin.getText();
        String faceValue = tfFaceValue.getText();
        String currentBalance = tfCurrentBalance.getText();
        String createDate = tfCreateDate.getText();
        String soldDate = tfSoldDate.getText();
        String expiryDate = tfExpiryDate.getText();

        GiftCertificate giftCertificate = (GiftCertificate) getBean();

        if(StringUtils.isEmpty(number)  || StringUtils.isEmpty(faceValue) || StringUtils.isEmpty(currentBalance) || StringUtils.isEmpty(createDate) || StringUtils.isEmpty(soldDate)) {
            POSMessageDialog.showError(null, Messages.getString("GiftCertificateForm.53"));
            return false;
        }

        giftCertificate.setNumber(number);
        giftCertificate.setPin(pin);
        try {
            giftCertificate.setFaceValue(Double.parseDouble(faceValue));
            giftCertificate.setCurrentBalance(Double.parseDouble(currentBalance));
        } catch (Exception e) {
            POSMessageDialog.showError(null, Messages.getString("GiftCertificateForm.52"));
            return false;
        }
        try {
            giftCertificate.setCreateDate(dateFormat.parse(createDate));
            giftCertificate.setSoldDate(dateFormat.parse(soldDate));
            if (expiryDate.isEmpty())
                giftCertificate.setExpiryDate(null);
            else
                giftCertificate.setExpiryDate(dateFormat.parse(expiryDate));
        } catch (Exception e) {
            POSMessageDialog.showError(null, Messages.getString("GiftCertificateForm.51"));
            return false;
        }

        if (giftCertificate.getFaceValue() <= 0 || giftCertificate.getCurrentBalance() < 0) {
            POSMessageDialog.showError(null, Messages.getString("GiftCertificateForm.54"));
            return false;
        }

        GiftCertificate existingGC = GiftCertificateDAO.getInstance().getGiftCertificateByNumber(number);
        if (existingGC != null && existingGC.getId() != giftCertificate.getId()) {
            POSMessageDialog.showError(null, Messages.getString("GiftCertificateForm.56"));
            return false;
        }

        return true;
    }

    @Override
    public boolean delete() {
        try {
            GiftCertificate bean2 = getBean();
            if (bean2 == null)
                return false;

            int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(), "Are you sure to delete selected gift certificate?", "Confirm"); //$NON-NLS-1$ //$NON-NLS-2$
            if (option != JOptionPane.YES_OPTION) {
                return false;
            }

            GiftCertificateDAO.getInstance().delete(bean2);
            return true;
        } catch (Exception e) {
            POSMessageDialog.showError(Messages.getString("GiftCertificateForm.55"));
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        return Messages.getString("GiftCertificateForm.0");
    }
}