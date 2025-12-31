package com.anchor.app.exception;

public class ImageServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ImageServiceException(String message)
	{
		super(message);
	}

	public ImageServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
