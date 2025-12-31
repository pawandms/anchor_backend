package com.anchor.app.msg.exceptions;

import com.anchor.app.exception.BaseException;

public class MsgServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MsgServiceException(String message)
	{
		super(message);
	}

	public MsgServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
