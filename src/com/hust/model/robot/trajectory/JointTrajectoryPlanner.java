package com.hust.model.robot.trajectory;

public abstract class JointTrajectoryPlanner {
	protected float prevAngle, target;

	public abstract float angleAt(long time);
	
	public abstract float speedAt(long time);
	
	public abstract float accelerationAt(long time);
}
