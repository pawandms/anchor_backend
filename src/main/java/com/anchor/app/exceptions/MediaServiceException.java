package com.anchor.app.exceptions;

public class MediaServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MediaServiceException(String message)
	{
		super(message);
	}

	public MediaServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
