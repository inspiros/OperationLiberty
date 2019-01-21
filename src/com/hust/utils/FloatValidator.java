package com.hust.utils;

public class FloatValidator {

	public static boolean validate(Float f) {
		if (f != null && f != Float.NaN) {
			return true;
		}
		return false;
	}

	public static boolean validate(float f) {
		if (f != Float.NaN) {
			return true;
		}
		return false;
	}
}
