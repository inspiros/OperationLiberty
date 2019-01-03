package com.hust.model.robot.trajectory;

public class NoneTrajectoryPlanner extends JointTrajectoryPlanner {

	private float direction, velocity;
	private long totalOperationTime;

	public NoneTrajectoryPlanner(float angle, float target, float velocity) {
		// Convert to degrees per millis
		velocity /= 1000;
		this.prevAngle = angle;
		this.target = target;
		this.direction = Math.signum(target - angle);
		this.velocity = direction * velocity;
		this.totalOperationTime = (long) Math.abs((target - angle) / velocity);
	}

	@Override
	public float angleAt(long time) {
		if (time < totalOperationTime) {
			return prevAngle + velocity * time;
		}
		return target;
	}

	@Override
	public float speedAt(long time) {
		return velocity;
	}

	@Override
	public float accelerationAt(long time) {
		return 0;
	}

}
