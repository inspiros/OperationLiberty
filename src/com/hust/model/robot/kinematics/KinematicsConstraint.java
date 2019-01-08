package com.hust.model.robot.kinematics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.hust.model.robot.Bone;
import com.hust.utils.algebra.FloatVector3;

public class KinematicsConstraint {

	public enum ConstraintType {
		/**
		 * No restriction, resulting in no angle change
		 */
		NONE,
		/**
		 * Minimizing distance from end effector to a desired point in 3D
		 */
		END_EFFECTOR_DISTANCE,
		/**
		 * Minimizing distance from end point of a bone to a desired point in 3D
		 */
		DISTANCE,
		/**
		 * Minimizing angle between direction vector of end effector to a desired vector
		 */
		END_EFFECTOR_DIRECTION,
		/**
		 * Minimizing angle between direction vector of a bone to a desired vector
		 */
		DIRECTION,
		/**
		 * Minimizing angle between direction vector of a bone to a desired line
		 */
		LINEAR,
		/**
		 * Minimizing angle between direction vector of a bone to a desired plane
		 */
		PLANAR,
		/**
		 * Minimizing difference between the angle of a bone to a desired angle
		 */
		ANGLE,
		/**
		 * User defined constraint type, requires a target function
		 */
		CUSTOM
	}

	private ConstraintType constraintType;

	/**
	 * Static function
	 */
	private Method targetFunction;
	private Object target;

	private Bone bone;

	private KinematicsConstraint() {
		this.constraintType = ConstraintType.NONE;
	}

	private KinematicsConstraint(ConstraintType constraintType) {
		this.constraintType = constraintType;
	}

	/**
	 * Default instance of END_EFFECTOR_DISTANCE constraint
	 * 
	 * @return
	 */
	public static KinematicsConstraint getInstance() {
		return new KinematicsConstraint();
	}

	public static KinematicsConstraint getInstance(Bone bone) {
		KinematicsConstraint res = new KinematicsConstraint();
		res.bone = bone;
		return res;
	}

	/**
	 * Change this instance to end effector distance constraint
	 * 
	 * @param endEffectorTargetLocation
	 */
	public void toEndEffectorDistanceConstraint(FloatVector3 endEffectorTargetLocation) {
		this.constraintType = ConstraintType.END_EFFECTOR_DISTANCE;
		this.target = endEffectorTargetLocation;
	}

	/**
	 * Change this instance to distance constraint
	 * 
	 * @param targetLocation
	 */
	public void toDistanceConstraint(FloatVector3 targetLocation) {
		this.constraintType = ConstraintType.DISTANCE;
		this.target = targetLocation;
	}

	/**
	 * Change this instance to end effector direction constraint
	 * 
	 * @param endEffectorDirection
	 */
	public void toEndEffectorDirectionConstraint(FloatVector3 endEffectorDirection) {
		this.constraintType = ConstraintType.END_EFFECTOR_DIRECTION;
		this.target = endEffectorDirection;
	}

	/**
	 * Change this instance to direction constraint
	 * 
	 * @param targetDirection
	 */
	public void toDirectionConstraint(FloatVector3 targetDirection) {
		this.constraintType = ConstraintType.DIRECTION;
		this.target = targetDirection;
	}
	
	/**
	 * Change this instance to linear constraint
	 * 
	 * @param targetLine
	 */
	public void toLinearConstraint(FloatVector3 targetLine) {
		this.constraintType = ConstraintType.LINEAR;
		this.target = targetLine;
	}
	
	/**
	 * Change this instance to planar constraint
	 * 
	 * @param targetPlaneNormal
	 */
	public void toPlanarConstraint(FloatVector3 targetPlaneNormal) {
		this.constraintType = ConstraintType.PLANAR;
		this.target = targetPlaneNormal;
	}

	/**
	 * 
	 * @param targetAngle
	 */
	public void toAngleConstraint(float targetAngle) {
		this.constraintType = ConstraintType.ANGLE;
		this.target = targetAngle;
	}

	public ConstraintType getConstraintType() {
		return constraintType;
	}

	public Bone getBone() {
		return bone;
	}

	public void setBone(Bone bone) {
		this.bone = bone;
	}

	public Method getTargetFunction() {
		return targetFunction;
	}

	public void setTargetFunction(Method targetFunction) {
		this.targetFunction = targetFunction;
	}

	/**
	 * END_EFFECTOR_DISTANCE target function
	 * 
	 * @return
	 * @throws KinematicsConstraintTypeMismatchException
	 */
	private float endEffectorDistanceFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.END_EFFECTOR_DISTANCE) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.distanceBetween(bone.chain.getEndEffector(), (FloatVector3) target);
	}

	private float distanceFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.DISTANCE) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.distanceBetween(bone.getEndPoint(), (FloatVector3) target);
	}

	private float endEffectorDirectionFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.END_EFFECTOR_DIRECTION) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.getAngleBetweenDegs(bone.chain.getEndEffectorDirectionUV(), (FloatVector3) target);
	}

	private float directionFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.DIRECTION) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.getAngleBetweenDegs(bone.getGlobalDirectionUV(), (FloatVector3) target);
	}

	private float linearFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.LINEAR) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.getLinearAngleBetweenDegs(bone.getGlobalDirectionUV(), (FloatVector3) target);
	}

	private float planarFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.PLANAR) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return FloatVector3.getPlanarAngleBetweenDegs(bone.getGlobalDirectionUV(), (FloatVector3) target);
	}

	private float angleFunc() throws KinematicsConstraintTypeMismatchException {
		if (constraintType != ConstraintType.ANGLE) {
			throw new KinematicsConstraintTypeMismatchException(this);
		}
		return Math.abs(bone.joint.angle.get() - (Float) target);
	}

	public float targetFunction() {

		try {
			switch (constraintType) {
			case NONE:
				return 0;
			case END_EFFECTOR_DISTANCE:
				return endEffectorDistanceFunc();
			case DISTANCE:
				return distanceFunc();
			case END_EFFECTOR_DIRECTION:
				return endEffectorDirectionFunc();
			case DIRECTION:
				return directionFunc();
			case LINEAR:
				return linearFunc();
			case PLANAR:
				return planarFunc();
			case ANGLE:
				return angleFunc();
			case CUSTOM:
				// Use custom function
				if (targetFunction != null) {
					try {
						return (float) targetFunction.invoke(null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return Float.NaN;
					}
				}
			}
		} catch (KinematicsConstraintTypeMismatchException e) {
			e.printStackTrace();
		}
		return Float.NaN;
	}

}
