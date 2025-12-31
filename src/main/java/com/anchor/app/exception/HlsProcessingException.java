package com.anchor.app.exception;

public class HlsProcessingException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public HlsProcessingException(String message)
	{
		super(message);
	}

	public HlsProcessingException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
