package com.hust.model.robot.process;

import com.hust.model.robot.kinematics.KinematicsSolver.IKMethod;
import com.hust.utils.algebra.FloatVector3;

public class PositionCommand extends Command {

	FloatVector3 newPosition;

	public PositionCommand(FloatVector3 newPosition) {
		this.newPosition = newPosition;
	}

	@Override
	public void execute() {
		process.model.moveToPosition(newPosition, IKMethod.JACOBIAN_TRANSPOSE);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = getStringBuilder().append("Move to [").append(newPosition.x).append(",")
				.append(newPosition.y).append(",").append(newPosition.z).append("]");
		return stringBuilder.toString();
	}
}
