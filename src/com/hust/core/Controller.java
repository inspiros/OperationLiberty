package com.hust.core;

import com.hust.robot.KinematicsSolver;
import com.hust.utils.FloatMatrix4;
import com.hust.utils.FloatVector3;
import com.hust.view.PWindow;
//import com.pi4j.wiringpi.Gpio;

@SuppressWarnings("unused")
public class Controller {
	public static boolean hasView;

	private static PWindow view;

	private static DataBuffer dataBuffer;

	public static void main(String[] args) {
		ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();
		Thread mainThread = Thread.currentThread();
		Thread.setDefaultUncaughtExceptionHandler(controllerExceptionHandler);
		
		dataBuffer = DataBuffer.setupModel();
		hasView = initiateView();

		// Gpio.wiringPiSetup();

		// PinOutDevice pinOut = new PinOutDevice(RaspiPin.GPIO_01);

		// for(;;) {
		// pinOut.toggle();
		// SleepUtils.delay(1000);
		// }
		
		//model.getArm().forwardKinematics();
	}

	public static void test(float... fs) {
		dataBuffer.moveToPosition(new FloatVector3(fs), KinematicsSolver.IKMethod.GRADIENT_DESCENT);
	}

	private static boolean initiateView() {
		try {
			view = new PWindow(dataBuffer);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void updateDegreeAngles(float... angles) {
		dataBuffer.setAnglesDegs(angles);
	}

	public static void updateDegreeAngle(int id, float angle) {
		dataBuffer.setAngleDegs(id, angle);
	}

	public static void changeDegreeAngles(float... changes) {
		dataBuffer.updateAnglesDegs(changes);
	}

	public static void changeDegreeAngle(int id, float change) {
		dataBuffer.updateAngleDegs(id, change);
	}
}
