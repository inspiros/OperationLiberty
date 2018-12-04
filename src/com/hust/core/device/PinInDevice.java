package com.hust.core.device;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;

public class PinInDevice {

	private final GpioController deviceController = GpioFactory.getInstance();
	private GpioPinDigitalInput pin;

	/**
	 * 
	 * @param _pin: RaspiPin.GPIO_XX
	 */
	public PinInDevice(Pin _pin) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionDigitalInputPin(_pin);
	}

	/**
	 * 
	 * @param _pin: RaspiPin.GPIO_XX
	 * @param _pullResistance: PinPullResistance.PULL_UP,
	 *        PinPullResistance.PULL_DOWN, PinPullResistance.OFF
	 */
	public PinInDevice(Pin _pin, PinPullResistance _pullResistance) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionDigitalInputPin(_pin, _pullResistance);
	}

	/**
	 * Get controlled pin of the device.
	 * 
	 * @return
	 */
	public Pin getPin() {
		return pin.getPin();
	}

	/**
	 * Get pin state.
	 * 
	 * @return
	 */
	public boolean getState() {
		return pin.getState().isHigh();
	}

	/**
	 * Get pull resistance of pin
	 * 
	 * @return
	 */
	public PinPullResistance getPullResistance() {
		return pin.getPullResistance();
	}

	/**
	 * Release pin device resource.
	 */
	public void release() {
		deviceController.shutdown();
	}
}
