package com.floreantpos.model;

import com.floreantpos.model.base.BaseServiceCharge;

public class ServiceCharge extends BaseServiceCharge {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public ServiceCharge() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ServiceCharge(java.lang.Integer id) {
		super(id);
	}
	/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return  (super.getName() != "null" ? super.getName() : "") + " (" + super.getRate() + "%)";
	}
}
