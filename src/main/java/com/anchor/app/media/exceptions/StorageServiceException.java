package com.anchor.app.media.exceptions;

import com.anchor.app.exceptions.BaseException;

public class StorageServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public StorageServiceException(String message)
	{
		super(message);
	}

	public StorageServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
