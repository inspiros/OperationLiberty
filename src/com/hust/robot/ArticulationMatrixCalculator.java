package com.hust.robot;

import org.ejml.simple.SimpleMatrix;

public class ArticulationMatrixCalculator {

	public static SimpleMatrix calculateRot(RotationAxis axis, float angle) {
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		switch (axis) {
		case Y:
			return new SimpleMatrix(
					new float[][] { { cos, 0, sin, 0 }, { 0, 1, 0, 0 }, { -sin, 0, cos, 0 }, { 0, 0, 0, 1 } });
		case Z:
			return new SimpleMatrix(
					new float[][] { { cos, -sin, 0, 0 }, { sin, cos, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
		case X:
			return new SimpleMatrix(
					new float[][] { { 1, 0, 0, 0 }, { 0, cos, -sin, 0 }, { 0, sin, cos, 0 }, { 0, 0, 0, 1 } });
		}
		return null;
	}

	public static SimpleMatrix calculateTrans(float[] endPoint) {
		return new SimpleMatrix(new float[][] { { 1, 0, 0, endPoint[0] }, { 0, 1, 0, endPoint[1] },
				{ 0, 0, 1, endPoint[2] }, { 0, 0, 0, 1 } });
	}

	public static SimpleMatrix calculateTransformation(RotationAxis axis, float angle, float[] endPoint) {
		return calculateRot(axis, angle).mult(calculateTrans(endPoint));
	}

	public static float calculateAngle(SimpleMatrix rot) {
		float[] u = new float[3];
		u[0] = (float) rot.get(2, 1) - (float) rot.get(1, 2);
		u[1] = (float) rot.get(0, 2) - (float) rot.get(2, 0);
		u[2] = (float) rot.get(1, 0) - (float) rot.get(0, 1);
		return (float) Math.asin(Math.abs(Math.sqrt(Math.pow(u[0], 2) + Math.pow(u[1], 2) + Math.pow(u[2], 2))) / 2);
	}
}
