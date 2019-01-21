package com.hust.model.robot.kinematics;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.hust.model.robot.Bone;
import com.hust.model.robot.Chain;
import com.hust.utils.algebra.FloatVector3;
import com.hust.utils.algebra.JacobianMatrix;

public class KinematicsSolver {

	public enum IKMethod {
		/**
		 * Jacobian Transpose
		 */
		JACOBIAN_TRANSPOSE,
		/**
		 * Jacobian Pseudo-inverse
		 */
		JACOBIAN_PSEUDO_INVERSE,
		/**
		 * Gradient Descent
		 */
		GRADIENT_DESCENT,
		/**
		 * Target Triangle
		 */
		TARGET_TRIANGLE,
		/**
		 * Forward and Bacward Reaching
		 */
		FABRIK
	}

	private IKMethod method;

	private FloatVector3 target;

	public static float tolerance = 0.01f;
	public static float minChange = 0.001f;
	public static int maxIterations = 1000;
	public static float samplingDistance = 0.01f;
	public static float maxLearningRate = 1;

	private Chain chain;

	private List<KinematicsConstraint> constraintList = new ArrayList<KinematicsConstraint>();

	/**
	 * Default Constructor.
	 * 
	 * @param chain
	 */
	public KinematicsSolver() {
		// TODO Generated
	}

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
			constraintList.add(KinematicsConstraint.getInstance(chain.bones.get(i)));
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

	/*
	 * Jacobianic Methods
	 */

	// Jacobian tranpose
	private Vector<Float> getTDeltaOrientation() {
		JacobianMatrix j = new JacobianMatrix();

		FloatVector3 e = chain.getEndEffector();
		for (int i = 0; i < chain.getDof(); i++) {
			Bone b = chain.bones.get(i);
			j.addColumn(b.getGlobalRotationAxis(), e, b.getStartPoint());
		}
		FloatVector3 v = target.sub(e);
		return j.mul(v);
	}

	private float[] solveJacobianTransposeIK() {
		chain.lock();
		int iterationCount = 0;
		float learningRate = maxLearningRate / 100;
		float distance = distanceFromTarget();
		while (iterationCount < maxIterations) {
			Vector<Float> deltaO = getTDeltaOrientation();
			for (int i = 0; i < chain.getDof(); i++) {
				chain.updateAngleDegs(i, deltaO.get(i) * learningRate);
			}
			iterationCount++;
			if (iterationCount % 100 == 0) {
				learningRate *= 0.8;
			}
			if (distance <= tolerance) {
				break;
			} else {
				distance = distanceFromTarget();
			}
		}
		float[] newAngles = chain.getAnglesDegs();
		chain.unlock();
		System.out.println(iterationCount);
		return newAngles;
	}

	// Jacobian pseudo-inverse
	private Vector<Float> getPIDeltaOrientation() {
		JacobianMatrix j = new JacobianMatrix();

		FloatVector3 e = chain.getEndEffector();
		for (int i = 0; i < chain.getDof(); i++) {
			Bone b = chain.bones.get(i);
			j.addColumn(b.getGlobalRotationAxis(), e, b.getStartPoint());
		}
		FloatVector3 v = target.sub(e);
		return j.pseudoInverseMul(v);
	}

	private float[] solveJacobianPseudoInverseIK() {
		chain.lock();
		int iterationCount = 0;
		float learningRate = maxLearningRate * 10;
		float distance = distanceFromTarget();
		while (iterationCount < maxIterations) {
			Vector<Float> deltaO = getPIDeltaOrientation();
			for (int i = 0; i < chain.getDof(); i++) {
				chain.updateAngleDegs(i, deltaO.get(i) * learningRate);
			}
			iterationCount++;
			if (iterationCount % 100 == 0) {
				learningRate *= 0.8;
			}
			if (distance <= tolerance) {
				break;
			} else {
				distance = distanceFromTarget();
			}
		}
		float[] newAngles = chain.getAnglesDegs();
		chain.unlock();
		System.out.println(iterationCount);
		return newAngles;
	}

	/*
	 * Target Triangle
	 */
	private float getDeltaTheta(int id) {
		Bone b = chain.bones.get(id);
		FloatVector3 rotAxis = b.getGlobalRotationAxis();
		FloatVector3 sp = b.getStartPoint();
		FloatVector3 ve = chain.getEndEffector().sub(sp).projectOntoPlane(rotAxis);
		FloatVector3 vt = target.sub(sp).projectOntoPlane(rotAxis);
		float res = FloatVector3.getSignedAngleBetweenDegs(ve, vt, rotAxis);
		return res;
	}

	private float[] solveTargetTriangleIK() {
		chain.lock();
		int iterationCount = 0;
		float distance = distanceFromTarget();
		while (iterationCount < maxIterations) {

			for (int i = chain.getDof() - 1; i >= 0; i--) {
				float deltaO = getDeltaTheta(i);
				chain.updateAngleDegs(i, deltaO);
			}
			iterationCount++;
			if (distance <= tolerance) {
				break;
			} else {
				distance = distanceFromTarget();
			}
		}
		float[] newAngles = chain.getAnglesDegs();
		chain.unlock();
		System.out.println(iterationCount);
		return newAngles;
	}

	/*
	 * Gradient Descent
	 */
	private float getPartialGradient(int id) {
		float fx = constraintList.get(id).targetFunction();
		chain.updateAngleDegs(id, samplingDistance);
		float fxPlusDx = constraintList.get(id).targetFunction();
		float partialGradient = (fxPlusDx - fx) / samplingDistance;
		chain.updateAngleDegs(id, -samplingDistance);
		return partialGradient;
	}

	private float[] solveGradientDescentIK() {
		chain.lock();
		int iterationCount = 0;
		float distance = distanceFromTarget();
//		if (distance <= tolerance) {
//			return chain.getAnglesDegs();
//		}
		float learningRate = maxLearningRate * 30;
		while (iterationCount < maxIterations) {
			for (int i = 0; i < chain.getDof(); i++) {
				float gradient = getPartialGradient(i);
				chain.updateAngleDegs(i, -learningRate * gradient);
			}
			// distance = distanceFromTarget();
			iterationCount++;
			if (iterationCount % 30 == 0) {
				learningRate *= 0.1;
			}
			if (distance <= tolerance) {
				break;
			} else {
				distance = distanceFromTarget();
			}
		}
		// System.out.println(iterationCount);
		float[] newAngles = chain.getAnglesDegs();
		chain.unlock();
		System.out.println(iterationCount);
		return newAngles;
	}

	public float[] solveIK() {

		switch (method) {
		case JACOBIAN_TRANSPOSE:
			return solveJacobianTransposeIK();
		case JACOBIAN_PSEUDO_INVERSE:
			return solveJacobianPseudoInverseIK();
		case GRADIENT_DESCENT:
			return solveGradientDescentIK();
		case TARGET_TRIANGLE:
			return solveTargetTriangleIK();
		case FABRIK:
			System.err.println("Unimplemented!");
		}
		return null;
	}

}