package com.hust.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.hust.model.robot.Joint;
import com.hust.model.robot.Joint.TrajectoryMethod;
import com.hust.view.javafx.SceneSyncService;

public final class Configurations {

	/**
	 * Properties containing configurations loaded at start-up of application.
	 */
	public static final Properties PROPERTIES = new Properties();

	/**
	 * Modules of this application.
	 */
	public static final Map<String, CountDownLatch> MODULES_INITIALIZATION = new HashMap<>();

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

		/*
		 * Setup modules loaders.
		 */
		MODULES_INITIALIZATION.put("model.unulled", new CountDownLatch(1));
		MODULES_INITIALIZATION.put("model", new CountDownLatch(1));

		if (Boolean.parseBoolean(PROPERTIES.getProperty("view"))) {
			MODULES_INITIALIZATION.put("view", new CountDownLatch(1));
		} else {
			MODULES_INITIALIZATION.put("view", new CountDownLatch(0));
		}

		if (Boolean.parseBoolean(PROPERTIES.getProperty("demo"))
				&& !PROPERTIES.getProperty("platform").equals("LINUX")) {
			MODULES_INITIALIZATION.put("demo", new CountDownLatch(1));
		} else {
			MODULES_INITIALIZATION.put("demo", new CountDownLatch(0));
		}

		if (Boolean.parseBoolean(PROPERTIES.getProperty("actuator"))) {
			MODULES_INITIALIZATION.put("actuator", new CountDownLatch(1));
		} else {
			MODULES_INITIALIZATION.put("actuator", new CountDownLatch(0));
		}

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
	 * @see SceneSyncService
	 */
	public static long viewUpdateInterval = 60L;

	/**
	 * Max displayed node in charts.
	 */
	public static int maxChartNodes = 30;
	
	/**
	 * Actuator readback timeout.
	 */
	public static long readbackTimeout = 20;
}
