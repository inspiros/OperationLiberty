package com.hust.model.robot.trajectory;

public class CubicTrajectoryPlanner extends JointTrajectoryPlanner {
	private float factor2, factor3;
	private long totalOperationTime;

	public CubicTrajectoryPlanner(float angle, float target, long totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		this.factor2 = 3 * (target - angle) / (totalOperationTime * totalOperationTime);
		this.factor3 = -2 * (target - angle) / (totalOperationTime * totalOperationTime * totalOperationTime);
	}

	@Override
	public float angleAt(long time) {
		if (time < totalOperationTime) {
			return prevAngle + factor2 * time * time + factor3 * time * time * time;
		}
		return target;
	}

	@Override
	public float speedAt(long time) {
		return 2 * factor2 * time + 3 * factor3 * time * time;
	}

	@Override
	public float accelerationAt(long time) {
		return 2 * factor2 + 6 * factor3 * time;
	}

}
