package com.anchor.app.exception;

public class AppUserServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public AppUserServiceException(String message)
	{
		super(message);
	}

	public AppUserServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
