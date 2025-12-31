package com.anchor.app.msg.exceptions;

import com.anchor.app.exception.BaseException;

public class ChannelDaoException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ChannelDaoException(String message)
	{
		super(message);
	}

	public ChannelDaoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
