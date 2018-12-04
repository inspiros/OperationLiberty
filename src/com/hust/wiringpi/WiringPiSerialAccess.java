package com.hust.wiringpi;

import java.nio.charset.StandardCharsets;

import com.pi4j.wiringpi.Serial;

public class WiringPiSerialAccess {

	public static String DEFAULT_SERIAL = Serial.DEFAULT_COM_PORT;

	private static int serialPort = -1;

	/**
	 * Open default serial port.
	 * 
	 * @param baudrate
	 * @return
	 */
	public static boolean openSerial(int baudrate) {
		serialPort = Serial.serialOpen(DEFAULT_SERIAL, baudrate);
		if (serialPort == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Close default serial port.
	 */
	public static void close(int serial) {
		if (serial == -1) {
			return;
		}
		Serial.serialClose(serial);
	}

	/**
	 * Write a string to default serial.
	 * 
	 * @param str
	 */
	public static void write(String _String) {
		if (serialPort == -1) {
			return;
		}
		Serial.serialPuts(serialPort, _String);
	}

	/**
	 * Write bytes to default serial.
	 * 
	 * @param _bytes
	 */
	public static void write(byte[] _bytes) {
		if (serialPort == -1) {
			return;
		}
		Serial.serialPutBytes(serialPort, _bytes);
	}

	/**
	 * Read bytes from default serial.
	 * 
	 * @return
	 */
	public static byte[] readBytes() {
		if (serialPort == -1 || Serial.serialDataAvail(serialPort) > 0) {
			return null;
		}
		return Serial.serialGetAvailableBytes(serialPort);
	}

	/**
	 * Read String from default serial.
	 * 
	 * @return
	 */
	public static String read() {
		if (serialPort == -1 || Serial.serialDataAvail(serialPort) > 0) {
			return null;
		}
		return new String(Serial.serialGetAvailableBytes(serialPort), StandardCharsets.UTF_8);
	}
}
