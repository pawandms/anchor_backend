package com.anchor.app.oauth.exceptions;

import com.anchor.app.exceptions.BaseException;

public class HifiAuthServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public HifiAuthServiceException(String message)
	{
		super(message);
	}

	public HifiAuthServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
