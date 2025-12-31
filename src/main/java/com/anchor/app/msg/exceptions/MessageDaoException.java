package com.anchor.app.msg.exceptions;

import com.anchor.app.exception.BaseException;

public class MessageDaoException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MessageDaoException(String message)
	{
		super(message);
	}

	public MessageDaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
