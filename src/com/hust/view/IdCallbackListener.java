package com.hust.view;

import controlP5.CallbackListener;

public abstract class IdCallbackListener implements CallbackListener {
	
	private int id;
	
	public IdCallbackListener(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

}
