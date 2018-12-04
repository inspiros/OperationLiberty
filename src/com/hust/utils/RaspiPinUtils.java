package com.hust.utils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class RaspiPinUtils {

	/**
	 * Get Interface Pin.
	 * 
	 * @param _pin
	 * @return
	 */
	public static Pin pinAtNumber(int _pin) {
		switch (_pin) {
		case 0:
			return RaspiPin.GPIO_00;
		case 1:
			return RaspiPin.GPIO_01;
		case 2:
			return RaspiPin.GPIO_02;
		case 3:
			return RaspiPin.GPIO_03;
		case 4:
			return RaspiPin.GPIO_04;
		case 5:
			return RaspiPin.GPIO_05;
		case 6:
			return RaspiPin.GPIO_06;
		case 7:
			return RaspiPin.GPIO_07;
		case 8:
			return RaspiPin.GPIO_08;
		case 9:
			return RaspiPin.GPIO_09;
		case 10:
			return RaspiPin.GPIO_10;
		case 11:
			return RaspiPin.GPIO_11;
		case 12:
			return RaspiPin.GPIO_12;
		case 13:
			return RaspiPin.GPIO_13;
		case 14:
			return RaspiPin.GPIO_14;
		case 15:
			return RaspiPin.GPIO_15;
		case 16:
			return RaspiPin.GPIO_16;
		case 17:
			return RaspiPin.GPIO_17;
		case 18:
			return RaspiPin.GPIO_18;
		case 19:
			return RaspiPin.GPIO_19;
		case 20:
			return RaspiPin.GPIO_20;
		case 21:
			return RaspiPin.GPIO_21;
		case 22:
			return RaspiPin.GPIO_22;
		case 23:
			return RaspiPin.GPIO_23;
		case 24:
			return RaspiPin.GPIO_24;
		case 25:
			return RaspiPin.GPIO_25;
		case 26:
			return RaspiPin.GPIO_26;
		case 27:
			return RaspiPin.GPIO_27;
		case 28:
			return RaspiPin.GPIO_28;
		case 29:
			return RaspiPin.GPIO_29;
		case 30:
			return RaspiPin.GPIO_30;
		case 31:
			return RaspiPin.GPIO_31;
		}
		return null;
	}

	/**
	 * Get pin number.
	 * 
	 * @param _pin
	 * @return
	 */
	public static int pinNumberOf(Pin _pin) {
		if (_pin.equals(RaspiPin.GPIO_00)) {
			return 0;
		} else if (_pin.equals(RaspiPin.GPIO_01)) {
			return 1;
		} else if (_pin.equals(RaspiPin.GPIO_02)) {
			return 2;
		} else if (_pin.equals(RaspiPin.GPIO_03)) {
			return 3;
		} else if (_pin.equals(RaspiPin.GPIO_04)) {
			return 4;
		} else if (_pin.equals(RaspiPin.GPIO_05)) {
			return 5;
		} else if (_pin.equals(RaspiPin.GPIO_06)) {
			return 6;
		} else if (_pin.equals(RaspiPin.GPIO_07)) {
			return 7;
		} else if (_pin.equals(RaspiPin.GPIO_08)) {
			return 8;
		} else if (_pin.equals(RaspiPin.GPIO_09)) {
			return 9;
		} else if (_pin.equals(RaspiPin.GPIO_10)) {
			return 10;
		} else if (_pin.equals(RaspiPin.GPIO_11)) {
			return 11;
		} else if (_pin.equals(RaspiPin.GPIO_12)) {
			return 12;
		} else if (_pin.equals(RaspiPin.GPIO_13)) {
			return 13;
		} else if (_pin.equals(RaspiPin.GPIO_14)) {
			return 14;
		} else if (_pin.equals(RaspiPin.GPIO_15)) {
			return 15;
		} else if (_pin.equals(RaspiPin.GPIO_16)) {
			return 16;
		} else if (_pin.equals(RaspiPin.GPIO_17)) {
			return 17;
		} else if (_pin.equals(RaspiPin.GPIO_18)) {
			return 18;
		} else if (_pin.equals(RaspiPin.GPIO_19)) {
			return 19;
		} else if (_pin.equals(RaspiPin.GPIO_20)) {
			return 20;
		} else if (_pin.equals(RaspiPin.GPIO_21)) {
			return 21;
		} else if (_pin.equals(RaspiPin.GPIO_22)) {
			return 22;
		} else if (_pin.equals(RaspiPin.GPIO_23)) {
			return 23;
		} else if (_pin.equals(RaspiPin.GPIO_24)) {
			return 24;
		} else if (_pin.equals(RaspiPin.GPIO_25)) {
			return 25;
		} else if (_pin.equals(RaspiPin.GPIO_26)) {
			return 26;
		} else if (_pin.equals(RaspiPin.GPIO_27)) {
			return 27;
		} else if (_pin.equals(RaspiPin.GPIO_28)) {
			return 28;
		} else if (_pin.equals(RaspiPin.GPIO_29)) {
			return 29;
		} else if (_pin.equals(RaspiPin.GPIO_30)) {
			return 30;
		} else if (_pin.equals(RaspiPin.GPIO_31)) {
			return 31;
		}
		return -1;
	}
}
