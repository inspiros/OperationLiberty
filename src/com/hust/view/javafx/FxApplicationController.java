package com.hust.view.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hust.core.Configuration;
import com.hust.core.DataBuffer;
import com.hust.core.Main;
import com.hust.robot.Bone;
import com.hust.robot.Joint;
import com.hust.robot.KinematicsSolver;
import com.hust.utils.Utils;
import com.hust.utils.data.FloatVector3;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.effects.JFXDepthManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxApplicationController implements Initializable {

	private DataBuffer data;

	private Stage stage;

	private Scene scene;

	@FXML
	private VBox vbox;

	@FXML
	private StackPane anchorPane1, anchorPane2, anchorPane3;

	@FXML
	private Pane fkLabelPane, ikLabelPane, jstLabelPane;

	@FXML
	private JFXComboBox<String> jfxComboBox;
	private ArrayList<String> jfxComboBoxChoices;
	private int listenedId = Integer.MIN_VALUE;

	/**
	 * Labels
	 * 
	 */
	@FXML
	private TextField startingPointLbl0, startingPointLbl1, startingPointLbl2, endPointLbl0, endPointLbl1, endPointLbl2,
			targetLbl0, targetLbl1, targetLbl2;

	@FXML
	private ToggleGroup ikToggleGroup, jstToggleGroup;
	/**
	 * IK Choices in toggle group.
	 */
	@FXML
	private JFXToggleNode ikJacobianTranspose, ikFABRIK, ikGradientDescent;
	/**
	 * Joint Space trajectory method Choices in toggle group.
	 */
	@FXML
	private JFXToggleNode jstNone, jstLinear, jstCubic, jstQuintic, jstLSPB, jstLSQB, jstExponential;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		data = Main.dataBuffer;

		initializePositioner();
		initializePanes();
		initiateListeners();
	}

	private void initializePanes() {
		JFXRippler rippler = new JFXRippler(fkLabelPane);
		anchorPane1.getChildren().add(rippler);
		JFXDepthManager.setDepth(anchorPane1, 5);

		rippler = new JFXRippler(ikLabelPane);
		anchorPane2.getChildren().add(rippler);
		JFXDepthManager.setDepth(anchorPane2, 5);

		rippler = new JFXRippler(jstLabelPane);
		anchorPane3.getChildren().add(rippler);
		JFXDepthManager.setDepth(anchorPane3, 5);
		
		// Forward Kinematics Pane
		jfxComboBoxChoices = new ArrayList<>();
		jfxComboBoxChoices.add("None");
		for (int i = 0; i < data.getDof(); i++) {
			jfxComboBoxChoices.add("Khớp " + (i + 1));
		}
		jfxComboBoxChoices.trimToSize();
		jfxComboBox.getItems().addAll(jfxComboBoxChoices);

		jfxComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Minus 1 as we added a None choice
			listenedId = jfxComboBoxChoices.indexOf(newValue) - 1;
			if (listenedId < 0) {
				startingPointLbl0.textProperty().set("");
				startingPointLbl1.textProperty().set("");
				startingPointLbl2.textProperty().set("");
				endPointLbl0.textProperty().set("");
				endPointLbl1.textProperty().set("");
				endPointLbl2.textProperty().set("");
				listenedId = Integer.MIN_VALUE;
			} else {
				reactToPropertyChanges(listenedId, data.getArm().getBone(listenedId).globalTranslation.get());
			}
		});

	}

	private void initiateListeners() {
		for (int i = 0; i < data.getDof(); i++) {
			Bone bone = data.getArm().getBone(i);
			bone.globalTranslation.addListener((observable, oldValue, newValue) -> {
				reactToPropertyChanges(bone.id, newValue);
			});
		}

		// Prevent null inverse kinematics selection.
		ikToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				oldValue.setSelected(true);
			}
		});

		jstToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				oldValue.setSelected(true);
			} else {
				Configuration.setTrajectoryMethod(Joint.TrajectoryMethod.values()[(int) newValue.getUserData()]);
			}
		});

		// Add Content to distinguish between toggle nodes.
		ikJacobianTranspose.setUserData(KinematicsSolver.IKMethod.JACOBIAN.ordinal());
		ikFABRIK.setUserData(KinematicsSolver.IKMethod.FABRIK.ordinal());
		ikGradientDescent.setUserData(KinematicsSolver.IKMethod.GRADIENT_DESCENT.ordinal());

		// And this also.
		jstNone.setUserData(Joint.TrajectoryMethod.NONE.ordinal());
		jstLinear.setUserData(Joint.TrajectoryMethod.LINEAR.ordinal());
		jstCubic.setUserData(Joint.TrajectoryMethod.CUBIC_POLYNOMIAL.ordinal());
		jstQuintic.setUserData(Joint.TrajectoryMethod.QUINTIC_POLYNOMIAL.ordinal());
		jstLSPB.setUserData(Joint.TrajectoryMethod.LSPB.ordinal());
		jstLSQB.setUserData(Joint.TrajectoryMethod.LSQB.ordinal());
		jstExponential.setUserData(Joint.TrajectoryMethod.EXPONENTIAL.ordinal());
	}

	@FXML
	private void onSetTarget(ActionEvent event) {
		FloatVector3 newPosition = new FloatVector3();
		if (targetLbl0.textProperty().get().length() != 0 && targetLbl1.textProperty().get().length() != 0
				&& targetLbl2.textProperty().get().length() != 0) {
			newPosition.x = Float.parseFloat(targetLbl0.textProperty().get());
			newPosition.y = Float.parseFloat(targetLbl1.textProperty().get());
			newPosition.z = Float.parseFloat(targetLbl2.textProperty().get());
		} else {
			throw new RuntimeException("Target position required!");
		}

		// Ready to move.
		data.moveToPosition(newPosition,
				KinematicsSolver.IKMethod.values()[(int) ikToggleGroup.getSelectedToggle().getUserData()]);
	}

	private void reactToPropertyChanges(int id, FloatVector3 newValue) {
		if (id == listenedId) {
			new Thread(() -> {
				// DO SOMETHING WITH CONTROLS ON FX THREAD ACCORDING RESULT OF OVER
				Platform.runLater(() -> {
					endPointLbl0.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.x));
					endPointLbl1.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.y));
					endPointLbl2.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.z));
					if (id == 0) {
						FloatVector3 translation = data.getArm().globalTranslation;
						startingPointLbl0.textProperty().setValue(Utils.DECIMAL_FORMAT.format(translation.x));
						startingPointLbl1.textProperty().setValue(Utils.DECIMAL_FORMAT.format(translation.y));
						startingPointLbl2.textProperty().setValue(Utils.DECIMAL_FORMAT.format(translation.z));
					}
				});
			}).start();
		} else if (id == listenedId - 1) {
			new Thread(() -> {
				// DO SOMETHING WITH CONTROLS ON FX THREAD ACCORDING RESULT OF OVER
				Platform.runLater(() -> {
					startingPointLbl0.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.x));
					startingPointLbl1.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.y));
					startingPointLbl2.textProperty().setValue(Utils.DECIMAL_FORMAT.format(newValue.z));
				});
			}).start();
		}
	}

	private void initializePositioner() {

		for (int i = 0; i < data.getDof(); i++) {
			Bone bone = data.getArm().getBone(i);

			Label label = new Label("Khớp " + (i + 1));
			label.setAlignment(Pos.CENTER);
			label.setStyle("-fx-font-style: normal; -fx-font-size: 12.0pt; -fx-text-fill: white;");

			HBox innerHbox1 = new HBox(10, label);
			innerHbox1.setPadding(new Insets(0, 10, 0, 10));
			innerHbox1.setStyle("-fx-background-color:  #EDA678;");

			SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
					bone.joint.lowerLimit, bone.joint.upperLimit, bone.joint.angle.get(), 1);
			Spinner<Double> spinner = new Spinner<Double>(valueFactory);
			spinner.setId(String.valueOf(i));
			spinner.setPrefWidth(100);
			spinner.setPrefHeight(50);
			spinner.setMinWidth(spinner.getPrefWidth() - 20);
			spinner.setEditable(true);
//			validation.registerValidator(spinner.getEditor(), Validator.createEmptyValidator(""));

			JFXSlider slider = new JFXSlider(bone.joint.lowerLimit, bone.joint.upperLimit, bone.joint.angle.get());
			slider.setShowTickLabels(true);
			slider.setShowTickMarks(true);
			slider.setMajorTickUnit(30);
			slider.setMinorTickCount(5);
			slider.setMinWidth(100);
			slider.setPrefWidth(Integer.MAX_VALUE);
			slider.setPickOnBounds(true);
			slider.setId(String.valueOf(i));

			spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue == null) {
					spinner.getValueFactory().setValue(oldValue);
					return;
				}
				slider.setValue(newValue);
				data.setTargetDegs(Integer.parseInt(slider.getId()), newValue.floatValue());
			});

			slider.valueChangingProperty().addListener((observableValue, wasChanging, changing) -> {
				if (wasChanging) {
					spinner.getValueFactory().setValue(slider.getValue());
				}
			});
			slider.setOnMouseClicked(event -> {
				spinner.getValueFactory().setValue(slider.getValue());
			});

			HBox innerHbox2 = new HBox(slider, spinner);
			innerHbox2.setPadding(new Insets(10, 2, 6, 6));
			innerHbox2.setFillHeight(true);
			innerHbox2.setSpacing(5);
			innerHbox2.setAlignment(Pos.CENTER);
			innerHbox2.setStyle("-fx-background-color: #4A4A4A;");

			VBox innerVbox = new VBox(1, innerHbox1, innerHbox2);
			innerVbox.setFillWidth(true);
			innerVbox.setAlignment(Pos.CENTER);
			JFXDepthManager.setDepth(innerVbox, 3);

			vbox.getChildren().add(innerVbox);
		}
		vbox.setPadding(new Insets(20, 5, 5, 10));
		vbox.setSpacing(10);
	}
}
