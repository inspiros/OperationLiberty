package com.hust.core;

import com.hust.robot.Joint;
import com.hust.robot.Joint.TrajectoryMethod;

public final class Configuration {

	/**
	 * Joint space trajectory method.
	 */
	public static Joint.TrajectoryMethod trajectoryMethod = TrajectoryMethod.CUBIC_POLYNOMIAL;

	public static void setTrajectoryMethod(Joint.TrajectoryMethod trajectoryMethod) {
		Configuration.trajectoryMethod = trajectoryMethod;
	}
	
	/**
	 * Rotation speed of joints when using no joint space trajectory method,
	 * specified in degrees per second.
	 */
	public static float noneTrajectoryVelocity = 60;

	/**
	 * Sleep time between updates.
	 */
	public static long sleepTime = 25L;
	
	/**
	 * Total joint trajectory time.
	 */
	public static long operationTime = 2000L;
	
	/**
	 * Blend factor of LSPB and LSQB trajectory method.
	 */
	public static float lspbFactor, lsqbFactor = 0.3f;
}
