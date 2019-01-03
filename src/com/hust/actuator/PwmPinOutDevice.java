package com.hust.actuator;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;

public class PwmPinOutDevice {

	private final GpioController deviceController = GpioFactory.getInstance();
	private GpioPinPwmOutput pin;

	public PwmPinOutDevice(Pin _pin) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionPwmOutputPin(_pin);
	}

	public PwmPinOutDevice(Pin _pin, int initialValue) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionPwmOutputPin(_pin);
		pin.setPwm(initialValue);
	}
	
	public PwmPinOutDevice(Pin _pin, int initialValue, int range) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionPwmOutputPin(_pin);
		pin.setPwmRange(range);
		pin.setPwm(initialValue);
	}

	public Pin getPin() {
		return pin.getPin();
	}
	
	public int getPwm() {
		return pin.getPwm();
	}
	
	public void setPwm(int value) {
		pin.setPwm(value);
	}
	
	public void setPwmRange(int range) {
		pin.setPwmRange(range);
	}

	public void release() {
		deviceController.shutdown();
	}
}
