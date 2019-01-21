package com.hust.model.robot.process;

public class JumpCommand extends Command {

	int targetId;

	public JumpCommand(int targetId) {
		this.targetId = targetId;
	}

	@Override
	public void execute() {
		process.iterator = process.commands.listIterator(targetId);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = getStringBuilder().append("Jump to ").append(Integer.toString(targetId));
		return stringBuilder.toString();
	}
}
