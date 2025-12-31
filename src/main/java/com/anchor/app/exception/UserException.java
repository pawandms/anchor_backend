package com.anchor.app.exception;

public class UserException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public UserException(String message)
	{
		super(message);
	}

	public UserException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
