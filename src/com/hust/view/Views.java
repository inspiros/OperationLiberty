package com.hust.view;

import com.hust.core.Configurations;
import com.hust.model.Models;
import com.hust.view.demo.PWindow;
import com.hust.view.javafx.FxApplication;

public class Views {

	public Models data;

	public PWindow demo;
	public FxApplication hmi;

	public Views(Models data) {
		this.data = data;
	}

	public Views setupViews() {
		// Might run on headless devices.
		try {
			Thread demoLauncher = new Thread(() -> {
				if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("demo"))
						&& Configurations.PROPERTIES.getProperty("platform") != "LINUX") {
					demo = new PWindow();
				}
			});
			demoLauncher.start();
			if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("view"))) {
				hmi = FxApplication.getInstance();
			}
			demoLauncher.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}
}
