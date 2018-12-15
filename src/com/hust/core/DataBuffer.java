package com.hust.core;

import com.hust.robot.Chain;
import com.hust.robot.KinematicsSolver;
import com.hust.utils.FloatVector3;

/**
 * A buffer class for storing data and communicating with other classes.
 * 
 * @author Inspiros
 *
 */
public class DataBuffer {

	public static final int SLEEP_TIME = 25;

	private Chain robot;

	private KinematicsSolver kinematicsSolver;

	private int dof;
	private float[] angles;

	public boolean limitSpeed = true;

	public static DataBuffer setupModel() {
		DataBuffer res = new DataBuffer();

		res.robot = new Chain();
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 50), FloatVector3.Z_AXIS, 0, -180, 180);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.Y_AXIS, 45, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 40), FloatVector3.Y_AXIS, 45, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 30), FloatVector3.Y_AXIS, 0, -120, 120);
		res.robot.addConsecutiveBone(new FloatVector3(0, 0, 20), FloatVector3.Y_AXIS, 0, -120, 120);

		res.kinematicsSolver = new KinematicsSolver();
		res.kinematicsSolver.setChain(res.robot);

		res.dof = res.robot.getDof();
		res.angles = res.robot.getAnglesDegs();

		return res;
	}

	private DataBuffer() {
		// A fucking PRIVATE Constructer
	}

	public Chain getArm() {
		return robot;
	}

	public int getDof() {
		return dof;
	}

	public float[] getAnglesDegs() {
		return angles;
	}

	public float getAngleDegs(int i) {
		return angles[i];
	}

	public void setAnglesDegs(float... anglesDegs) {
		robot.setAnglesDegs(this.angles);
	}

	public void setAngleDegs(int id, float angleDegs) {
		if (limitSpeed) {
			robot.setTargetDegs(id, angleDegs);
		} else {

		}
	}

	public void updateAnglesDegs(float... changesDegs) {
		robot.updateAnglesDegs(changesDegs);
	}

	public void updateAngleDegs(int id, float changeDegs) {
		robot.updateAngleDegs(id, changeDegs);
	}

	public void setTargets(float... targets) {
	}

	public void setTarget(int id, float target) {
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
		kinematicsSolver.solveIK();
	}

}
