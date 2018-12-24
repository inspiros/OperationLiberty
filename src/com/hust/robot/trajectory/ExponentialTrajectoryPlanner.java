package com.hust.robot.trajectory;

public class ExponentialTrajectoryPlanner extends JointTrajectoryPlanner {
	private float direction;
	private double totalOperationTime;

	public ExponentialTrajectoryPlanner(float angle, float target, long totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.direction = (target - angle);
		this.totalOperationTime = totalOperationTime;
	}

	@Override
	public float angleAt(long time) {
		if (time < totalOperationTime) {
			return target
					- direction * (float) Math.pow(2.0, 10 * (totalOperationTime - time) / totalOperationTime) / 1024;
		}
		return target;
	}

	@Override
	public float speedAt(long time) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float accelerationAt(long time) {
		// TODO Auto-generated method stub
		return 0;
	}

}
