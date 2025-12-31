package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.Date;

public class ChnlUserEnrolmentVo implements Serializable {
	
	private Date validFrom;
	private Date validTo;
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	
	

}
