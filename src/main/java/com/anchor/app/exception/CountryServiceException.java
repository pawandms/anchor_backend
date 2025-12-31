package com.anchor.app.exception;

public class CountryServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public CountryServiceException(String message)
	{
		super(message);
	}

	public CountryServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
