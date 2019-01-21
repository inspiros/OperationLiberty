package com.hust.model.robot.process;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hust.model.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Process {

	public Models model;

	public ObservableList<Command> commands = FXCollections.observableArrayList();

	public Iterator<Command> iterator;

	public ExecutorService processExecutorService = Executors.newSingleThreadExecutor();
	public Future<Void> processFuture;

	public String name;

	public void addCommand(Command command) {
		command.process = this;
		commands.add(command);
		command.id = commands.size() - 1;
	}

	public void start() {
		iterator = commands.listIterator();
		// Start
		processFuture = processExecutorService.submit(() -> {
			while (iterator.hasNext()) {
				try {
				Command cmd = iterator.next();
				cmd.execute();
				} catch (InterruptedException e) {
					break;
				}
			}
			processFuture = null;
			return null;
		});
	}

	public void stop() {
		if (processFuture != null) {
			processFuture.cancel(true);
			processFuture = null;
		}
	}

	@Override
	public String toString() {
		if (name != null && name.length() > 0) {
			return name;
		}
		return "Process";
	}
}
