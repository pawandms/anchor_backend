package com.anchor.app.exception;

public class CountryException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public CountryException(String message)
	{
		super(message);
	}

	public CountryException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
