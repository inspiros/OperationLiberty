package com.hust.robot;

public interface DataChangeListener<T> {

	public abstract void dataChanged(int id);

	public abstract void dataChangedTo(int id, T value);
}
