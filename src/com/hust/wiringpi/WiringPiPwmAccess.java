package com.hust.wiringpi;

import com.hust.utils.RaspiPinUtils;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.SoftPwm;

public class WiringPiPwmAccess {

	/**
	 * Start pwm in a pin.
	 * 
	 * @param _pin
	 */
	public static void start(int _pin) {
		SoftPwm.softPwmCreate(_pin, 0, 1000);
	}

	/**
	 * Start pwm in a pin.
	 * 
	 * @param _pin
	 * @param lower
	 * @param upper
	 */
	public static void start(int _pin, int lower, int upper) {
		SoftPwm.softPwmCreate(_pin, lower, upper);
	}

	/**
	 * Start pwm in a pin.
	 * 
	 * @param _pin
	 */
	public static void start(Pin _pin) {
		SoftPwm.softPwmCreate(RaspiPinUtils.pinNumberOf(_pin), 0, 1000);
	}

	/**
	 * Start pwm in a pin.
	 * 
	 * @param _pin
	 * @param lower
	 * @param upper
	 */
	public static void start(Pin _pin, int lower, int upper) {
		SoftPwm.softPwmCreate(RaspiPinUtils.pinNumberOf(_pin), lower, upper);
	}

	/**
	 * Start pwm and set its initial value in a pin.
	 * 
	 * @param _pin
	 * @param initialValue
	 */
	public static void start(int _pin, int initialValue) {
		SoftPwm.softPwmCreate(_pin, 0, 1000);
		SoftPwm.softPwmWrite(_pin, initialValue);
	}

	/**
	 * Start pwm and set its initial value in a pin.
	 * 
	 * @param _pin
	 * @param initialValue
	 */
	public static void start(Pin _pin, int initialValue) {
		SoftPwm.softPwmCreate(RaspiPinUtils.pinNumberOf(_pin), 0, 1000);
		SoftPwm.softPwmWrite(RaspiPinUtils.pinNumberOf(_pin), initialValue);
	}

	/**
	 * Modify pwm value in a pin.
	 * 
	 * @param _pin
	 * @param value
	 */
	public static void write(int _pin, int value) {
		SoftPwm.softPwmWrite(_pin, value);
	}

	/**
	 * Modify pwm value in a pin.
	 * 
	 * @param _pin
	 * @param value
	 */
	public static void write(Pin _pin, int value) {
		SoftPwm.softPwmWrite(RaspiPinUtils.pinNumberOf(_pin), value);
	}

	/**
	 * Stop pwm.
	 * 
	 * @param _pin
	 */
	public static void stop(int _pin) {
		SoftPwm.softPwmStop(_pin);
	}

	/**
	 * Stop pwm.
	 * 
	 * @param _pin
	 */
	public static void stop(Pin _pin) {
		SoftPwm.softPwmStop(RaspiPinUtils.pinNumberOf(_pin));
	}
}
