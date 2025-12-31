package com.anchor.app.importer.exception;

import com.anchor.app.exception.BaseException;

public class ImportMediaServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public ImportMediaServiceException(String message)
	{
		super(message);
	}

	public ImportMediaServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
