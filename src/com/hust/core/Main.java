package com.hust.core;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import com.hust.actuator.Actuators;
import com.hust.utils.Utils;
import com.hust.view.demo.PWindow;
import com.hust.view.javafx.FxApplication;
//import com.pi4j.wiringpi.Gpio;

public class Main {

	public static FxApplication view;

	public static PWindow demo;

	public static Models model;

	public static Actuators actuator;

	public void start() throws IOException, InterruptedException {
		Utils.PROPERTIES.load(getClass().getResourceAsStream("/resources/config.properties"));
		checkPlatform();

		ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(controllerExceptionHandler);

		model = Models.setupModel();
		actuator = new Actuators(model).setupActuators();
		initiateView();

		// Gpio.wiringPiSetup();

		// PinOutDevice pinOut = new PinOutDevice(RaspiPin.GPIO_01);

		// for(;;) {
		// pinOut.toggle();
		// SleepUtils.delay(1000);
		// }

//		@SuppressWarnings("resource")
//		Scanner scanner = new Scanner(System.in);
//		while (true) {
//			System.err.print("Insert command: ");
//			String cmd = scanner.nextLine();
//
//			if (cmd.equalsIgnoreCase("A")) {
//				for (int i = 0; i < model.getDof(); i++) {
//					model.getArm().getBone(i).getEndPoint().print();
//				}
//			}
//			System.out.println();
//		}
//		
//		try {
//			Thread.sleep(5000);
//			while(true) {
//				Thread[] list = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
//				Thread.currentThread().getThreadGroup().enumerate(list);
//				for (Thread thread : list) {
//					System.out.println(thread.getName() + " " + thread.getState().toString());
//				}
//				Thread.sleep(5000);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Thread.currentThread().join();
	}

	private void checkPlatform() {
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
		Utils.PROPERTIES.put("platform", osProperty);
	}

	private void initiateView() {
		// Might run on headless devices.
		try {
			if (Boolean.parseBoolean(Utils.PROPERTIES.getProperty("demo"))
					&& Utils.PROPERTIES.getProperty("platform") != "LINUX") {
				demo = new PWindow();
			}
			if (Boolean.parseBoolean(Utils.PROPERTIES.getProperty("view"))) {
				view = FxApplication.getInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Real Main Method.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		new Main().start();
	}

}
