package com.teaboot.context.exception;

import java.io.IOException;

public class ResourceNotFoundException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8819556200577695350L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}

	public String getStackTraceInfo() {
		StackTraceElement[] ste = getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ste.length; i++) {
			sb.append(ste[i].toString());
		}
		return sb.toString();
	}
}
