package com.hust.view.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.hust.model.Models;
import com.hust.model.robot.process.JumpCommand;
import com.hust.model.robot.process.JointCommand;
import com.hust.model.robot.process.PositionCommand;
import com.hust.model.robot.process.SleepCommand;
import com.hust.utils.algebra.FloatVector3;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class CommandAddDialogController implements Initializable {

	private Models model;

	private ApplicationController appController;

	/*
	 * Dialog:
	 */
	@FXML
	private ToggleGroup commandType = new ToggleGroup();

	@FXML
	private HBox jointCommandHBox, positionCommandHBox, jumpCommandHBox, sleepCommandHBox;

	@FXML
	private JFXRadioButton jointCommand, positionCommand, jumpCommand, sleepCommand;

	@FXML
	private TextField posX, posY, posZ, targetId, sleepTime;

	@FXML
	private JFXButton addCommand;

	private ArrayList<TextField> jointAngles = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.appController = FxView.instance.controller;
		this.model = appController.model;
		initializeDialog();
	}

	private void initializeDialog() {
		commandType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				oldValue.setSelected(true);
			} else {
				String selected = ((JFXRadioButton) commandType.getSelectedToggle()).getAccessibleText();
				if (selected.equals("jointCommand")) {
					jointCommandHBox.setDisable(false);
					positionCommandHBox.setDisable(true);
					jumpCommandHBox.setDisable(true);
					sleepCommandHBox.setDisable(true);
				} else if (selected.equals("positionCommand")) {
					jointCommandHBox.setDisable(true);
					positionCommandHBox.setDisable(false);
					jumpCommandHBox.setDisable(true);
					sleepCommandHBox.setDisable(true);
				} else if (selected.equals("jumpCommand")) {
					jointCommandHBox.setDisable(true);
					positionCommandHBox.setDisable(true);
					jumpCommandHBox.setDisable(false);
					sleepCommandHBox.setDisable(true);
				} else if (selected.equals("sleepCommand")) {
					jointCommandHBox.setDisable(true);
					positionCommandHBox.setDisable(true);
					jumpCommandHBox.setDisable(true);
					sleepCommandHBox.setDisable(false);
				}
			}
		});

		for (int i = 0; i < model.robot.bones.size(); i++) {
			jointAngles.add(new TextField());
		}
		jointCommandHBox.getChildren().addAll(jointAngles);

		addCommand.setOnAction((event) -> {
			String selected = ((JFXRadioButton) commandType.getSelectedToggle()).getAccessibleText();
			if (selected.equals("jointCommand")) {
				ArrayList<Float> newAngles = new ArrayList<>();
				for (int i = 0; i < jointAngles.size(); i++) {
					try {
						newAngles.add(Float.parseFloat(jointAngles.get(i).getText()));
					} catch (Exception e) {
						newAngles.add(Float.NaN);
					}
				}
				appController.processList.getSelectionModel().getSelectedItem()
						.addCommand(new JointCommand(newAngles));
			} else if (selected.equals("positionCommand")) {
				appController.processList.getSelectionModel().getSelectedItem()
						.addCommand(new PositionCommand(new FloatVector3(Float.parseFloat(posX.getText()),
								Float.parseFloat(posY.getText()), Float.parseFloat(posZ.getText()))));
			} else if (selected.equals("jumpCommand")) {
				appController.processList.getSelectionModel().getSelectedItem()
						.addCommand(new JumpCommand(Integer.parseInt(targetId.getText())));
			} else if (selected.equals("sleepCommand")) {
				appController.processList.getSelectionModel().getSelectedItem()
						.addCommand(new SleepCommand(Long.parseLong(sleepTime.getText()), TimeUnit.SECONDS));
			}
		});
	}
}
