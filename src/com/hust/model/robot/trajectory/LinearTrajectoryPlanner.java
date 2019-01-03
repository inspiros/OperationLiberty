package com.hust.model.robot.trajectory;

public class LinearTrajectoryPlanner extends JointTrajectoryPlanner {
	private float factor;
	private long totalOperationTime;

	public LinearTrajectoryPlanner(float angle, float target, long totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		this.factor = (target - angle) / totalOperationTime;
	}

	@Override
	public float angleAt(long time) {
		if (time < totalOperationTime) {
			return prevAngle + factor * time;
		}
		return target;
	}

	@Override
	public float speedAt(long time) {
		return (target - prevAngle) / totalOperationTime;
	}

	@Override
	public float accelerationAt(long time) {
		return 0;
	}

}
