package com.hust.utils;

import com.hust.core.DataBuffer;

public abstract class Operator implements Runnable {
	protected long sleepTime = DataBuffer.SLEEP_TIME;

	protected Thread thread;

	public void setSleepTime(long millis) {
		this.sleepTime = millis;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void startThread(Thread thread) {
		this.thread = thread;
		thread.start();
	}

	public void start() {
		if (thread != null) {
			thread.start();
		} else {
			Thread newThread = new Thread(this);
			thread = newThread;
			thread.start();
		}
	}

	public void abort() {
		if (thread != null) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract boolean terminateCondition();

	protected abstract void setup();

	protected abstract void loop();

	protected abstract void terminate();

	@Override
	public void run() {
		setup();
		while (!terminateCondition()) {
			try {
				loop();
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				break;
			}
		}
		terminate();
		thread = null;
	}

}
