package com.hust.core;

import java.util.Scanner;

import com.hust.view.PWindow;
//import com.pi4j.wiringpi.Gpio;

public class Main {
	public static boolean hasView;

	public static PWindow view;

	public static DataBuffer dataBuffer;

	public static void main(String[] args) {
		ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

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
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.err.print("Insert command: ");
			String cmd = scanner.nextLine();
			
			if(cmd.equalsIgnoreCase("A")) {
				for(int i = 0; i < dataBuffer.getDof(); i++) {
					dataBuffer.getArm().getBone(i).getEndPoint().print();
				}
			}
			System.out.println();
		}
	}


	private static boolean initiateView() {
		try {
			view = new PWindow(dataBuffer);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
