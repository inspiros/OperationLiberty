package com.hust.view.javafx;

import com.hust.core.Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import jfxtras.styles.jmetro8.JMetro.Style;

public class FxApplication extends Application {

	/**
	 * Instance of this app.
	 */
	public static FxApplication instance;

	/**
	 * Controller class instance.
	 */
	public FxApplicationController controller;

	/**
	 * Primary window.
	 */
	public Stage stage;

	/**
	 * Primary scene.
	 */
	public Scene scene;

	public FxApplication() {
		instance = this;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
		Parent parent = loader.load();
		controller = loader.getController();

		scene = new Scene(parent);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Operation Liberty");
		try {
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icon.png")));
		} catch (Exception e) {
		}

		// Apply Metro theme.
		JMetro jmetro = new JMetro(Style.DARK);
		jmetro.applyTheme(scene);

		scene.getStylesheets().addAll(getClass().getResource("Application.css").toExternalForm());

		primaryStage.setOnCloseRequest((e) -> {
			if (Main.demo != null) {
				Platform.runLater(() -> {
					Main.demo.exit();
				});
			} else {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.show();
	}

	public synchronized static FxApplication getInstance() {
		if (instance == null) {
			Thread launcher = new Thread(new Runnable() {
				@Override
				public void run() {
					Application.launch(FxApplication.class);
				}
			});
			launcher.start();
		}
		while (instance == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return instance;
	}
}
