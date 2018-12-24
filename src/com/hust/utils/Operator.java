package com.hust.utils;

import com.hust.core.Configuration;

public abstract class Operator implements Runnable {
	protected long sleepTime = Configuration.sleepTime;
	
	protected long operatedTime;

	public Thread thread;

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

	public synchronized void start() {
		if (thread != null) {
			thread.start();
		} else {
			Thread newThread = new Thread(this);
			thread = newThread;
			thread.start();
		}
	}

	public synchronized void abort() {
		if (thread != null) {
			thread.interrupt();
			try {
				if (thread != null) {
					thread.join();
				}
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
				operatedTime += sleepTime;
			} catch (InterruptedException e) {
				break;
			}
		}
		terminate();
		operatedTime = 0;
		thread = null;
	}

}
