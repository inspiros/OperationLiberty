package com.hust.utils.concurrent;

import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FunnelService implements Callable<Void> {

	CopyOnWriteArrayList<Runnable> scheduledCommands = new CopyOnWriteArrayList<>();
	ConcurrentLinkedQueue<Runnable> urgentCommands = new ConcurrentLinkedQueue<>();

	ListIterator<Runnable> scheduledCommandsIterator;

	BinarySemaphore useSemaphore = new BinarySemaphore(false);
	BinarySemaphore scheduledSemaphore = new BinarySemaphore(false);

	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
	ScheduledFuture<?> scheduledFuture;

	public FunnelService() {
		scheduledCommandsIterator = scheduledCommands.listIterator();
		scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
			scheduledSemaphore.release();
			useSemaphore.release();
		}, 10000, 15000, TimeUnit.MILLISECONDS);

		new Thread(new FutureTask<Void>(this), getClass().getSimpleName()).start();
	}

	public void reschedule(long period, TimeUnit unit) {
		scheduledFuture.cancel(true);
		scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
			scheduledSemaphore.release();
			useSemaphore.release();
		}, 0, period, unit);
	}

	public void runScheduled(Runnable command) {
		scheduledCommands.add(command);
		scheduledCommandsIterator = scheduledCommands.listIterator(scheduledCommandsIterator.nextIndex());
	}

	public void runLater(Runnable command) {
		urgentCommands.add(command);
		useSemaphore.release();
	}

	@Override
	public Void call() throws InterruptedException {
		while (true) {
			useSemaphore.acquire();

			if (!urgentCommands.isEmpty()) {
				urgentCommands.poll().run();
			} else if (!scheduledCommands.isEmpty()) {
				if (scheduledCommandsIterator.nextIndex() == 0) {
					if (scheduledSemaphore.tryAcquire()) {
						scheduledCommandsIterator.next().run();
						// TODO handle
						Thread.sleep(10);
						if (!scheduledCommandsIterator.hasNext()) {
							scheduledCommandsIterator = scheduledCommands.listIterator();
						} else {
							scheduledSemaphore.release();
						}
					}
				} else {
					scheduledCommandsIterator.next().run();
					// TODO handle
					Thread.sleep(10);
					if (!scheduledCommandsIterator.hasNext()) {
						scheduledCommandsIterator = scheduledCommands.listIterator();
					} else {
						scheduledSemaphore.release();
					}
				}
			}

			if (!urgentCommands.isEmpty() || scheduledCommandsIterator.nextIndex() != 0) {
				useSemaphore.release();
			}
		}
	}

}
