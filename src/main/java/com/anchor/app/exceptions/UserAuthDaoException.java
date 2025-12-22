package com.anchor.app.exceptions;

public class UserAuthDaoException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public UserAuthDaoException(String message)
	{
		super(message);
	}

	public UserAuthDaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
