package com.anchor.app.exception;

public class UserServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public UserServiceException(String message)
	{
		super(message);
	}

	public UserServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
