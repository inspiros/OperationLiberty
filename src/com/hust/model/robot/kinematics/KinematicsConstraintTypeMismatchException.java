package com.hust.model.robot.kinematics;

public class KinematicsConstraintTypeMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1394254325177035069L;

	/**
	 * {@inheritDoc}
	 */
	public KinematicsConstraintTypeMismatchException(String arg0) {
		super(arg0);
	}
	
	public KinematicsConstraintTypeMismatchException(KinematicsConstraint kinematicsConstraint) {
		super(kinematicsConstraint.getConstraintType().name());
	}
}
