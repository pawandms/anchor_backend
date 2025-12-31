package com.anchor.app.action.exceptions;

import com.anchor.app.exception.BaseException;

public class ActionLogServiceException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ActionLogServiceException(String message)
	{
		super(message);
	}

	public ActionLogServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
