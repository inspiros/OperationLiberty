package com.hust.actuator;

import java.util.concurrent.TimeUnit;

import com.hust.core.Configurations;
import com.hust.utils.concurrent.RequestTransferQueue;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class LewansoulLX16A {

	public static final byte SERVO_MODE = 0;
	public static final byte MOTOR_MODE = 1;

	public static final byte FRAME_HEADER = 0x55;
	public static final byte MOVE_TIME_WRITE = 1;
	public static final byte MOVE_TIME_READ = 2;
	public static final byte MOVE_TIME_WAIT_WRITE = 7;
	public static final byte MOVE_TIME_WAIT_READ = 8;
	public static final byte MOVE_START = 11;
	public static final byte MOVE_STOP = 12;
	public static final byte ID_WRITE = 13;
	public static final byte ID_READ = 14;
	public static final byte ANGLE_OFFSET_ADJUST = 17;
	public static final byte ANGLE_OFFSET_WRITE = 18;
	public static final byte ANGLE_OFFSET_READ = 19;
	public static final byte ANGLE_LIMIT_WRITE = 20;
	public static final byte ANGLE_LIMIT_READ = 21;
	public static final byte VIN_LIMIT_WRITE = 22;
	public static final byte VIN_LIMIT_READ = 23;
	public static final byte TEMP_MAX_LIMIT_WRITE = 24;
	public static final byte TEMP_MAX_LIMIT_READ = 25;
	public static final byte TEMP_READ = 26;
	public static final byte VIN_READ = 27;
	public static final byte POS_READ = 28;
	public static final byte MODE_WRITE = 29;
	public static final byte MODE_READ = 30;
	public static final byte LOAD_OR_UNLOAD_WRITE = 31;
	public static final byte LOAD_OR_UNLOAD_READ = 32;
	public static final byte LED_CTRL_WRITE = 33;
	public static final byte LED_CTRL_READ = 34;
	public static final byte LED_ERROR_WRITE = 35;
	public static final byte LED_ERROR_READ = 36;

	public static final byte BROADCAST_ID = (byte) 0xFE;

	protected SerialPort serialPort;

	/**
	 * Timout.
	 */
	protected long readbackTimeout = Configurations.readbackTimeout;

	/**
	 * A transfer queue for transferring results of serial listener.
	 */
	private RequestTransferQueue<Object> queue;

//	private BinarySemaphore readSemaphore = new BinarySemaphore(false);

	/**
	 * Default constructor.
	 * 
	 * @throws SerialPortException
	 */
	public LewansoulLX16A() throws SerialPortException {
		queue = new RequestTransferQueue<>();
	}

	/**
	 * Set up call back functions of serial port.
	 * 
	 * @throws SerialPortException
	 */
	private void setupSerialCallback() throws SerialPortException {
		// Put data to handlers.
		serialPort.addEventListener(new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.isRXCHAR() && event.getEventValue() > 0) {
					try {
						byte[] bytes = serialPort.readBytes();
						handleReadback(bytes);
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Autoclose serial port.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					serialPort.closePort();
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Automatically connect to a port.
	 * 
	 * @param portname
	 * @throws SerialPortException
	 */
	public void autoConnect() {
		String[] portNames = SerialPortList.getPortNames();

		for (int i = 0; i < portNames.length; i++) {
			try {
				serialPort = new SerialPort(portNames[i]);
				serialPort.openPort();
				serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				break;
			} catch (SerialPortException e) {
			}
		}
		if (serialPort == null || !serialPort.isOpened()) {
			System.err.println("All serial ports busy! Using virtual debug actuator...");
			return;
		}

		if (serialPort.isOpened()) {
			try {
				setupSerialCallback();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Manually connect to a port.
	 * 
	 * @param portname
	 * @throws SerialPortException
	 */
	public void connect(String portname) throws SerialPortException {
		serialPort = new SerialPort(portname);
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		if (serialPort == null || !serialPort.isOpened()) {
			System.err.println("All serial ports busy! Using virtual debug actuator...");
			return;
		}

		if (serialPort.isOpened()) {
			setupSerialCallback();
		}
	}

	/**
	 * Calculate checksum of protocol package.
	 * 
	 * @param bytes The package.
	 * @return The checksum of that package
	 */
	public static byte checkSum(byte[] bytes) {
		int temp = 0;
		for (int i = 2; i < Byte.toUnsignedInt(bytes[3]) + 2; i++) {
			temp += Byte.toUnsignedInt(bytes[i]);
		}
		temp = ~temp;
		return (byte) temp;
	}

	/**
	 * Calculate and modify the last element of a protocol package to it's checksum.
	 * 
	 * @param bytes The package.
	 */
	private void setCheckSum(byte[] bytes) {
		int temp = 0;
		for (int i = 2; i < bytes.length - 1; i++) {
			temp += Byte.toUnsignedInt(bytes[i]);
		}
		bytes[bytes.length - 1] = (byte) ~temp;
	}

	/**
	 * Convert integer to lowbyte of a 2 bytes number.
	 * 
	 * @param i The integer.
	 * @return The lowbyte.
	 */
	public static byte getLowByte(int i) {
		return (byte) (i & 0xFF);
	}

	/**
	 * Convert integer to highbyte of a 2 bytes number.
	 * 
	 * @param i The integer.
	 * @return The highbyte.
	 */
	public static byte getHighByte(int i) {
		return (byte) ((i >> 8) & 0xFF);
	}

	/**
	 * Convert a 2 bytes number to an integer value.
	 * 
	 * @param high The highbyte.
	 * @param low  The lowbyte.
	 * @return The integer value.
	 */
	public static int getIntVal(byte high, byte low) {
		return ((Byte.toUnsignedInt(high)) << 8) | Byte.toUnsignedInt(low);
	}

	/**
	 * Utility function to write bytes to the servos and catch exception.
	 * 
	 * @param bytes The bytes to send.
	 * @return Success or not.
	 */
	public boolean serialWrite(byte[] bytes) {
		try {
			return serialPort.writeBytes(bytes);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Move the servo to specified position in specified time.
	 * 
	 * @param id       The id of the servo.
	 * @param position The desired position.
	 * @param time     The desired operation time.
	 */
	public void move(int id, int position, int time) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MOVE_TIME_WRITE;
		bytes[5] = getLowByte(position);
		bytes[6] = getHighByte(position);
		bytes[7] = getLowByte(time);
		bytes[8] = getHighByte(time);
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the move command of the servo.
	 * 
	 * @param id       The id of the servo.
	 * @param position The desired position.
	 * @param time     The desired operation time.
	 */
	public byte[] getMoveCmd(int id, int position, int time) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MOVE_TIME_WRITE;
		bytes[5] = getLowByte(position);
		bytes[6] = getHighByte(position);
		bytes[7] = getLowByte(time);
		bytes[8] = getHighByte(time);
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Move the servo to specified position after specified waiting time.
	 * 
	 * @param id       The id of the servo.
	 * @param position The desired position.
	 * @param time     The wait time.
	 */
	public void moveTimeWaitCmd(int id, int position, int time) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MOVE_TIME_WAIT_WRITE;
		bytes[5] = getLowByte(position);
		bytes[6] = getHighByte(position);
		bytes[7] = getLowByte(time);
		bytes[8] = getHighByte(time);
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the move time wait command of the servo.
	 * 
	 * @param id       The id of the servo.
	 * @param position The desired position.
	 * @param time     The wait time.
	 */
	public byte[] getMoveTimeWaitCmd(int id, int position, int time) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MOVE_TIME_WAIT_WRITE;
		bytes[5] = getLowByte(position);
		bytes[6] = getHighByte(position);
		bytes[7] = getLowByte(time);
		bytes[8] = getHighByte(time);
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read the current position of a servo.
	 * 
	 * @param id The id of the servo.
	 */
	public Integer readPosition(int id) throws InterruptedException {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = POS_READ;
		setCheckSum(bytes);

		queue.requested = true;
		serialWrite(bytes);
		Integer res = (Integer) queue.poll(Configurations.readbackTimeout, TimeUnit.MILLISECONDS);
		queue.requested = false;
		return res;
	}

	/**
	 * Send the read position command without trying to read. Use in
	 * multi-threading.
	 * 
	 * @param id The id of the servo.
	 */
	public void requestReadPosition(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = POS_READ;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the read position command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getReadPositionCmd(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = POS_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Start movement of a waiting servo. Use in coordination with
	 * <code>moveTimeWait();</code>.
	 * 
	 * @param id The id of servo.
	 */
	public void start(int id) {
		byte[] bytes = new byte[6];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = MOVE_START;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the start movement command.
	 * 
	 * @param id The id of servo.
	 */
	public byte[] getStartCmd(int id) {
		byte[] bytes = new byte[6];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = MOVE_START;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Immediately stop a servo at current position.
	 * 
	 * @param id The id of the servo.
	 */
	public void stop(int id) {
		byte[] bytes = new byte[6];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = MOVE_STOP;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the stop command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getStopCmd(int id) {
		byte[] bytes = new byte[6];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = MOVE_STOP;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Set id of a servo to a new value.
	 * 
	 * @param oldId The old id of the servo.
	 * @param newId The new id of the servo.
	 */
	public void setId(int oldId, int newId) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) oldId;
		bytes[3] = 4;
		bytes[4] = ID_WRITE;
		bytes[5] = (byte) newId;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the set id command.
	 * 
	 * @param oldId The old id of the servo.
	 * @param newId The new id of the servo.
	 */
	public byte[] getSetIdCmd(int oldId, int newId) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) oldId;
		bytes[3] = 4;
		bytes[4] = ID_WRITE;
		bytes[5] = (byte) newId;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Set id of servo by broadcasting. Use to set id of a single plugged servo with
	 * unknown id.
	 * 
	 * @param newId The new id of the servo.
	 */
	public void setId(int newId) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = BROADCAST_ID;
		bytes[3] = 4;
		bytes[4] = ID_WRITE;
		bytes[5] = (byte) newId;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the broadcast set id command.
	 * 
	 * @param newId The new id of the servo.
	 */
	public byte[] getSetIdCmd(int newId) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = BROADCAST_ID;
		bytes[3] = 4;
		bytes[4] = ID_WRITE;
		bytes[5] = (byte) newId;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read id of a single plugged servo using broadcast id.
	 * 
	 * @throws InterruptedException
	 */
	public Integer readId() throws InterruptedException {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = BROADCAST_ID;
		bytes[3] = 3;
		bytes[4] = ID_READ;
		setCheckSum(bytes);

		queue.requested = true;
		serialWrite(bytes);
		Integer res = (Integer) queue.poll(Configurations.readbackTimeout, TimeUnit.MILLISECONDS);
		queue.requested = false;
		return res;
	}

	/**
	 * Request readback id of a single plugged servo using broadcast id.
	 */
	public void requestReadId() {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = BROADCAST_ID;
		bytes[3] = 3;
		bytes[4] = ID_READ;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get broadcast read id command.
	 */
	public byte[] getReadIdCmd() {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = BROADCAST_ID;
		bytes[3] = 3;
		bytes[4] = ID_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Set operating mode of a servo. Mode can be either:
	 * <p>
	 * 0 for servo mode.
	 * <p>
	 * 1 for motor mode.
	 * 
	 * @param id    The id of the servo.
	 * @param mode  The desired mode 0 or 1.
	 * @param speed The rotation speed applying only to motor mode.
	 */
	public void setMode(int id, byte mode, int speed) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MODE_WRITE;
		bytes[5] = mode;
		bytes[6] = 0;
		bytes[7] = getLowByte(speed);
		bytes[8] = getHighByte(speed);
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the set mode command. Mode can be either:
	 * <p>
	 * 0 for servo mode.
	 * <p>
	 * 1 for motor mode.
	 * 
	 * @param id    The id of the servo.
	 * @param mode  The desired mode 0 or 1.
	 * @param speed The rotation speed applying only to motor mode.
	 */
	public byte[] getSetModeCmd(int id, byte mode, int speed) {
		byte[] bytes = new byte[10];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 7;
		bytes[4] = MODE_WRITE;
		bytes[5] = mode;
		bytes[6] = 0;
		bytes[7] = getLowByte(speed);
		bytes[8] = getHighByte(speed);
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Load the servo.
	 * 
	 * @param id The id of the servo.
	 */
	public void load(int id) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LOAD_OR_UNLOAD_WRITE;
		bytes[5] = 1;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the load command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getLoadCmd(int id) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LOAD_OR_UNLOAD_WRITE;
		bytes[5] = 1;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Unload the servo.
	 * 
	 * @param id The id of the servo.
	 */
	public void unload(int id) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LOAD_OR_UNLOAD_WRITE;
		bytes[5] = 0;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the unload command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getUnloadCmd(int id) {
		byte[] bytes = new byte[7];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LOAD_OR_UNLOAD_WRITE;
		bytes[5] = 0;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read load state of a servo. TODO
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getReadLoadCmd(int id) {
		byte[] bytes = new byte[5];
		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = LOAD_OR_UNLOAD_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read temperature of a servo.
	 * 
	 * @param id The id of the servo.
	 * @throws InterruptedException
	 */
	public Integer readTemperature(int id) throws InterruptedException {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = TEMP_READ;
		setCheckSum(bytes);

		queue.requested = true;
		serialWrite(bytes);
		Integer res = (Integer) queue.poll(Configurations.readbackTimeout, TimeUnit.MILLISECONDS);
		queue.requested = false;
		return res;
	}

	/**
	 * Send read temperature command without trying to read. Use in multi-threading.
	 * 
	 * @param id The id of the servo.
	 */
	public void requestReadTemperature(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = TEMP_READ;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the read temperature command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getReadTemperatureCmd(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = TEMP_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read the input voltage of a servo.
	 * 
	 * @param id The id of the servo.
	 * @throws InterruptedException
	 */
	public Float readVoltage(int id) throws InterruptedException {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = VIN_READ;
		setCheckSum(bytes);

		queue.requested = true;
		serialWrite(bytes);
		Float res = (Float) queue.poll(Configurations.readbackTimeout, TimeUnit.MILLISECONDS);
		queue.requested = false;
		return res;
	}

	/**
	 * Send read input voltage command without trying to read. Use in
	 * multi-threading.
	 * 
	 * @param id The id of the servo.
	 */
	public void requestReadVoltage(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = VIN_READ;
		setCheckSum(bytes);

		serialWrite(bytes);
	}

	/**
	 * Get the read input voltage command.
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getReadVoltageCmd(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = VIN_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Turn on the led on a servo. TODO
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getSetLedCtrlOnCmd(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LED_CTRL_WRITE;
		bytes[5] = 0;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Turn off the led on a servo. TODO
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getSetLedCtrlOffCmd(int id) {
		byte[] bytes = new byte[6];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 4;
		bytes[4] = LED_CTRL_WRITE;
		bytes[5] = 1;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	/**
	 * Read the led status on a servo. TODO
	 * 
	 * @param id The id of the servo.
	 */
	public byte[] getReadLedCtrlCmd(int id) {
		byte[] bytes = new byte[5];

		bytes[0] = bytes[1] = FRAME_HEADER;
		bytes[2] = (byte) id;
		bytes[3] = 3;
		bytes[4] = LED_CTRL_READ;
		setCheckSum(bytes);

		// serialWrite(bytes);
		return bytes;
	}

	public void handleReadback(byte[] bytes) {
//		for (byte b : bytes) {
//			System.out.println(Byte.toUnsignedInt(b));
//		}
		if (bytes == null || bytes.length < 7) {
			return;
		}

		Object val = null;
		switch (bytes[4]) {
		case ID_READ:
			// Id
			val = Byte.toUnsignedInt(bytes[2]);
			System.out.println("Id: " + val);
			break;
		case TEMP_READ:
			// Temp
			val = Byte.toUnsignedInt(bytes[5]);
			System.out.println("Temperature: " + val);
			break;
		case VIN_READ:
			// Vin
			val = (float) getIntVal(bytes[6], bytes[5]) / 1000.0f;
			System.out.println("Voltage: " + val);
			break;
		case POS_READ:
			// Pos
			val = getIntVal(bytes[6], bytes[5]);
			System.out.println("Position: " + val);
			break;
		case MODE_READ:
			// Mode
			val = Byte.toUnsignedInt(bytes[5]);
			System.out.println("Mode: " + val);
			break;
		case LED_CTRL_READ:
			// LED
			val = Byte.toUnsignedInt(bytes[5]);
			System.out.println("Control Led: " + val);
			break;
		}

		// Insert to transfer queue if requested.
		if (queue.requested && val != null) {
			queue.tryTransfer(val);
		}
	}
}
