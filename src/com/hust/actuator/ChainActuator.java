package com.hust.actuator;

import com.hust.core.Configurations;
import com.hust.model.Models;
import com.hust.utils.Utils;
import com.hust.utils.concurrent.FunnelService;
import com.pi4j.io.serial.RaspberryPiSerial;
import com.pi4j.io.serial.Serial;

import jssc.SerialPortException;

/**
 * A class for controlling actuators representing a joint.
 * <p>
 * For other branch of servos, just extends another class.
 * </p>
 * 
 * @author Inspiros
 *
 */
public class ChainActuator extends LewansoulLX16A {

	/**
	 * A funnel is needed to manage communication of bunch of threads with a single
	 * serial port.
	 */
	public FunnelService serialService = new FunnelService();

	public Models model;

	/**
	 * Constructor.
	 * 
	 * @param model
	 * @throws SerialPortException
	 */
	public ChainActuator(Models model) throws SerialPortException {
		this.model = model;
		setupConnection();
		setupServices();
	}

	public int toActuatorId(int id) {
		return id + 1;
	}

	public int toPosition(float angleDegs) {
		return 500 + Math.round(angleDegs * 500 / 120);
	}

	public float toAngleDegs(int pos) {
		return ((float) pos - 500) / 500 * 120;
	}

	/**
	 * Connecty boiz.
	 * 
	 * @return itself.
	 */
	public ChainActuator setupConnection() {
		if (Configurations.PROPERTIES.get("platform").equals("LINUX")) {
			// Native Raspberry Pi.
			try {
				connect(Serial.FIRST_USB_COM_PORT);
			} catch (Exception e0) {
				try {
					connect(Serial.SECOND_USB_COM_PORT);
				} catch (Exception e1) {
					try {
						connect(Serial.DEFAULT_COM_PORT);
					} catch (Exception e2) {
						try {
							connect(RaspberryPiSerial.AMA0_COM_PORT);
						} catch (Exception e3) {
							try {
								connect(RaspberryPiSerial.S0_COM_PORT);
							} catch (Exception e4) {
								autoConnect();
							}
						}
					}
				}
			}
		} else {
			// Test machine.
			autoConnect();
		}

		return this;
	}

	/**
	 * Sendy boiz.
	 * 
	 * @return itself.
	 */
	public ChainActuator setupServices() {
		if (model.robot == null) {
			return this;
		}

		if (serialPort != null && serialPort.isOpened()) {
			model.robot.bones.forEach(bone -> {
				// Read wake up position.
				serialService.runLater(() -> {
					try {
						Integer pos = readPosition(bone.id + 1);
						if (pos != null) {
							System.out.println(pos + " " + toAngleDegs(pos));
							bone.joint.setAngleDegs(toAngleDegs(pos));
						}
					} catch (InterruptedException e) {
					}
				});

				// Schedule.
				serialService.runScheduled(() -> {
					try {
						Integer temperature = readTemperature(bone.id + 1);
						model.vault.dataSeries.get(bone.id).series.get("temperature").add(temperature);
					} catch (InterruptedException e) {
					}
				});

				// Move command.
				bone.joint.angle.addListener((observable, oldValue, newValue) -> {
					if (!bone.joint.locked) {
						int pos = toPosition(newValue.floatValue());
						byte[] moveCmd = getMoveCmd(bone.id + 1, pos, 0);
						serialService.runLater(() -> {
							serialWrite(moveCmd);
						});
					}
				});
			});

		} else {
			// Virtual actuator.
			model.robot.bones.forEach(bone -> {
				serialService.runScheduled(() -> {
					System.out.println("Reading: " + new Integer(bone.id + 1));

					Integer temperature = Utils.randRange(0, 100);
					model.vault.dataSeries.get(bone.id).series.get("temperature").add(temperature);
				});
				bone.joint.angle.addListener((observable, oldValue, newValue) -> {
					if (!bone.joint.locked) {
						serialService.runLater(() -> System.out
								.println("Moving: " + new Integer(bone.id + 1) + " (" + newValue.floatValue() + ")"));
					}
				});
			});
		}
		return this;
	}

}
