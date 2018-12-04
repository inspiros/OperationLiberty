package com.hust.robot;

import org.ejml.simple.SimpleMatrix;

import com.hust.utils.NumericUtils;

public class Articulation {
	private int id;
	private String friendlyName;

	private Articulation prevJoint;
	
	private RotationAxis axis;
	private float angle;
	
	private SimpleMatrix rot;
	private SimpleMatrix trans;

	private SimpleMatrix globalRotation;
	private SimpleMatrix globalTransformation;

	public static Articulation getBase() {
		return new Articulation();
	}

	private Articulation() {
		this.friendlyName = "Base";
		this.rot = NumericUtils.I4X4;
		this.trans = NumericUtils.I4X4;
		this.globalRotation = NumericUtils.I4X4;
		this.globalTransformation = NumericUtils.I4X4;
	}

	public Articulation(RotationAxis axis, float angle, float[] nextJointPosition) {
		this.axis = axis;
		this.angle = angle;
		this.rot = ArticulationMatrixCalculator.calculateRot(axis, angle);
		this.trans = ArticulationMatrixCalculator.calculateTrans(nextJointPosition);
	}

	public void setName(String name) {
		this.friendlyName = name;
	}
	
	public String getName() {
		return friendlyName;
	}

	public void setPrevJoint(Articulation articulation) {
		this.prevJoint = articulation;
		updateTransformation();
	}

	public void setDOF(int id) {
		this.id = id;
	}

	public int getDOF() {
		return id;
	}

	public SimpleMatrix getGlobalTransformation() {
		return globalTransformation;
	}

	public SimpleMatrix getTransformation() {
		return rot.mult(trans);
	}

	public SimpleMatrix getRotation() {
		return rot;
	}
	
	public SimpleMatrix getGlobalRotation() {
		return globalRotation;
	}

	public SimpleMatrix getTranslation() {
		return trans;
	}

	public SimpleMatrix getEndPointCoordinate() {
		return NumericUtils.subMatrix(
				globalTransformation.mult(new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 0 }, { 1 } })), 0, 2, 0, 0);
	}

	public SimpleMatrix getStartPointCoordinate() {
		return prevJoint.getEndPointCoordinate();
	}

	public SimpleMatrix getDirection() {
		return NumericUtils.subMatrix(rot.mult(new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 1 }, { 1 } })), 0, 2, 0,
				0);
	}

	public SimpleMatrix getGlobalDirection() {
		return NumericUtils.subMatrix(
				globalRotation.mult(new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 1 }, { 1 } })), 0, 2, 0, 0);
	}

	public boolean hasPrevJoint() {
		if (prevJoint != null) {
			return true;
		}
		return false;
	}

	public Articulation getPrevJoint() {
		return prevJoint;
	}

	public void updateTransformation() {
		if (this.hasPrevJoint()) {
			globalTransformation = prevJoint.getGlobalTransformation().mult(this.getTransformation());
			globalRotation = prevJoint.getGlobalRotation().mult(rot);
		} else {
			globalTransformation = this.getTransformation();
			globalRotation = rot;
		}
	}

	public float getAngle() {
		return angle;
	}
	
	public void updateAngle(float angle) {
		this.angle = angle;
		this.rot = ArticulationMatrixCalculator.calculateRot(this.axis, this.angle);
	}
	
}
