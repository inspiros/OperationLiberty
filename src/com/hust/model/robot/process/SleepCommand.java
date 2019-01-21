package com.hust.model.robot.process;

import java.util.concurrent.TimeUnit;

public class SleepCommand extends Command {

	public long sleepTime;

	public TimeUnit timeUnit;

	public SleepCommand(long sleepTime, TimeUnit timeUnit) {
		this.sleepTime = sleepTime;
		this.timeUnit = timeUnit;
	}

	@Override
	public void execute() throws InterruptedException {
		try {
			switch (timeUnit) {
			case DAYS:
				TimeUnit.DAYS.sleep(sleepTime);
				break;
			case HOURS:
				TimeUnit.HOURS.sleep(sleepTime);
				break;
			case MICROSECONDS:
				TimeUnit.MICROSECONDS.sleep(sleepTime);
				break;
			case MILLISECONDS:
				TimeUnit.MILLISECONDS.sleep(sleepTime);
				break;
			case MINUTES:
				TimeUnit.MINUTES.sleep(sleepTime);
				break;
			case NANOSECONDS:
				TimeUnit.NANOSECONDS.sleep(sleepTime);
				break;
			case SECONDS:
				TimeUnit.SECONDS.sleep(sleepTime);
				break;
			default:
				break;
			}
		} catch (InterruptedException e) {
			throw e;
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = getStringBuilder().append("Sleep for ").append(Long.toString(sleepTime))
				.append(" ");
		switch (timeUnit) {
		case DAYS:
			stringBuilder.append("day");
			break;
		case HOURS:
			stringBuilder.append("hour");
			break;
		case MICROSECONDS:
			stringBuilder.append("microsecond");
			break;
		case MILLISECONDS:
			stringBuilder.append("millisecond");
			break;
		case MINUTES:
			stringBuilder.append("minute");
			break;
		case NANOSECONDS:
			stringBuilder.append("nanosecond");
			break;
		case SECONDS:
			stringBuilder.append("second");
			break;
		default:
			break;
		}
		if (sleepTime > 1) {
			stringBuilder.append("s");
		}
		return stringBuilder.toString();
	}
}
