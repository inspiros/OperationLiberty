package com.hust.robot;

import com.hust.robot.trajectory.CubicTrajectoryPlanner;
import com.hust.robot.trajectory.JointTrajectoryPlanner;
import com.hust.robot.trajectory.LSPBTrajectoryPlanner;
import com.hust.robot.trajectory.LinearTrajectoryPlanner;
import com.hust.robot.trajectory.LSQBTrajectoryPlanner;
import com.hust.robot.trajectory.QuinticTrajectoryPlanner;
import com.hust.utils.FloatVector3;
import com.hust.utils.Operator;
import com.hust.utils.Utils;

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
		LSQB
	}

	public static TrajectoryMethod trajectoryMethod = TrajectoryMethod.LSPB;

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
	public float angle;

	/**
	 * Lockable implementation.
	 */
	public float angleLock;

	/**
	 * Rotation speed limit in degrees per second.
	 */
	public float maxUpdateRate = 3;
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
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs, float lowerLimit, float upperLimit) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.angle = Utils.clamp(angleDegs, lowerLimit, upperLimit);
		this.target = this.angle;
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.angle = Utils.clamp(angleDegs, lowerLimit, upperLimit);
		this.target = this.angle;
		addDataChangeListener(bone.chain);
	}

	public Joint(Bone bone, FloatVector3 rotationAxis) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		addDataChangeListener(bone.chain);
	}

	public void setAngleRads(float angleRads) {
		this.angle = Utils.clamp(angleRads * Utils.RADS_TO_DEGS, lowerLimit, upperLimit);
		changeDataTo(bone.id, angle);
	}

	public void setAngleDegs(float angleDegs) {
		this.angle = Utils.clamp(angleDegs, lowerLimit, upperLimit);
		changeDataTo(bone.id, angle);
	}

	public void updateAngleRads(float changeRads) {
		float temp = angle + changeRads * Utils.RADS_TO_DEGS;
		angle = Utils.clamp(temp, lowerLimit, upperLimit);
		changeDataTo(bone.id, angle);
	}

	public void updateAngleDegs(float changeDegs) {
		float temp = angle + changeDegs;
		angle = Utils.clamp(temp, lowerLimit, upperLimit);
		changeDataTo(bone.id, angle);
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
		 * Precalculated direction for linear trajectory, or used as previous angle for
		 * other methods of trajectory.
		 */
		public JointTrajectoryPlanner trajectoryPlanner;
		private float direction;

		@Override
		protected boolean terminateCondition() {
			return angle == target;
		}

		@Override
		protected void setup() {

			switch (trajectoryMethod) {
			case NONE:
				direction = (float) Math.signum(target - angle);
				return;
			case LINEAR:
				trajectoryPlanner = new LinearTrajectoryPlanner(angle, target, totalOperationTime);
				return;
			case CUBIC_POLYNOMIAL:
				trajectoryPlanner = new CubicTrajectoryPlanner(angle, target, totalOperationTime);
				return;
			case QUINTIC_POLYNOMIAL:
				trajectoryPlanner = new QuinticTrajectoryPlanner(angle, target, totalOperationTime);
				return;
			case LSPB:
				trajectoryPlanner = new LSPBTrajectoryPlanner(angle, target, 0.3f, totalOperationTime);
				return;
			case LSQB:
				trajectoryPlanner = new LSQBTrajectoryPlanner(angle, target, 0.3f, totalOperationTime);
				return;
			}
		}

		@Override
		protected void loop() {
			if (trajectoryMethod == TrajectoryMethod.NONE) {
				updateAngleDegs(direction * Math.min(maxUpdateRate, Math.abs(target - angle)));
			} else {
				setAngleDegs(trajectoryPlanner.angleAt(operatedTime));
			}
		}

		@Override
		protected void terminate() {
			trajectoryPlanner = null;
		}
	}

	@Override
	public void lock() {
		angleLock = angle;
		locked = true;
	}

	@Override
	public void unlock() {
		angle = angleLock;
		locked = false;
	}
}
