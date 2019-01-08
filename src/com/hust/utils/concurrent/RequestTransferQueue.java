package com.hust.utils.concurrent;

import java.util.concurrent.LinkedTransferQueue;

public class RequestTransferQueue<E> extends LinkedTransferQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4230793549898569866L;

	public boolean requested;

	public boolean isRequested() {
		return requested;
	}

	public void request() {
		requested = true;
	}

	public void free() {
		requested = false;
	}
}
