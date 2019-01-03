package com.hust.core;

import com.hust.model.robot.Chain;
import com.hust.model.robot.kinematics.KinematicsSolver;
import com.hust.utils.data.FloatVector3;

/**
 * A buffer class for storing data and communicating with other classes.
 * 
 * @author Inspiros
 *
 */
public class Models {

	private Chain robot;

	private KinematicsSolver kinematicsSolver;

	public static Models setupModel() {
		Models res = new Models();

		res.robot = new Chain();
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 50), FloatVector3.Z_AXIS, 0, -180, 180);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.Y_AXIS, 45, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.X_AXIS, 0, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 30), FloatVector3.Y_AXIS, 45, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 20), FloatVector3.Y_AXIS, 0, -120, 120);

		res.kinematicsSolver.setChain(res.robot);

		return res;
	}

	private Models() {
		kinematicsSolver = new KinematicsSolver();
	}

	public Chain getArm() {
		return robot;
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

		for (int i = 0; i < 4; i++) {
			kinematicsSolver.getConstraint(i).toEndEffectorDistanceConstraint(newPosition);
		}
		for (int i = 4; i < 5; i++) {
			kinematicsSolver.getConstraint(i).toEndEffectorDirectionConstraint(FloatVector3.Z_AXIS.negated());
		}

		float[] newAngles = kinematicsSolver.solveIK();

		robot.prepareTargetsDegs(newAngles);
		robot.actuate();
	}

}