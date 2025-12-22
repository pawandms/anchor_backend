package com.anchor.app.exceptions;

public class ContentServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ContentServiceException(String message)
	{
		super(message);
	}

	public ContentServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
