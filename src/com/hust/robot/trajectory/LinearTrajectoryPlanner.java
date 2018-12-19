package com.hust.robot.trajectory;

public class LinearTrajectoryPlanner extends JointTrajectoryPlanner {
	private float prevAngle, factor, totalOperationTime;

	public LinearTrajectoryPlanner(float angle, float target, float totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		this.factor = (target - angle) / totalOperationTime;
	}

	@Override
	public float angleAt(float time) {
		if (time < totalOperationTime) {
			return prevAngle + factor * time;
		}
		return target;
	}

	@Override
	public float speedAt(float time) {
		return (target - prevAngle) / totalOperationTime;
	}

	@Override
	public float accelerationAt(float time) {
		return 0;
	}

}
