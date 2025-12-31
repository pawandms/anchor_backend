package com.anchor.app.oauth.exceptions;

import com.anchor.app.exception.BaseException;

public class ConversionException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ConversionException(String message)
	{
		super(message);
	}

	public ConversionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
