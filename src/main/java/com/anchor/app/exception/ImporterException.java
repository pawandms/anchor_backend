package com.anchor.app.exception;

public class ImporterException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ImporterException(String message)
	{
		super(message);
	}

	public ImporterException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
