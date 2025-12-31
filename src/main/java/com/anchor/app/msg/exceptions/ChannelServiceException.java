package com.anchor.app.msg.exceptions;

import com.anchor.app.exception.BaseException;

public class ChannelServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ChannelServiceException(String message)
	{
		super(message);
	}

	public ChannelServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
