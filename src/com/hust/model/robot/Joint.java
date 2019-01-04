package com.hust.model.robot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.hust.core.Configurations;
import com.hust.model.robot.trajectory.CubicTrajectoryPlanner;
import com.hust.model.robot.trajectory.ExponentialTrajectoryPlanner;
import com.hust.model.robot.trajectory.JointTrajectoryPlanner;
import com.hust.model.robot.trajectory.LSPBTrajectoryPlanner;
import com.hust.model.robot.trajectory.LSQBTrajectoryPlanner;
import com.hust.model.robot.trajectory.LinearTrajectoryPlanner;
import com.hust.model.robot.trajectory.NoneTrajectoryPlanner;
import com.hust.model.robot.trajectory.QuinticTrajectoryPlanner;
import com.hust.utils.Lockable;
import com.hust.utils.Utils;
import com.hust.utils.data.FloatVector3;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Joint implements Lockable {
	public enum TrajectoryMethod {
		/**
		 * Without any trajectory model.
		 */
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
	public AngleUpdater angleUpdater = new AngleUpdater();

	/**
	 * Lockable
	 */
	public boolean locked;

	public Joint(Bone bone) {
		this.bone = bone;
		this.rotationAxis = FloatVector3.Z_AXIS;
		this.angle = new SimpleFloatProperty();
		setupAngleChangeListener();
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs, float lowerLimit, float upperLimit) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.angle = new SimpleFloatProperty(Utils.clamp(angleDegs, lowerLimit, upperLimit));
		this.target = this.angle.get();
		setupAngleChangeListener();
	}

	public Joint(Bone bone, FloatVector3 rotationAxis, float angleDegs) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.angle = new SimpleFloatProperty(Utils.clamp(angleDegs, lowerLimit, upperLimit));
		this.target = this.angle.get();
		setupAngleChangeListener();
	}

	public Joint(Bone bone, FloatVector3 rotationAxis) {
		this.bone = bone;
		this.rotationAxis = rotationAxis;
		this.angle = new SimpleFloatProperty();
		setupAngleChangeListener();
	}

	private void setupAngleChangeListener() {
		angle.addListener((observable, oldValue, newValue) -> {
			bone.chain.recalculateTransformation(bone.id);
		});
	}

	public void setAngleRads(float angleRads) {
		this.angle.set(Utils.clamp(angleRads * Utils.RADS_TO_DEGS, lowerLimit, upperLimit));
	}

	public void setAngleDegs(float angleDegs) {
		this.angle.set(Utils.clamp(angleDegs, lowerLimit, upperLimit));
	}

	public void updateAngleRads(float changeRads) {
		float temp = angle.get() + changeRads * Utils.RADS_TO_DEGS;
		angle.set(Utils.clamp(temp, lowerLimit, upperLimit));
	}

	public void updateAngleDegs(float changeDegs) {
		float temp = angle.get() + changeDegs;
		angle.set(Utils.clamp(temp, lowerLimit, upperLimit));
	}

	public void setTargetRads(float angleRads) {
		this.target = angleRads * Utils.RADS_TO_DEGS;
		updateToTarget();
	}

	public void setTargetDegs(float angleDegs) {
		this.target = angleDegs;
		updateToTarget();
	}

	public void prepareTargetRads(float angleRads) {
		this.target = angleRads * Utils.RADS_TO_DEGS;
	}

	public void prepareTargetDegs(float angleDegs) {
		this.target = angleDegs;
	}

	public void abortTargetFollowing() {
		angleUpdater.abort();
	}

	public void updateToTarget() {
		angleUpdater.abort();
		angleUpdater.start();
	}

	/**
	 * Joint angle space trajectory class.
	 * 
	 * @author Inspiros
	 *
	 */
	public class AngleUpdater implements Runnable {
		/**
		 * Joint space trajectory planner.
		 */
		public JointTrajectoryPlanner trajectoryPlanner;

		public BooleanProperty operationFinishedFlag;

		public ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		public ScheduledFuture<?> scheduledFuture;

		protected long operatedTime;

		public AngleUpdater() {
			this.operationFinishedFlag = new SimpleBooleanProperty(false);

		}

		public synchronized void start() {
			// Small changes
			if (Math.abs(target - angle.get()) < 1) {
				setAngleDegs(target);
				terminate();
			} else {

				setup();
				operatedTime = 0;
				scheduledFuture = scheduler.scheduleAtFixedRate(this, 0, Configurations.sleepTime,
						TimeUnit.MILLISECONDS);
			}
		}

		public synchronized void abort() {
			if (scheduledFuture != null && !scheduledFuture.isDone()) {
				scheduledFuture.cancel(true);
				// terminate();
			}
		}

		protected void setup() {
			TrajectoryMethod trajectoryMethod = Configurations.trajectoryMethod;

			switch (trajectoryMethod) {
			case NONE:
				trajectoryPlanner = new NoneTrajectoryPlanner(angle.get(), target,
						Configurations.noneTrajectoryVelocity);
				return;
			case LINEAR:
				trajectoryPlanner = new LinearTrajectoryPlanner(angle.get(), target, Configurations.operationTime);
				return;
			case CUBIC_POLYNOMIAL:
				trajectoryPlanner = new CubicTrajectoryPlanner(angle.get(), target, Configurations.operationTime);
				return;
			case QUINTIC_POLYNOMIAL:
				trajectoryPlanner = new QuinticTrajectoryPlanner(angle.get(), target, Configurations.operationTime);
				return;
			case LSPB:
				trajectoryPlanner = new LSPBTrajectoryPlanner(angle.get(), target, Configurations.lspbFactor,
						Configurations.operationTime);
				return;
			case LSQB:
				trajectoryPlanner = new LSQBTrajectoryPlanner(angle.get(), target, Configurations.lsqbFactor,
						Configurations.operationTime);
				return;
			case EXPONENTIAL:
				trajectoryPlanner = new ExponentialTrajectoryPlanner(angle.get(), target, Configurations.operationTime);
				return;
			}
		}

		protected void terminate() {
			trajectoryPlanner = null;
			operationFinishedFlag.set(true);
			operationFinishedFlag.set(false);
		}

		@Override
		public void run() {
			operatedTime += Configurations.sleepTime;
			setAngleDegs(trajectoryPlanner.angleAt(operatedTime));
			if (operatedTime == Configurations.operationTime || angle.get() == target) {
				scheduledFuture.cancel(false);
				terminate();
			}
		}
	}

	// Lockable implementations.
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
