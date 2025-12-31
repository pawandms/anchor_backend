package com.anchor.app.exception;

public class CollectionException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public CollectionException(String message)
	{
		super(message);
	}

	public CollectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
