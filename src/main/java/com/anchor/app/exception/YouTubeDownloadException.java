package com.anchor.app.exception;

public class YouTubeDownloadException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073323840744734538L;

	public YouTubeDownloadException(String message)
	{
		super(message);
	}

	public YouTubeDownloadException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
