package com.hust.view;

import com.hust.core.Configurations;
import com.hust.core.Main;
import com.hust.view.demo.DemoWindow;
import com.hust.view.javafx.FxView;

public class Views {

	public DemoWindow demo;
	public FxView view;

	public Views() {

	}

	public Views setupViews() {
		// Might run on headless devices.
		try {
			Thread demoLauncher = new Thread(() -> {
				if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("demo"))
						&& Configurations.PROPERTIES.getProperty("platform") != "LINUX") {
					try {
						Configurations.MODULES_INITIALIZATION.get("model").await();
						demo = new DemoWindow(Main.model);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			demoLauncher.start();
			if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("view"))) {
				view = FxView.getInstance(this);
			}
			demoLauncher.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}
}
