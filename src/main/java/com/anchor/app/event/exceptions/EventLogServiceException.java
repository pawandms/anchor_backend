package com.anchor.app.event.exceptions;

import com.anchor.app.exception.BaseException;

public class EventLogServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public EventLogServiceException(String message)
	{
		super(message);
	}

	public EventLogServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
