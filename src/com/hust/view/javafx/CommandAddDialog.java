package com.hust.view.javafx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CommandAddDialog {

	public Stage dialogStage;

	public CommandAddDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CommandAddDialog.fxml"));
			dialogStage = new Stage();

			Parent dialogRoot = (Parent) loader.load();
			Scene dialogScene;

			dialogScene = new Scene(dialogRoot);

			dialogScene.getStylesheets().add((getClass().getResource("Application.css").toExternalForm()));

			dialogStage.setScene(dialogScene);
			dialogStage.setTitle("Add Command");
			dialogStage.initModality(Modality.APPLICATION_MODAL);

			dialogStage.initStyle(StageStyle.UTILITY);

			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
