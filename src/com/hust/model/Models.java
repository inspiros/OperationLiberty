package com.hust.model;

import com.hust.model.data.DataSeries;
import com.hust.model.data.DataVault;
import com.hust.model.robot.Chain;
import com.hust.model.robot.kinematics.KinematicsSolver;
import com.hust.model.robot.kinematics.KinematicsSolver.IKMethod;
import com.hust.utils.algebra.FloatVector3;

/**
 * A buffer class for storing data and communicating with other classes.
 * 
 * @author Inspiros
 *
 */
public class Models {

	public Chain robot;

	public KinematicsSolver kinematicsSolver;

	public DataVault vault;

	public Models() {
		vault = new DataVault();
		kinematicsSolver = new KinematicsSolver();
	}

	public Models setupModel() {

		robot = new Chain();
		robot.addConsecutiveBone(new FloatVector3(0, 0, 50), FloatVector3.Z_AXIS, 0, -120, 120);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.Y_AXIS, 45, -120, 120);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.X_AXIS, 0, -120, 120);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 30), FloatVector3.Y_AXIS, 45, -120, 120);
		// robot.addConsecutiveBone(new FloatVector3(0, 0, 20), FloatVector3.Y_AXIS, 0,
		// -120, 120);

		for (int i = 0; i < getDof(); i++) {
			vault.dataSeries.add(new DataSeries());
			vault.dataSeries.get(i).registerSeries("temperature");
		}

		kinematicsSolver.setChain(robot);

		return this;
	}

	public Chain getArm() {
		return robot;
	}

	public DataVault getVault() {
		return vault;
	}

	public int getDof() {
		return robot.getDof();
	}

	public float[] getAnglesDegs() {
		return robot.getAnglesDegs();
	}

	public float getAngleDegs(int i) {
		return robot.getAngleDegs(i);
	}

	public void setTargetDegs(float... anglesDegs) {
		robot.setTargetsDegs(anglesDegs);
	}

	public void setTargetDegs(int id, float angleDegs) {
		robot.setTargetDegs(id, angleDegs);
	}

	public void moveToPosition(FloatVector3 newPosition, KinematicsSolver.IKMethod ikAlgorithm) {
		kinematicsSolver.setMethod(ikAlgorithm);
		kinematicsSolver.setTarget(newPosition);

		if (ikAlgorithm == IKMethod.GRADIENT_DESCENT) {
			for (int i = 0; i < robot.bones.size(); i++) {
				kinematicsSolver.getConstraint(i).toEndEffectorDistanceConstraint(newPosition);
			}
//			for (int i = robot.bones.size() - 1; i < robot.bones.size(); i++) {
//				kinematicsSolver.getConstraint(i).toEndEffectorDirectionConstraint(FloatVector3.Z_AXIS.negated());
//			}
		}

		float[] newAngles = kinematicsSolver.solveIK();

		robot.prepareTargetsDegs(newAngles);
		robot.actuate();
	}

}
