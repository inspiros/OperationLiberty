package com.hust.core;

import org.ejml.simple.SimpleMatrix;

import com.hust.utils.Kinematics;
import com.hust.view.PWindow;
//import com.pi4j.wiringpi.Gpio;

public class Controller {
	private static boolean hasView;

	private static PWindow view;

	private static Data model;

	public static void main(String[] args) {
		System.out.println("Bye HUST, I'm FREE!");

		model = Data.setupModel();
		hasView = initiateView();

		// Gpio.wiringPiSetup();

		// PinOutDevice pinOut = new PinOutDevice(RaspiPin.GPIO_01);

		// for(;;) {
		// pinOut.toggle();
		// SleepUtils.delay(1000);
		// }

		for (SimpleMatrix matrix : model.getArm().getEndPoints()) {
		//	matrix.print();
		}
		
	}

	public static void test() {
		model.moveToPosition(new float[] { -70, 90, 100 }, Kinematics.FABRIK);
	}

	private static boolean initiateView() {
		try {
			view = new PWindow(model);
		} catch (Exception e) {
			return false;
		}

		Thread updater = new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				while (true) {
					try {
						Thread.sleep(50);
						view.updateRobotModel();
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updater.setPriority(Thread.MIN_PRIORITY);
		updater.start();
		return true;
	}

	public static void updateDegreeAngles(float... angles) {
		model.updateDegreeAngles(angles);
		printEndEffectorInfos();
	}

	public static void updateDegreeAngle(int id, float angle) {
		model.updateDegreeAngle(id, angle);
		// printEndEffectorInfos();
	}

	public static void changeDegreeAngles(float... changes) {
		model.changeDegreeAngles(changes);
		printEndEffectorInfos();
	}

	public static void changeDegreeAngle(int id, float change) {
		model.changeDegreeAngle(id, change);
		printEndEffectorInfos();
	}

	public static void printEndEffectorInfos() {
		float[] angles = model.getDegreeAngles();
		System.out.print("Angles: \t\t");
		for (float f : angles) {
			System.out.print(f + " ");
		}
		System.out.print("\nCoordinate: \t\t");
		model.getArm().printEndEffectorCoordinate();
		System.out.print("Direction Vector: \t");
		model.getArm().printEndEffectorDirection();
		System.out.println();
	}
}
