package com.anchor.app.event.exceptions;

import com.anchor.app.exception.BaseException;

public class FireBaseServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public FireBaseServiceException(String message)
	{
		super(message);
	}

	public FireBaseServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
