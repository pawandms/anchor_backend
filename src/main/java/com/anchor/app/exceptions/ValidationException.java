package com.anchor.app.exceptions;

public class ValidationException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ValidationException(String message)
	{
		super(message);
	}

	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
