package com.anchor.app.oauth.exceptions;

import com.anchor.app.exception.BaseException;

public class AuthClientDaoException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public AuthClientDaoException(String message)
	{
		super(message);
	}

	public AuthClientDaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
