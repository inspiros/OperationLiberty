package com.hust.core;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import com.hust.utils.Utils;
import com.hust.view.PWindow;
import com.hust.view.javafx.FxApplication;
//import com.pi4j.wiringpi.Gpio;

public class Main {

	public static FxApplication view;

	public static PWindow demo;

	public static DataBuffer dataBuffer;

	public void start() throws IOException {
		Utils.PROPERTIES.load(getClass().getResourceAsStream("/resources/config.properties"));
		checkPlatform();

		ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(controllerExceptionHandler);

		dataBuffer = DataBuffer.setupModel();
		initiateView();

		// Gpio.wiringPiSetup();

		// PinOutDevice pinOut = new PinOutDevice(RaspiPin.GPIO_01);

		// for(;;) {
		// pinOut.toggle();
		// SleepUtils.delay(1000);
		// }

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.err.print("Insert command: ");
			String cmd = scanner.nextLine();

			if (cmd.equalsIgnoreCase("A")) {
				for (int i = 0; i < dataBuffer.getDof(); i++) {
					dataBuffer.getArm().getBone(i).getEndPoint().print();
				}
			}
			System.out.println();
		}
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
	 */
	public static void main(String[] args) throws IOException {
		new Main().start();
	}

}
