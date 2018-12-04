package com.hust.utils;

public class ArrayUtils {
	
	public static float[] fill(float[] arr, float toFill) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = toFill;
		}
		return arr;
	}

	public static float[] fill(int size, float toFill) {
		float[] arr = new float[size];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = toFill;
		}
		return arr;
	}

	public static float[] arrayMult(float[] arr, float r) {
		float[] res = arr.clone();
		for(int i = 0; i < arr.length; i++) {
			res[i] *= r;
		}
		return res;
	}
	
	public static float[] arrayAdd(float[] arr1, float[] arr2) {
		float[] res = arr1.clone();
		for(int i = 0; i < arr1.length; i++) {
			res[i] += arr2[i];
		}
		return res;
	}
}
