package com.hust.core;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import com.hust.model.robot.Joint;
import com.hust.model.robot.Joint.TrajectoryMethod;
import com.hust.view.javafx.FxSceneSyncService;

public final class Configurations {

	/**
	 * Properties containing configurations loaded at start-up of application.
	 */
	public static final Properties PROPERTIES = new Properties();

	/**
	 * Load properties.
	 * 
	 * @throws IOException
	 */
	public static void setupProperties() throws IOException {
		PROPERTIES.load(new Configurations().getClass().getResourceAsStream("/resources/properties"));
		String osProperty;
		String detectedOs = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((detectedOs.indexOf("mac") >= 0) || (detectedOs.indexOf("darwin") >= 0)) {
			osProperty = "MAC";
		} else if (detectedOs.indexOf("win") >= 0) {
			osProperty = "WINDOWS";
		} else if (detectedOs.indexOf("nux") >= 0) {
			osProperty = "LINUX";
		} else {
			osProperty = "OTHER";
		}
		PROPERTIES.put("platform", osProperty);
	}

	/**
	 * Joint space trajectory method.
	 */
	public static Joint.TrajectoryMethod trajectoryMethod = TrajectoryMethod.CUBIC_POLYNOMIAL;

	/**
	 * Set the general joint space trajectory method.
	 * 
	 * @param trajectoryMethod The new trajectory method.
	 */
	public static void setTrajectoryMethod(Joint.TrajectoryMethod trajectoryMethod) {
		Configurations.trajectoryMethod = trajectoryMethod;
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

	/**
	 * Delay between update time of UI, 50 millis as default value.
	 * 
	 * @see FxSceneSyncService
	 */
	public static long viewUpdateInterval = 60L;
}
