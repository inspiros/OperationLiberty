package com.hust.actuator;

import com.hust.model.Models;
import com.hust.utils.Utils;
import com.hust.utils.concurrent.FunnelService;

import jssc.SerialPortException;

/**
 * A class for controlling actuators representing a joint.
 * <p>
 * For other branch of servos, just extends another class.
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

	public ChainActuator(Models model) throws SerialPortException {
		super();
		this.model = model;
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
