package com.hust.utils;

public class RangeLimiter {
	private float lower;
	private float upper;

	public RangeLimiter(float lower, float upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public boolean contains(float number) {
		if (number < lower || number > upper) {
			return false;
		}
		return true;
	}

	public float limits(float number, float change) {
		float res = number + change;
		if (res < lower) {
			res = lower;
		} else if (res > upper) {
			res = upper;
		}
		return res;
	}
	
	public float[] getBounds() {
		return new float[] {lower, upper};
	}
}
