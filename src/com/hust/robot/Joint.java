package com.hust.robot;

import com.hust.utils.FloatVector3;
import com.hust.utils.Operator;
import com.hust.utils.Utils;

public class Joint extends DataChanger<Float> {

	/**
	 * The bone containing this joint.
	 */
	private Bone bone;

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
	 * Rotation speed limit in degrees per second.
	 */
	public float maxUpdateRate = 2;
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
	private float target;

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

	private class AngleUpdater extends Operator {
		private float direction;

		@Override
		protected boolean terminateCondition() {
			return angle == target;
		}

		@Override
		protected void setup() {
			direction = Utils.sign(target - angle);
		}

		@Override
		protected void loop() {
			updateAngleDegs(direction * Math.min(maxUpdateRate, Math.abs(target - angle)));
		}

		@Override
		protected void terminate() {
			direction = 0.0f;
		}
	}
}
