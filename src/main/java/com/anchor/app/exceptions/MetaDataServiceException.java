package com.anchor.app.exceptions;

public class MetaDataServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public MetaDataServiceException(String message)
	{
		super(message);
	}

	public MetaDataServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
