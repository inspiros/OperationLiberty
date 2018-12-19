package com.hust.robot.trajectory;

public class CubicTrajectoryPlanner extends JointTrajectoryPlanner {
	private float prevAngle, factor2, factor3, totalOperationTime;

	public CubicTrajectoryPlanner(float angle, float target, float totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		this.factor2 = 3 * (target - angle) / (totalOperationTime * totalOperationTime);
		this.factor3 = -2 * (target - angle) / (totalOperationTime * totalOperationTime * totalOperationTime);
	}

	@Override
	public float angleAt(float time) {
		if (time < totalOperationTime) {
			return prevAngle + factor2 * time * time + factor3 * time * time * time;
		}
		return target;
	}

	@Override
	public float speedAt(float time) {
		return 2 * factor2 * time + 3 * factor3 * time * time;
	}

	@Override
	public float accelerationAt(float time) {
		return 2 * factor2 + 6 * factor3 * time;
	}

}
