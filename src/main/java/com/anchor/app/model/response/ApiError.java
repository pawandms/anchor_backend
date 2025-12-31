package com.anchor.app.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.anchor.app.vo.ErrorMsg;
public class ApiError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -742402312245404743L;
	
	private HttpStatus status;
    private String message;
    private List<ErrorMsg> errors = new ArrayList<>();

    public ApiError(HttpStatus status, String message, List<ErrorMsg> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
       
    }

	public HttpStatus getStatus() {
		return status;
	}


	public String getMessage() {
		return message;
	}

	public List<ErrorMsg> getErrors() {
		return errors;
	}


    
}
