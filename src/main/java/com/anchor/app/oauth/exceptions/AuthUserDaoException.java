package com.anchor.app.oauth.exceptions;

import com.anchor.app.exceptions.BaseException;

public class AuthUserDaoException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public AuthUserDaoException(String message)
	{
		super(message);
	}

	public AuthUserDaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
