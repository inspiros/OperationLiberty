package com.hust.model;

import java.util.ArrayList;

import com.hust.core.Configurations;
import com.hust.model.data.DataSeries;
import com.hust.model.data.DataVault;
import com.hust.model.robot.Chain;
import com.hust.model.robot.kinematics.KinematicsSolver;
import com.hust.model.robot.kinematics.KinematicsSolver.IKMethod;
import com.hust.model.robot.process.Processes;
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
	
	public Processes process;

	public DataVault vault;

	public Models() {
		vault = new DataVault();
		kinematicsSolver = new KinematicsSolver();
		process = new Processes();
		Configurations.MODULES_INITIALIZATION.get("model.unulled").countDown();
	}

	public Models setupModel() {

		robot = new Chain();
		robot.addConsecutiveBone(new FloatVector3(0, 0, 50), FloatVector3.Z_AXIS, 0, -40, 120);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 120), FloatVector3.Y_AXIS, 0, -30, 90);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 100), FloatVector3.Y_AXIS, 0, -30, 90);
		robot.addConsecutiveBone(new FloatVector3(0, 0, 100), FloatVector3.Y_AXIS, 0, -30, 90);

		for (int i = 0; i < getDof(); i++) {
			vault.dataSeries.add(new DataSeries());
			vault.dataSeries.get(i).registerSeries("temperature");
		}

		kinematicsSolver.setChain(robot);

		Configurations.MODULES_INITIALIZATION.get("model").countDown();
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

	public void setTargetsDegs(float... anglesDegs) {
		robot.setTargetsDegs(anglesDegs);
	}

	public void setTargetDegs(int id, float angleDegs) {
		robot.setTargetDegs(id, angleDegs);
	}

	public void setTargetsRads(ArrayList<Float> anglesRads) {
		robot.setTargetsDegs(anglesRads);
	}

	public void setTargetsDegs(ArrayList<Float> anglesDegs) {
		robot.setTargetsDegs(anglesDegs);
	}

	public void prepareTargetsRads(float... anglesRads) {
		robot.setTargetsRads(anglesRads);
	}

	public void prepareTargetsDegs(float... anglesDegs) {
		robot.setTargetsDegs(anglesDegs);
	}

	public void prepareTargetsRads(ArrayList<Float> anglesRads) {
		robot.setTargetsRads(anglesRads);
	}

	public void prepareTargetsDegs(ArrayList<Float> anglesDegs) {
		robot.setTargetsDegs(anglesDegs);
	}

	public void actuate() {
		robot.actuate();
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
