package com.hust.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.hust.core.Configuration;

public abstract class Operator implements Runnable {
	
	protected long operatedTime;
	
	protected Runnable loop;

	public ExecutorService executorService = Executors.newSingleThreadExecutor();
	public ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	public Future<?> future;
	public ScheduledFuture<?> scheduledFuture;

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
		setup();
		scheduledFuture = scheduler.scheduleAtFixedRate(this, 0, Configuration.sleepTime, TimeUnit.MILLISECONDS);
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

	protected abstract void terminate();

	@Override
	public void run() {
		loop.run();
		operatedTime += Configuration.sleepTime;
		terminate();
		future = null;
	}

}
