package com.hust.view.javafx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hust.core.Configurations;

import javafx.application.Platform;

public abstract class FxSceneSyncService<T> implements Runnable {

	protected ExecutorService executorService;
	protected Future<?> future;

	protected T currentVal = null;
	protected T futureVal = null;
	protected boolean hasTask;

	public FxSceneSyncService() {
		executorService = Executors.newSingleThreadExecutor();
	}

	public void runLater(T newValue) {
		this.futureVal = newValue;
		this.hasTask = true;
		if (future == null) {
			future = executorService.submit(this);
		}
	}
	
	public void tryRunLater(T newValue) {
		this.futureVal = newValue;
		this.hasTask = true;
		if (future == null && (currentVal == null || !currentVal.equals(futureVal))) {
			future = executorService.submit(this);
		}
	}

	public abstract void doTask();

	@Override
	public void run() {
		currentVal = futureVal;
		Platform.runLater(() -> {
			doTask();
		});
		try {
			Thread.sleep(Configurations.viewUpdateInterval);
		} catch (InterruptedException e) {
		}
		terminate();
	}

	protected void terminate() {
		future = null;
		if (currentVal.equals(futureVal)) {
			hasTask = false;
		} else {
			future = executorService.submit(this);
		}
	}
}
