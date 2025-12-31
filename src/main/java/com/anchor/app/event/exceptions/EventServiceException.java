package com.anchor.app.event.exceptions;

import com.anchor.app.exception.BaseException;

public class EventServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public EventServiceException(String message)
	{
		super(message);
	}

	public EventServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
