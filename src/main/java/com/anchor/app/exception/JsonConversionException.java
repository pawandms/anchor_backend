package com.anchor.app.exception;

public class JsonConversionException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public JsonConversionException(String message)
	{
		super(message);
	}

	public JsonConversionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
