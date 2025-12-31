package com.anchor.app.exception;

public class MediaException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MediaException(String message)
	{
		super(message);
	}

	public MediaException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
