package com.anchor.app.exception;

public class AuthServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public AuthServiceException(String message)
	{
		super(message);
	}

	public AuthServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
