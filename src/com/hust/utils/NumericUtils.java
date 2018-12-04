package com.hust.utils;

import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleMatrix;

import processing.core.PApplet;

public class NumericUtils {
	public static final float PI = (float) Math.PI;
	public static final SimpleMatrix I4X4 = getI(4);

	public static SimpleMatrix getI(int size) {
		SimpleMatrix res = new SimpleMatrix(size, size, MatrixType.FDRM);
		for (int i = 0; i < size; i++) {
			res.set(i, i, 1);
		}
		return res;
	}

	public static SimpleMatrix subMatrix(SimpleMatrix matrix, int startr, int endr, int startc, int endc) {
		SimpleMatrix res = new SimpleMatrix(endr - startr + 1, endc - startc + 1, MatrixType.FDRM);
		for (int i = startr; i <= endr; i++) {
			for (int j = startc; j <= endc; j++) {
				res.set(i, j, matrix.get(i, j));
			}
		}
		return res;
	}

	public static float dist(float[] a, float[] b) {
		return PApplet.dist(a[0], a[1], a[2], b[0], b[1], b[2]);
	}

	public static float[] toArray(SimpleMatrix matrix) {
		float[] res = new float[matrix.getNumElements()];
		int elementsPerCol = matrix.numCols();
		for (int i = 0; i < matrix.numRows(); i++) {
			for (int j = 0; j < elementsPerCol; j++) {
				res[i * elementsPerCol + j] = (float) matrix.get(i, j);
			}
		}
		return res;
	}

	public static float[][] toArrays(SimpleMatrix[] matrix) {
		float[][] res = new float[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			res[i] = toArray(matrix[i]);
		}
		return res;
	}

	public static float mean(float[] vector) {
		return (float) Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2) + Math.pow(vector[2], 2));
	}
	
}
