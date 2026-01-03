package com.anchor.app.oauth.exceptions;

import com.anchor.app.exceptions.BaseException;

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
