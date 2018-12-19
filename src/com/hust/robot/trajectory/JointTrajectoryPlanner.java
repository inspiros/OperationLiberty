package com.hust.robot.trajectory;

public abstract class JointTrajectoryPlanner {
	protected float target;

	public abstract float angleAt(float time);
	
	public abstract float speedAt(float time);
	
	public abstract float accelerationAt(float time);
}
