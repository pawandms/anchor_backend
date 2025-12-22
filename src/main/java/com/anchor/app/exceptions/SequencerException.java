package com.anchor.app.exceptions;

public class SequencerException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public SequencerException(String message)
	{
		super(message);
	}

	public SequencerException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
