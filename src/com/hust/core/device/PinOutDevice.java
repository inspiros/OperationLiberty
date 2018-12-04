package com.hust.core.device;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class PinOutDevice {

	private final GpioController deviceController = GpioFactory.getInstance();
	private GpioPinDigitalOutput pin;

	/**
	 * 
	 * @param _pin: RaspiPin.GPIO_XX
	 */
	public PinOutDevice(Pin _pin) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionDigitalOutputPin(_pin);
	}

	/**
	 * 
	 * @param _pin: RaspiPin.GPIO_XX
	 * @param defaultState: high or low
	 */
	public PinOutDevice(Pin _pin, boolean defaultState) {
		// TODO Auto-generated constructor stub
		pin = deviceController.provisionDigitalOutputPin(_pin, defaultState ? PinState.HIGH : PinState.LOW);
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
	 * Set pin state.
	 * 
	 * @param _state
	 */
	public void setState(boolean _state) {
		pin.setState(_state);
	}

	/**
	 * Toggle pin.
	 */
	public void toggle() {
		pin.toggle();
	}

	/**
	 * Toggle pin state in the following duration.
	 * 
	 * @param _time
	 * @param _state
	 */
	public void pulse(long _time) {
		pin.pulse(_time);
	}

	/**
	 * Blink pin in the following duration.
	 * 
	 * @param _time
	 */
	public void blink(long _time) {
		pin.blink(_time);
	}

	/**
	 * Release pin device resource.
	 */
	public void release() {
		deviceController.shutdown();
	}
}
