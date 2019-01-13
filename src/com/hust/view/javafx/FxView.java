package com.hust.view.javafx;

import java.io.IOException;

import com.hust.core.Configurations;
import com.hust.view.Views;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.styles.jmetro8.JMetro;
import jfxtras.styles.jmetro8.JMetro.Style;

public class FxView extends HApplication {

	/**
	 * Instance of this app.
	 */
	public static FxView instance;

	/**
	 * Model.
	 */
	public Views view;

	/**
	 * Controller class instance.
	 */
	public ApplicationController controller;

	/**
	 * Primary window.
	 */
	public Stage stage;

	/**
	 * Preloader.
	 */
	public Stage splash;

	/**
	 * Primary scene.
	 */
	public Scene scene;

	/**
	 * Splash scene.
	 */
	public Pane splashPane;

	public FxView() {
		instance = this;
		Configurations.MODULES_INITIALIZATION.get("view").countDown();
	}

	@Override
	public void start(Stage splashStage) throws Exception {

		showSplash(splashStage);

		Thread transit = new Thread(() -> {
			try {
				// Await model initialization.
				Configurations.MODULES_INITIALIZATION.get("model").await();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
				Parent parent = loader.load();
				controller = loader.getController();

				scene = new Scene(parent);

				// Apply Metro theme.
				Platform.runLater(() -> {

					this.stage = new Stage();
					this.stage.setScene(scene);
					this.stage.setTitle("Operation Liberty");
					this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icon.png")));

					JMetro jmetro = new JMetro(Style.DARK);
					jmetro.applyTheme(scene);

					scene.getStylesheets().addAll(getClass().getResource("Application.css").toExternalForm());

					if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("view.fullscreen"))
							|| (Configurations.PROPERTIES.getProperty("platform").equals("LINUX") && Boolean
									.parseBoolean(Configurations.PROPERTIES.getProperty("view.native-fullscreen")))) {
						this.stage.setFullScreen(true);
					}

					// this.stage.centerOnScreen();

					this.stage.setOnCloseRequest((e) -> {
						if (view.demo != null) {
							view.demo.exit();
						} else {
							System.exit(0);
						}
					});
				});

				FadeTransition fadeSplash = new FadeTransition(Duration.seconds(2), this.splashPane);
				fadeSplash.setFromValue(1.0);
				fadeSplash.setToValue(0.0);
				fadeSplash.setOnFinished(actionEvent -> this.splash.hide());

				// Await other modules initialization.
				Configurations.MODULES_INITIALIZATION.get("actuator").await();
				Configurations.MODULES_INITIALIZATION.get("demo").await();

				System.out.println("Here");
				Platform.runLater(() -> {
					fadeSplash.play();
					this.stage.show();
					centerize(this.stage);
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		transit.start();
	}

	private void showSplash(final Stage splashStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Preloader.fxml"));
		this.splashPane = loader.load();

		this.splash = splashStage;

		Scene splashScene = new Scene(this.splashPane, Color.TRANSPARENT);
		this.splash.setScene(splashScene);

		// this.splash.centerOnScreen();

		this.splash.setAlwaysOnTop(true);
		this.splash.initStyle(StageStyle.UNDECORATED);
		this.splash.initStyle(StageStyle.TRANSPARENT);

		if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("view.splash.fullscreen"))
				|| (Configurations.PROPERTIES.getProperty("platform").equals("LINUX") && Boolean
						.parseBoolean(Configurations.PROPERTIES.getProperty("view.splash.native-fullscreen")))) {
			this.splash.setFullScreen(true);
		}
		splashStage.show();
		centerize(this.splash);
	}

	public synchronized static FxView getInstance(Views view) {
		if (instance == null) {
			Thread setupModel = new Thread(() -> {
				try {
					Configurations.MODULES_INITIALIZATION.get("view").await();
					instance.view = view;
				} catch (InterruptedException e) {
				}
			});
			setupModel.start();
			Application.launch(FxView.class);
			try {
				setupModel.join();
			} catch (InterruptedException e) {
			}
		}
		return instance;
	}
}
