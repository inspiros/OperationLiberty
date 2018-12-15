package com.hust.utils;

public class ArrayUtils {

	public static void print(float... arr) {
		System.out.print("{" + arr[0]);
		for (int i = 1; i < arr.length; i++) {
			System.out.print(", " + arr[i]);
		}
		System.out.println("}");
	}

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
		for (int i = 0; i < arr.length; i++) {
			res[i] *= r;
		}
		return res;
	}

	public static float[] arrayAdd(float[] arr1, float[] arr2) {
		float[] res = arr1.clone();
		for (int i = 0; i < arr1.length; i++) {
			res[i] += arr2[i];
		}
		return res;
	}

	public static float dist(float[] a, float[] b) {
		return Math.abs(
				FloatVector3.distanceBetween(new FloatVector3(a[0], a[1], a[2]), new FloatVector3(b[0], b[1], b[2])));
	}

	public static float mean(float[] vector) {
		return (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
	}

	public static String toString(float[] arr) {
		String res = "";
		for (int i = 0; i < arr.length - 1; i++) {
			res.concat(Float.toString(arr[i]));
			res.concat("\t");
		}
		res.concat(Float.toString(arr[arr.length - 1]));
		return res;
	}
}
