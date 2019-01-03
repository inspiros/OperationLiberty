package com.hust.model.robot;

import java.util.ArrayList;
import java.util.List;

public abstract class Commander {
	private List<CommandListener> commandListeners = new ArrayList<CommandListener>();
	
	public void addCommandListener(CommandListener commandListener) {
		commandListeners.add(commandListener);
	}
	
	public void sendCommand(String command) {
		for (CommandListener commandListener : commandListeners) {
			commandListener.handleCommand(command);
		}
	}
}
