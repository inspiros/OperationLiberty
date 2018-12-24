package com.hust.robot.trajectory;

public class QuinticTrajectoryPlanner extends JointTrajectoryPlanner {
	private float factor3, factor4, factor5;
	private long totalOperationTime;

	public QuinticTrajectoryPlanner(float angle, float target, long totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		this.factor3 = 10 * (target - angle) / (totalOperationTime * totalOperationTime * totalOperationTime);
		this.factor4 = -15 * (target - angle)
				/ (totalOperationTime * totalOperationTime * totalOperationTime * totalOperationTime);
		this.factor5 = 6 * (target - angle) / (totalOperationTime * totalOperationTime * totalOperationTime
				* totalOperationTime * totalOperationTime);
	}

	@Override
	public float angleAt(long time) {
		if (time < totalOperationTime) {
			return prevAngle + factor3 * time * time * time + factor4 * time * time * time * time
					+ factor5 * time * time * time * time * time;
		}
		return target;
	}

	@Override
	public float speedAt(long time) {
		return 3 * factor3 * time * time + 4 * factor4 * time * time * time + 5 * factor5 * time * time * time * time;
	}

	@Override
	public float accelerationAt(long time) {
		return 6 * factor3 * time + 12 * factor4 * time * time + 20 * factor5 * time * time * time;
	}
}
