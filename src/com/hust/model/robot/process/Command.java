package com.hust.model.robot.process;

import com.hust.utils.concurrent.BinarySemaphore;

public abstract class Command {

	public Process process;

	public int id;

	public static BinarySemaphore terminationFlag = new BinarySemaphore(true);

	public abstract void execute() throws InterruptedException;

	protected StringBuilder getStringBuilder() {
		return new StringBuilder(Integer.toString(id)).append(". ");
	}
}
