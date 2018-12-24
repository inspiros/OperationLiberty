package com.hust.robot;

import java.util.ArrayList;
import java.util.List;

import com.hust.utils.data.FloatVector3;

public class KinematicsSolver {

	public enum IKMethod {
		/**
		 * Jacobian
		 */
		JACOBIAN,
		/**
		 * Gradient Descent
		 */
		GRADIENT_DESCENT,
		/**
		 * Forward and Bacward Reaching
		 */
		FABRIK
	}

	private IKMethod method;

	private FloatVector3 target;

	public static float tolerance = 0.01f;
	public static float minChange = 0.001f;
	public static int maxIterations = 10000;
	public static float samplingDistance = 0.01f;
	public static float maxLearningRate = 1;

	private Chain chain;

	private List<KinematicsConstraint> constraintList = new ArrayList<KinematicsConstraint>();

	public IKMethod getMethod() {
		return method;
	}

	public void setMethod(IKMethod method) {
		this.method = method;
	}

	public FloatVector3 getTarget() {
		return target;
	}

	public void setTarget(FloatVector3 target) {
		this.target = target;
	}

	public List<KinematicsConstraint> getConstraints() {
		return constraintList;
	}

	public KinematicsConstraint getConstraint(int i) {
		return constraintList.get(i);
	}

	public Chain getChain() {
		return chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
		for (int i = 0; i < chain.getDof(); i++) {
			constraintList.add(KinematicsConstraint.getInstance(chain.getBone(i)));
		}
	}

	public FloatVector3 forwardKinematics() {
		chain.recalculateTransformations();
		return chain.getEndEffector();
	}

	public FloatVector3 forwardKinematics(float... anglesDegs) {
		chain.lock();
		chain.updateAnglesDegs(anglesDegs);
		FloatVector3 fkVector = forwardKinematics();
		chain.unlock();
		return fkVector;
	}

	public FloatVector3[] totalForwardKinematics() {
		chain.recalculateTransformations();
		FloatVector3[] endPoints = chain.getEndPoints();
		return endPoints;
	}

	public float distanceFromTarget() {
		return FloatVector3.distanceBetween(chain.getEndEffector(), target);
	}

	private float partialGradient(int id) {
		float fx = constraintList.get(id).targetFunction();
		chain.updateAngleDegs(id, samplingDistance);
		float fxPlusDx = constraintList.get(id).targetFunction();
		float partialGradient = (fxPlusDx - fx) / samplingDistance;
		chain.updateAngleDegs(id, -samplingDistance);
		return partialGradient;
	}

	public float[] solveGDIK() {
		chain.lock();
		float[] newAngles = chain.getAnglesDegs();
		int dof = chain.getDof();
		int iterationCount = 0;
//		float distance = distanceFromTarget();
//		if (distance <= tolerance) {
//			return chain.getAnglesDegs();
//		}
		float learningRate = maxLearningRate;
		while (iterationCount < maxIterations) {
			for (int i = 0; i < dof; i++) {
				float gradient = partialGradient(i);
				chain.updateAngleDegs(i, -learningRate * gradient);
			}
			// distance = distanceFromTarget();
			iterationCount++;
			if (iterationCount % 100 == 0) {
				learningRate *= 0.8;
			}
		}
		// System.out.println(iterationCount);
		newAngles = chain.getAnglesDegs();
		chain.unlock();
		chain.setTargetsDegs(newAngles);
		return newAngles;
	}

	public float[] solveIK() {
		switch (method) {
		case JACOBIAN:
			System.err.println("Unimplemented!");
		case GRADIENT_DESCENT:
			return solveGDIK();
		case FABRIK:
			System.err.println("Unimplemented!");
		}
		return null;
	}

//	public static float[] fabrIK(FloatVector3[] axis, float[] angles, RangeLimiter[] bounds, FloatVector3[] endPoints,
//			FloatVector3 target) {
//		float[] res = angles.clone();
//		int numPoints = angles.length + 1;
//		FloatVector3[] origins = totalForwardKinematics(axis, angles, endPoints);
//		FloatVector3 base = origins[0];
//
//		float distance = FloatVector3.distanceBetween(origins[numPoints - 1], target);
//		System.out.println(distance);
//		while (distance > tolerance) {
//
//			// Forward Reaching
//			origins[numPoints - 1] = FloatVector3.clone(target);
//			for (int i = numPoints - 2; i > -1; i--) {
//				float r = endPoints[i].length() / FloatVector3.distanceBetween(origins[i], origins[i + 1]);
//				origins[i] = origins[i + 1].mul(1 - r).add(origins[i].mul(r));
//			}
//			// Backward Reaching
//			origins[0] = FloatVector3.clone(base);
//			for (int i = 0; i < numPoints - 1; i++) {
//				float r = endPoints[i].length() / FloatVector3.distanceBetween(origins[i], origins[i + 1]);
//				origins[i + 1] = (origins[i].mul(1 - r).add(origins[i + 1].mul(r)));
//			}
//			distance = FloatVector3.distanceBetween(origins[numPoints - 1], target);
//			System.out.println(distance);
//		}
//
//		// TODO Check
//		for (int i = 0; i < numPoints; i++) {
//			origins[i].print();
//			if (i > 0) {
//				System.out.println(FloatVector3.distanceBetween(origins[i], origins[i - 1]));
//			}
//		}
//
//		return res;
//	}
}
