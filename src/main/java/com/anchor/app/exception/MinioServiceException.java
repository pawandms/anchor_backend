package com.anchor.app.exception;

public class MinioServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MinioServiceException(String message)
	{
		super(message);
	}

	public MinioServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
