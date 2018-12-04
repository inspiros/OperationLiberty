package com.hust.view.utils;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;

public class IdCallbackListener implements CallbackListener {
	
	private int id;
	
	public IdCallbackListener(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public void controlEvent(CallbackEvent arg0) {
		// TODO Auto-generated method stub
	}

}
