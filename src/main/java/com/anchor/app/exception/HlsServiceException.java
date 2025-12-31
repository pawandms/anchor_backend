package com.anchor.app.exception;

public class HlsServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public HlsServiceException(String message)
	{
		super(message);
	}

	public HlsServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
