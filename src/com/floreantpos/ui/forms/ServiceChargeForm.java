package com.floreantpos.ui.forms;

import javax.swing.*;

import com.floreantpos.Messages;
import net.miginfocom.swing.MigLayout;
import com.floreantpos.model.ServiceCharge;
import com.floreantpos.model.dao.ServiceChargeDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ServiceChargeForm extends BeanEditor {

    private FixedLengthTextField tfName;
    private DoubleTextField tfRate;

    public ServiceChargeForm() {
        this(new ServiceCharge());
    }

    public ServiceChargeForm(ServiceCharge serviceCharge) {
        initComponents();
        setBean(serviceCharge);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel(new MigLayout("hidemode 3,fill"));

        JLabel lblName = new JLabel(Messages.getString("ServiceChargeForm.1")); //$NON-NLS-1$
        tfName = new FixedLengthTextField(ServiceCharge.PROP_NAME_LENGTH);

        JLabel lblRate = new JLabel(Messages.getString("ServiceChargeForm.2")); //$NON-NLS-1$
        tfRate = new DoubleTextField(10);

        contentPanel.add(lblName, "cell 0 0");
        contentPanel.add(tfName, "cell 1 0");
        contentPanel.add(lblRate, "cell 0 1");
        contentPanel.add(tfRate, "cell 1 1");

        add(contentPanel);
    }

    @Override
    public boolean save() {
        try {
            if (!updateModel())
                return false;

            ServiceCharge serviceCharge = (ServiceCharge) getBean();
            ServiceChargeDAO dao = new ServiceChargeDAO();
            dao.saveOrUpdate(serviceCharge);
        } catch (Exception e) {
            POSMessageDialog.showError(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected void updateView() {
        ServiceCharge serviceCharge = (ServiceCharge) getBean();
        if (serviceCharge == null) {
            return;
        }
        tfName.setText(serviceCharge.getName());
        tfRate.setText(String.valueOf(serviceCharge.getRate()));
    }

    @Override
    protected boolean updateModel() {
        ServiceCharge serviceCharge = (ServiceCharge) getBean();

        String name = tfName.getText();
        if (POSUtil.isBlankOrNull(name)) {
            POSMessageDialog.showError(POSUtil.getFocusedWindow(), Messages.getString("ServiceChargeForm.3")); //$NON-NLS-1$
            return false;
        }

        double rate = tfRate.getDouble();
        serviceCharge.setName(name);
        serviceCharge.setRate(rate);

        return true;
    }

    @Override
    public String getDisplayText() {
        ServiceCharge serviceCharge = (ServiceCharge) getBean();
        if (serviceCharge.getId() == null) {
            return Messages.getString("ServiceChargeForm.4"); //$NON-NLS-1$
        }
        return Messages.getString("ServiceChargeForm.5"); //$NON-NLS-1$
    }
}
