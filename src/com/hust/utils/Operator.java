package com.hust.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hust.core.Configuration;

public abstract class Operator implements Runnable {
	protected long sleepTime = Configuration.sleepTime;
	
	protected long operatedTime;

	public ExecutorService executorService = Executors.newCachedThreadPool();
	
	public Future<?> future;

	public void setSleepTime(long millis) {
		this.sleepTime = millis;
	}

//	public void setThread(Thread thread) {
//		this.thread = thread;
//	}
//
//	public void startThread(Thread thread) {
//		this.thread = thread;
//		thread.start();
//	}

	public synchronized void start() {
		operatedTime = 0;
		future = executorService.submit(this);
	}

	public synchronized void abort() {
		if (future != null) {
			future.cancel(true);
			future = null;
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
		future = null;
	}

}
