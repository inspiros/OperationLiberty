package com.hust.utils.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class BinarySemaphore extends Semaphore {

	private static final long serialVersionUID = -927596707339500451L;

	private final Object lock = new Object();

	/**
	 * Creates a {@code Semaphore} with the given number of permits between 0 and 1,
	 * and the given fairness setting.
	 *
	 * @param startReleased <code>true</code> if this semaphore starts with 1 permit
	 *                      or <code>false</code> to start with 0 permits.
	 * @param fairMode      {@code true} if this semaphore will guarantee first-in
	 *                      first-out granting of permits under contention, else
	 *                      {@code false}
	 */
	public BinarySemaphore(boolean startReleased, boolean fairMode) {
		super((startReleased ? 1 : 0), fairMode);
	}

	/**
	 * Creates a {@code Semaphore} with the given number of permits between 0 and 1,
	 * and the given fairness as false.
	 *
	 * @param startReleased <code>true</code> if this semaphore starts with 1 permit
	 *                      or <code>false</code> to start with 0 permits.
	 */
	public BinarySemaphore(boolean startReleased) {
		super(startReleased ? 1 : 0);
	}

	@Override
	public void acquire(int permits) throws InterruptedException {
		if (permits > 1)
			throw new UnsupportedOperationException("Cannot acquire more than one permit!");
		else
			super.acquire(permits);
	}

	@Override
	public void acquireUninterruptibly(int permits) {
		if (permits > 1)
			throw new UnsupportedOperationException("Cannot acquire more than one permit!");
		else
			super.acquireUninterruptibly(permits);
	}

	@Override
	public void release() {
		synchronized (lock) {
			if (this.availablePermits() == 0)
				super.release();
		}
	}

	@Override
	public void release(int permits) {
		if (permits > 1)
			throw new UnsupportedOperationException("Cannot release more than one permit!");
		else
			this.release();
	}

	@Override
	public boolean tryAcquire(int permits) {
		if (permits > 1)
			throw new UnsupportedOperationException("Cannot acquire more than one permit!");
		else
			return super.tryAcquire(permits);
	}

	@Override
	public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
		if (permits > 1)
			throw new UnsupportedOperationException("Cannot release more than one permit!");
		else
			return super.tryAcquire(permits, timeout, unit);
	}

	public boolean available() {
		return availablePermits() == 1 ? true : false;
	}
}
