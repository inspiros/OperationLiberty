package com.hust.utils;

public class OperationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156938934428103340L;

	protected final int id;
	
	public int getId() {
		return id;
	}
	
	public OperationException(int id) {
		this.id = id;
	}
	
	public OperationException(String message, int id) {
		super(message);
		this.id = id;
	}
}
