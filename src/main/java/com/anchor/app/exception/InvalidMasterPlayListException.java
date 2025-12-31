package com.anchor.app.exception;

public class InvalidMasterPlayListException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public InvalidMasterPlayListException(String message)
	{
		super(message);
	}

	public InvalidMasterPlayListException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
