package com.anchor.app.exception;

public class PeopleException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public PeopleException(String message)
	{
		super(message);
	}

	public PeopleException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
