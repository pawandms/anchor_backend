package com.anchor.app.exception;



public class CacheServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public CacheServiceException(String message)
	{
		super(message);
	}

	public CacheServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
