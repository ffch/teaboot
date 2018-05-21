package com.teaboot.context.exception;

public class ReflectErrorException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8819556200577695350L;

	public ReflectErrorException() {
		super();
	}

	public ReflectErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectErrorException(String message) {
		super(message);
	}

	public ReflectErrorException(Throwable cause) {
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

