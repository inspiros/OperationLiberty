package com.hust.robot;

import com.hust.core.Configuration;
import com.hust.robot.trajectory.CubicTrajectoryPlanner;
import com.hust.robot.trajectory.ExponentialTrajectoryPlanner;
import com.hust.robot.trajectory.JointTrajectoryPlanner;
import com.hust.robot.trajectory.LSPBTrajectoryPlanner;
import com.hust.robot.trajectory.LinearTrajectoryPlanner;
import com.hust.robot.trajectory.NoneTrajectoryPlanner;
import com.hust.robot.trajectory.LSQBTrajectoryPlanner;
import com.hust.robot.trajectory.QuinticTrajectoryPlanner;
import com.hust.utils.DataChanger;
import com.hust.utils.Operator;
import com.hust.utils.Utils;
import com.hust.utils.data.FloatVector3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Joint extends DataChanger<Float> {
	public enum TrajectoryMethod {
		NONE,
		/**
		 * Linear interpolation.
		 */
		LINEAR,
		/**
		 * 3rd polynomial interpolation.
		 */
		CUBIC_POLYNOMIAL,
		/**
		 * 5th polynomial interpolation.
		 */
		QUINTIC_POLYNOMIAL,
		/**
		 * Linear Segment with Parabolic Blends (2-1-2).
		 */
		LSPB,
		/**
		 * Linear Segment with Quadratic Blends (4-1-4).
		 */
		LSQB,
		/**
		 * Exponential interpolation.
		 */
		EXPONENTIAL
	}

	/**
	 * The bone containing this joint.
	 */
	public Bone bone;

	/**
	 * Rotation axis vector, also the normal vector of the plane containing the bone
	 * of this joint.
	 */
	public FloatVector3 rotationAxis;
	/**
	 * Rotation angle in degrees.
	 */
	public FloatProperty angle;
	//public float angle;

	/**
	 * Lockable implementation.
	 */
	public float angleLock;

	/**
	 * Lower limit to angle.
	 */
	public float lowerLimit = -180;
	/**
	 * Upper limit to angle.
	 */
	public float upperLimit = 180;

	/**
	 * The target angle to which this joint would automatically follow.
	 */
	public float target;
	/**
	 * Angle updater runnable to implement rotation speed limit.
	 */
	private AngleUpdater updater = new AngleUpdater();

	public Joint(Bone bone) {
		this.bone = bone;
		this.rotationAxis = FloatVector3.Z_AXIS;
		this.angle = new SimpleFloatProperty();
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs, float lowerLimit, float upperLimit) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.angle = new SimpleFloatProperty(Utils.clamp(angleDegs, lowerLimit, upperLimit));
		this.target = this.angle.get();
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.angle = new SimpleFloatProperty(Utils.clamp(angleDegs, lowerLimit, upperLimit));
		this.target = this.angle.get();
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.angle = new SimpleFloatProperty();
		addDataChangeListener(bone.chain);
	}

	public void setAngleRads(float angleRads) {
		this.angle.set(Utils.clamp(angleRads * Utils.RADS_TO_DEGS, lowerLimit, upperLimit));
		changeDataTo(bone.id, angle.get());
	}

	public void setAngleDegs(float angleDegs) {
		this.angle.set(Utils.clamp(angleDegs, lowerLimit, upperLimit));
		changeDataTo(bone.id, angle.get());
	}

	public void updateAngleRads(float changeRads) {
		float temp = angle.get() + changeRads * Utils.RADS_TO_DEGS;
		angle.set(Utils.clamp(temp, lowerLimit, upperLimit));
		changeDataTo(bone.id, angle.get());
	}

	public void updateAngleDegs(float changeDegs) {
		float temp = angle.get() + changeDegs;
		angle.set(Utils.clamp(temp, lowerLimit, upperLimit));
		changeDataTo(bone.id, angle.get());
	}

	public void setTargetRads(float angleRads) {
		this.target = angleRads * Utils.RADS_TO_DEGS;
		updateToTarget();
	}

	public void setTargetDegs(float angleDegs) {
		this.target = angleDegs;
		updateToTarget();
	}

	public void abortTargetFollowing() {
		updater.abort();
	}

	private void updateToTarget() {
		updater.abort();
		updater.start();
	}

	/**
	 * Joint angle space trajectory class.
	 * 
	 * @author Inspiros
	 *
	 */
	private class AngleUpdater extends Operator {
		/**
		 * Joint space trajectory planner.
		 */
		public JointTrajectoryPlanner trajectoryPlanner;

		@Override
		protected boolean terminateCondition() {
			return angle.get() == target;
		}

		@Override
		protected void setup() {
			TrajectoryMethod trajectoryMethod = Configuration.trajectoryMethod;

			switch (trajectoryMethod) {
			case NONE:
				trajectoryPlanner = new NoneTrajectoryPlanner(angle.get(), target, Configuration.noneTrajectoryVelocity);
				return;
			case LINEAR:
				trajectoryPlanner = new LinearTrajectoryPlanner(angle.get(), target, Configuration.operationTime);
				return;
			case CUBIC_POLYNOMIAL:
				trajectoryPlanner = new CubicTrajectoryPlanner(angle.get(), target, Configuration.operationTime);
				return;
			case QUINTIC_POLYNOMIAL:
				trajectoryPlanner = new QuinticTrajectoryPlanner(angle.get(), target, Configuration.operationTime);
				return;
			case LSPB:
				trajectoryPlanner = new LSPBTrajectoryPlanner(angle.get(), target, Configuration.lspbFactor, Configuration.operationTime);
				return;
			case LSQB:
				trajectoryPlanner = new LSQBTrajectoryPlanner(angle.get(), target, Configuration.lsqbFactor, Configuration.operationTime);
				return;
			case EXPONENTIAL:
				trajectoryPlanner = new ExponentialTrajectoryPlanner(angle.get(), target, Configuration.operationTime);
				return;
			}
		}

		@Override
		protected void loop() {
			setAngleDegs(trajectoryPlanner.angleAt(operatedTime));
		}

		@Override
		protected void terminate() {
			trajectoryPlanner = null;
		}
	}

	@Override
	public void lock() {
		angleLock = angle.get();
		locked = true;
	}

	@Override
	public void unlock() {
		angle.set(angleLock);
		locked = false;
	}
}
