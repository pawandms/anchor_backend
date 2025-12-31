package com.anchor.app.msg.exceptions;

import com.anchor.app.exception.BaseException;

public class NatsServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public NatsServiceException(String message)
	{
		super(message);
	}

	public NatsServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
