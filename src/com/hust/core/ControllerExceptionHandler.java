package com.hust.core;

public class ControllerExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println(t.getName() + " - " + e.getMessage());
		e.printStackTrace();
	}

}
