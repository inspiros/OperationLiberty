package com.hust.robot;

import java.util.ArrayList;
import java.util.List;

public abstract class DataChanger<T> implements Lockable {
	protected boolean locked;

	private List<DataChangeListener<T>> changeListeners = new ArrayList<DataChangeListener<T>>();
	private List<DataChangeListener<T>> amountChangeListeners = new ArrayList<DataChangeListener<T>>();

	protected void addDataChangeListener(DataChangeListener<T> angleChangeListener) {
		changeListeners.add(angleChangeListener);
	}

	protected void addDataAmountChangeListener(DataChangeListener<T> angleChangeListener) {
		amountChangeListeners.add(angleChangeListener);
	}

	public boolean isLocked() {
		return locked;
	}

	public abstract void lock();

	public abstract void unlock();

	protected void changeData(int id) {
//		if (locked) {
//			return;
//		}
		for (DataChangeListener<T> dataChangeListener : changeListeners) {
			dataChangeListener.dataChanged(id);
		}
	}

	protected void changeDataTo(int id, T value) {
		changeData(id);
		if (locked) {
			return;
		}
		for (DataChangeListener<T> dataAmountChangeListener : amountChangeListeners) {
			dataAmountChangeListener.dataChangedTo(id, value);
		}
	}
}
