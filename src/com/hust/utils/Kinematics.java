package com.hust.utils;

import org.ejml.simple.SimpleMatrix;

import com.hust.robot.ArticulationMatrixCalculator;
import com.hust.robot.RotationAxis;

import processing.core.PApplet;

public class Kinematics {

	public static final int JACOBIAN = 0;
	public static final int GRADIENT_DESCENT = 1;
	public static final int FABRIK = 2;

	public static float tolerance = 0.1f;
	public static float samplingDistance = 1;
	public static float learningRate = 1;

	public static SimpleMatrix forwardKinematics(RotationAxis[] axis, float[] angles, float[][] endPoints) {
		SimpleMatrix res = NumericUtils.I4X4;
		for (int i = 0; i < angles.length; i++) {
			res = res.mult(ArticulationMatrixCalculator.calculateTransformation(axis[i],
					(float) Math.toRadians(angles[i]), endPoints[i]));
		}
		return NumericUtils.subMatrix(res.mult(new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 0 }, { 1 } })), 0, 2, 0,
				0);
	}

	public static SimpleMatrix[] totalForwardKinematics(RotationAxis[] axis, float[] angles, float[][] endPoints) {
		SimpleMatrix[] res = new SimpleMatrix[angles.length + 1];
		res[0] = new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 0 } });
		SimpleMatrix temp = NumericUtils.I4X4;
		for (int i = 0; i < angles.length; i++) {
			temp = temp.mult(ArticulationMatrixCalculator.calculateTransformation(axis[i],
					(float) Math.toRadians(angles[i]), endPoints[i]));
			res[i + 1] = NumericUtils
					.subMatrix(temp.mult(new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 0 }, { 1 } })), 0, 2, 0, 0);
		}

		return res;
	}

	public static float distanceFromTarget(RotationAxis[] axis, float[] angles, float[][] endPoints, float[] target) {
		SimpleMatrix currentPos = forwardKinematics(axis, angles, endPoints);
		return PApplet.dist((float) currentPos.get(0, 0), (float) currentPos.get(1, 0), (float) currentPos.get(2, 0),
				target[0], target[1], target[2]);
	}

	public static float partialGradient(RotationAxis[] axis, float[] angles, float[][] endPoints, float[] target,
			int id) {
		float[] temp = angles.clone();
		float fx = distanceFromTarget(axis, temp, endPoints, target);
		temp[id] += samplingDistance;
		float fxPlusDx = distanceFromTarget(axis, temp, endPoints, target);
		float partialGradient = (fxPlusDx - fx) / samplingDistance;
		return partialGradient;
	}

	public static float[] gradientDescentIK(RotationAxis[] axis, float[] angles, RangeLimiter[] bounds,
			float[][] endPoints, float[] target) {
		float[] res = angles.clone();
		for (int i = 0; i < angles.length; i++) {
			float gradient = partialGradient(axis, res, endPoints, target, i);
			res[i] -= learningRate * gradient;
			// res[i] = bounds[i].limits(res[i], -learningRate * gradient);
		}
		return res;
	}

	public static float[] fabrIK(RotationAxis[] axis, float[] angles, RangeLimiter[] bounds, float[][] endPoints,
			float[] target) {
		float[] res = angles.clone();// new float[angles.length];
		int numPoints = angles.length + 1;
		float[][] origins = NumericUtils.toArrays(totalForwardKinematics(axis, angles, endPoints));
		float[] base = origins[0];

		float distance = NumericUtils.dist(origins[numPoints - 1], target);
		System.out.println(distance);
		while (distance > tolerance) {

			// Forward Reaching
			origins[numPoints - 1] = target.clone();
			for (int i = numPoints - 2; i > -1; i--) {
				float r = NumericUtils.mean(endPoints[i]) / Math.abs(NumericUtils.dist(origins[i], origins[i + 1]));
				origins[i] = ArrayUtils.arrayAdd(ArrayUtils.arrayMult(origins[i + 1], 1 - r),
						ArrayUtils.arrayMult(origins[i], r));
			}
			// Backward Reaching
			origins[0] = base.clone();
			for (int i = 0; i < numPoints - 1; i++) {
				float r = NumericUtils.mean(endPoints[i]) / Math.abs(NumericUtils.dist(origins[i], origins[i + 1]));
				origins[i + 1] = ArrayUtils.arrayAdd(ArrayUtils.arrayMult(origins[i], 1 - r),
						ArrayUtils.arrayMult(origins[i + 1], r));
			}
			distance = NumericUtils.dist(origins[numPoints - 1], target);
			System.out.println(distance);
		}

		// TODO Check
		for (int i = 0; i < numPoints; i++) {
			System.out.println(origins[i][0] + " " + origins[i][1] + " " + origins[i][2]);
			if (i > 0) {
				System.out.println(PApplet.dist(origins[i][0], origins[i][1], origins[i][2], origins[i - 1][0],
						origins[i - 1][1], origins[i - 1][2]));
			}
		}

		return res;
	}
}
