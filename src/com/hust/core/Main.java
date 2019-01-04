package com.hust.core;

import java.io.IOException;

import com.hust.actuator.Actuators;
import com.hust.model.Models;
import com.hust.view.Views;
import com.hust.view.demo.PWindow;
//import com.pi4j.wiringpi.Gpio;

import jssc.SerialPortException;

public class Main {

	public static Views view;

	public static PWindow demo;

	public static Models model;

	public static Actuators actuator;

	public void start() throws IOException, InterruptedException {
		Configurations.setupProperties();

		ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(controllerExceptionHandler);

		model = new Models().setupModel();
		actuator = new Actuators(model).setupActuators();
		view = new Views(model).setupViews();

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
//			e.printStackTrace();
//		}
		Thread.currentThread().join();
	}

	/**
	 * Real Main Method.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SerialPortException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		new Main().start();
	}

}
