package com.anchor.app.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Transient;

public class BaseVo implements Serializable {

	@Transient
	private boolean valid;
	
	@Transient
	private String errorCode;
	
	@Transient
	private String errorMessage;
	
	@Transient
	private List<ErrorMsg> errors = new ArrayList<>();

	public BaseVo() {
		this.valid = true;
	}

	
	public boolean isValid() {
		return valid;
	}


	public void setValid(boolean valid) {
		this.valid = valid;
	}


	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public List<ErrorMsg> getErrors() {
		return errors;
	}


}
