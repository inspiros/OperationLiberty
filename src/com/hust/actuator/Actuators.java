package com.hust.actuator;

import com.hust.core.Configurations;
import com.hust.model.Models;

import jssc.SerialPortException;

public class Actuators {

	Models data;

	ChainActuator chainActuator;

	public Actuators(Models data) {
		this.data = data;
	}

	public Actuators setupActuators() {
		if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("actuator"))) {
			try {
				this.chainActuator = new ChainActuator(data);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}

		Configurations.MODULES_INITIALIZATION.get("actuator").countDown();
		return this;
	}
}
