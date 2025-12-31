package com.anchor.app.exception;

public class InvalidMediaPathException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public InvalidMediaPathException(String message)
	{
		super(message);
	}

	public InvalidMediaPathException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
