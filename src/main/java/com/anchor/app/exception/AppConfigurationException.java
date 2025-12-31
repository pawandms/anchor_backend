package com.anchor.app.exception;

public class AppConfigurationException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public AppConfigurationException(String message)
	{
		super(message);
	}

	public AppConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
