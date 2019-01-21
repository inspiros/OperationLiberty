package com.hust.model.robot.process;

import java.util.ArrayList;

import com.hust.utils.FloatValidator;

public class JointCommand extends Command {

	private ArrayList<Float> newAngles = new ArrayList<>();

	public JointCommand(ArrayList<Float> newAngles) {
		this.newAngles = newAngles;
	}

	public JointCommand(int id, float newAngle) {
		for (int i = 0; i < id; i++) {
			newAngles.add(Float.NaN);
		}
		newAngles.add(newAngle);
	}

	@Override
	public void execute() {
		process.model.robot.prepareTargetsDegs(newAngles);
		process.model.robot.actuate();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = getStringBuilder().append("Rotate to [");
		for (int i = 0; i < newAngles.size() - 1; i++) {
			Float newAngle = newAngles.get(i);
			if (FloatValidator.validate(newAngle)) {
				stringBuilder.append(newAngle);
			} else {
				stringBuilder.append("-");
			}
			stringBuilder.append(",");
		}

		Float newAngle = newAngles.get(newAngles.size() - 1);
		if (FloatValidator.validate(newAngle)) {
			stringBuilder.append(newAngle);
		} else {
			stringBuilder.append("-");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
