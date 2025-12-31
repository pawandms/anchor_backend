package com.anchor.app.exception;

public class GenreException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public GenreException(String message)
	{
		super(message);
	}

	public GenreException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
