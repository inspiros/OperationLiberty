package com.hust.view.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import com.hust.core.Configurations;
import com.hust.model.Models;
import com.hust.model.robot.Bone;
import com.hust.model.robot.Joint;
import com.hust.model.robot.kinematics.KinematicsSolver;
import com.hust.core.Main;
import com.hust.utils.Utils;
import com.hust.utils.algebra.FloatMatrix3;
import com.hust.utils.algebra.FloatQuaternion;
import com.hust.utils.algebra.FloatVector3;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.effects.JFXDepthManager;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ApplicationController implements Initializable {

	/**
	 * Controlled model.
	 */
	private Models model;

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
	private TextField locationLbl0, locationLbl1, locationLbl2, effectorLbl0, effectorLbl1, effectorLbl2, rotationLbl00,
			rotationLbl01, rotationLbl02, rotationLbl10, rotationLbl11, rotationLbl12, rotationLbl20, rotationLbl21,
			rotationLbl22, targetLbl0, targetLbl1, targetLbl2;

	@FXML
	private LineChart<Number, Number> temperatureChart;

	/**
	 * Location and effector listeners.
	 */
	private SceneSyncService<FloatVector3> locationSync, effectorSync;

	/**
	 * Rotation listener.
	 */
	private SceneSyncService<FloatQuaternion> rotationSync;

	@FXML
	private ToggleGroup ikToggleGroup, jstToggleGroup;
	/**
	 * IK Choices in toggle group.
	 */
	@FXML
	private JFXToggleNode ikJacobianTranspose, ikJacobianPseudoInverse, ikTargetTriangle, ikGradientDescent, ikFABRIK;
	/**
	 * Joint Space trajectory method Choices in toggle group.
	 */
	@FXML
	private JFXToggleNode jstNone, jstLinear, jstCubic, jstQuintic, jstLSPB, jstLSQB, jstExponential;

	@FXML
	private JFXButton setTargetBtn;

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
		model = Main.model;

		initializePositioner();
		initializePanes();
		initializeListeners();
		initializeSensorsDisplayer();
	}

	/**
	 * Initialize some data listeners and data bindings.
	 */
	private void initializeListeners() {
		for (int i = 0; i < model.getDof(); i++) {
			Bone bone = model.robot.bones.get(i);
			bone.globalTranslation.addListener((observable, oldValue, newValue) -> {
				reactToPropertyChanges(bone.id, newValue);
			});
			bone.globalRotation.addListener((observable, oldValue, newValue) -> {
				reactToRotationChanges(bone.id, newValue);
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
				Configurations.setTrajectoryMethod(Joint.TrajectoryMethod.values()[(int) newValue.getUserData()]);
			}
		});

		// Add Content to distinguish between toggle nodes.
		ikJacobianTranspose.setUserData(KinematicsSolver.IKMethod.JACOBIAN_TRANSPOSE.ordinal());
		ikJacobianPseudoInverse.setUserData(KinematicsSolver.IKMethod.JACOBIAN_PSEUDO_INVERSE.ordinal());
		ikTargetTriangle.setUserData(KinematicsSolver.IKMethod.TARGET_TRIANGLE.ordinal());
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

		// Quick mafs
		targetLbl0.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER)) {
				setTargetBtn.fire();
			}
		});
		targetLbl1.setOnKeyPressed(targetLbl0.getOnKeyPressed());
		targetLbl2.setOnKeyPressed(targetLbl0.getOnKeyPressed());

		// Sum bad boiz
		locationSync = new SceneSyncService<FloatVector3>() {

			@Override
			public void doTask() {
				locationLbl0.setText(Utils.DECIMAL_FORMAT.format(currentVal.x));
				locationLbl1.setText(Utils.DECIMAL_FORMAT.format(currentVal.y));
				locationLbl2.setText(Utils.DECIMAL_FORMAT.format(currentVal.z));
			}
		};

		effectorSync = new SceneSyncService<FloatVector3>() {

			@Override
			public void doTask() {
				effectorLbl0.setText(Utils.DECIMAL_FORMAT.format(currentVal.x));
				effectorLbl1.setText(Utils.DECIMAL_FORMAT.format(currentVal.y));
				effectorLbl2.setText(Utils.DECIMAL_FORMAT.format(currentVal.z));
			}
		};

		rotationSync = new SceneSyncService<FloatQuaternion>() {

			@Override
			public void doTask() {
				FloatMatrix3 mat = currentVal.toFloatMatrix3();
				rotationLbl00.setText(Utils.DECIMAL_FORMAT.format(mat.m00));
				rotationLbl01.setText(Utils.DECIMAL_FORMAT.format(mat.m01));
				rotationLbl02.setText(Utils.DECIMAL_FORMAT.format(mat.m02));

				rotationLbl10.setText(Utils.DECIMAL_FORMAT.format(mat.m10));
				rotationLbl11.setText(Utils.DECIMAL_FORMAT.format(mat.m11));
				rotationLbl12.setText(Utils.DECIMAL_FORMAT.format(mat.m12));

				rotationLbl20.setText(Utils.DECIMAL_FORMAT.format(mat.m20));
				rotationLbl21.setText(Utils.DECIMAL_FORMAT.format(mat.m21));
				rotationLbl22.setText(Utils.DECIMAL_FORMAT.format(mat.m22));
			}
		};

	}

	/**
	 * Call back of set target for inverse kinematics action.
	 * 
	 * @param event
	 */
	@FXML
	private void onSetTarget(ActionEvent event) {
		FloatVector3 newPosition = new FloatVector3();
		if (targetLbl0.textProperty().get().length() != 0 && targetLbl1.textProperty().get().length() != 0
				&& targetLbl2.textProperty().get().length() != 0) {
			newPosition.x = Float.parseFloat(targetLbl0.textProperty().get());
			newPosition.y = Float.parseFloat(targetLbl1.textProperty().get());
			newPosition.z = Float.parseFloat(targetLbl2.textProperty().get());
			targetLbl0.clear();
			targetLbl1.clear();
			targetLbl2.clear();
		} else {
			return;
		}

		if (FxView.instance.view.demo != null) {
			// TODO Demo
			FxView.instance.view.demo.updateTarget(newPosition);
		}
		// Ready to move.
		model.moveToPosition(newPosition,
				KinematicsSolver.IKMethod.values()[(int) ikToggleGroup.getSelectedToggle().getUserData()]);
	}

	/**
	 * Read location data forcefully.
	 * 
	 * @param id       The id of the listened bone.
	 * @param newValue The new value of {@code FloatVector3} of the listened bone.
	 */
	private void preactToPropertyChanges(int id, FloatVector3 newValue) {
		if (id == listenedId) {
			effectorSync.runLater(newValue);
			if (id == 0) {
				locationSync.runLater(model.robot.globalTranslation);
			}
		} else if (id == listenedId - 1) {
			locationSync.runLater(newValue);
		}
	}

	/**
	 * Read location data whenever detected changes, the update rate is enforced to
	 * be under a certain amount of times in 1 second so that the fast changing pace
	 * of the background data won't make the UI application 'not responding'.
	 * 
	 * @param id       The id of the listened bone.
	 * @param newValue The new value of {@code FloatVector3} of the listened bone.
	 */
	private void reactToPropertyChanges(int id, FloatVector3 newValue) {
		if (id == listenedId) {
			effectorSync.tryRunLater(newValue);
			if (id == 0) {
				locationSync.tryRunLater(model.robot.globalTranslation);
			}
		} else if (id == listenedId - 1) {
			locationSync.tryRunLater(newValue);
		}
	}

	/**
	 * Read rotation data forcefully.
	 * 
	 * @param id       The id of the listened bone.
	 * @param newValue The new value of {@code FloatVector3} of the listened bone.
	 */
	private void preactToRotationChanges(int id, FloatQuaternion newValue) {
		if (id == listenedId) {
			rotationSync.runLater(newValue);
		}
	}

	/**
	 * Read rotation data whenever detected changes, the update rate is enforced to
	 * be under a certain amount of times in 1 second so that the fast changing pace
	 * of the background data won't make the UI application 'not responding'.
	 * 
	 * @param id
	 * @param newValue
	 */
	private void reactToRotationChanges(int id, FloatQuaternion newValue) {
		if (id == listenedId) {
			rotationSync.tryRunLater(newValue);
		}
	}

	/**
	 * Initialize some UI components that cannot be loaded normally from FXML
	 * document.
	 */
	private void initializePositioner() {

		for (int i = 0; i < model.getDof(); i++) {
			Bone bone = model.robot.bones.get(i);

			Label label = new Label("Khớp " + (i + 1));
			label.setAlignment(Pos.CENTER);
			label.setStyle("-fx-font-style: normal; -fx-font-size: 12.0pt; -fx-text-fill: white;");

			HBox innerHbox1 = new HBox(10, label);
			innerHbox1.setPadding(new Insets(0, 10, 0, 10));
			innerHbox1.setStyle("-fx-background-color:  #EDA678;");

			SpinnerValueFactory<Float> valueFactory = new FloatSpinnerValueFactory(bone.joint.lowerLimit,
					bone.joint.upperLimit, bone.joint.angle.get(), 1);
			Spinner<Float> spinner = new Spinner<Float>(valueFactory);
			spinner.setId(String.valueOf(i));
			spinner.setPrefWidth(100);
			spinner.setPrefHeight(50);
			spinner.setMinWidth(spinner.getPrefWidth() - 20);
			spinner.setEditable(true);

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
				model.setTargetDegs(Integer.parseInt(slider.getId()), newValue);
			});

			slider.valueChangingProperty().addListener((observableValue, wasChanging, changing) -> {
				if (wasChanging) {
					spinner.getValueFactory().setValue(slider.valueProperty().floatValue());
				}
			});
//			slider.setOnMouseDragReleased(event -> {
//				spinner.getValueFactory().setValue(slider.valueProperty().floatValue());
//			});
			slider.setOnMouseClicked(event -> {
				float value = (float) (slider.getMin()
						+ (event.getX() / slider.getWidth()) * (slider.getMax() - slider.getMin()));
				spinner.getValueFactory().setValue(value);
			});

			// Angle change listener
			SceneSyncService<Float> angleListener = new SceneSyncService<Float>() {
				@Override
				public void doTask() {
					slider.setValue(currentVal.doubleValue());
				}
			};
			bone.joint.angle.addListener((observable, oldValue, newValue) -> {
				if (!bone.joint.locked) {
					angleListener.tryRunLater(newValue.floatValue());
				}
			});

			bone.joint.angleUpdater.operationFinishedFlag.addListener((observable, oldValue, newValue) -> {
				if (newValue.equals(true) && spinner.getValue() != bone.joint.angle.doubleValue()) {
					spinner.getValueFactory().setValue(bone.joint.angle.get());
				}
			});

			// Put them all in a pretty panel
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

	/**
	 * Initialize some beautifying components for the application. Must be call
	 * after initializePositioner().
	 */
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
		for (int i = 0; i < model.getDof(); i++) {
			jfxComboBoxChoices.add("Khớp " + (i + 1));
		}
		jfxComboBoxChoices.trimToSize();
		jfxComboBox.getItems().addAll(jfxComboBoxChoices);

		// Position listener.
		jfxComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Minus 1 as we added a None choice
			listenedId = jfxComboBoxChoices.indexOf(newValue) - 1;
			if (listenedId < 0) {
				locationLbl0.clear();
				locationLbl1.clear();
				locationLbl2.clear();

				effectorLbl0.clear();
				effectorLbl1.clear();
				effectorLbl2.clear();

				rotationLbl00.clear();
				rotationLbl01.clear();
				rotationLbl02.clear();
				rotationLbl10.clear();
				rotationLbl11.clear();
				rotationLbl12.clear();
				rotationLbl20.clear();
				rotationLbl21.clear();
				rotationLbl22.clear();
			} else {
				Bone bone = model.robot.bones.get(listenedId);
				preactToPropertyChanges(listenedId, bone.globalTranslation.get());
				preactToRotationChanges(listenedId, bone.globalRotation.get());
				if (listenedId > 0) {
					preactToPropertyChanges(listenedId - 1,
							model.robot.bones.get(listenedId - 1).globalTranslation.get());
				}
			}
		});

		// TODO Angle listener.

	}

	private void initializeSensorsDisplayer() {
		temperatureChart.setCreateSymbols(false);
		temperatureChart.setAnimated(false);
		temperatureChart.getXAxis().setAnimated(false);
		temperatureChart.getYAxis().setAnimated(false);
		((NumberAxis) temperatureChart.getXAxis()).setForceZeroInRange(false);
		((NumberAxis) temperatureChart.getYAxis()).setForceZeroInRange(false);

		/*
		 * 
		 */
		model.vault.dataSeries.forEach((dataSeries) -> {
			XYChart.Series<Number, Number> chartSeries = new XYChart.Series<>();
			chartSeries.setName("lx-16a");

			temperatureChart.getData().add(chartSeries);

			ObservableList<Number> series = dataSeries.series.get("temperature");

			series.addListener(new ListChangeListener<Number>() {
				/**
				 * Auto increment.
				 */
				final AtomicInteger tick = new AtomicInteger();
				/**
				 * List.
				 */
				ObservableList<XYChart.Data<Number, Number>> list = chartSeries.getData();

				@Override
				public void onChanged(Change<? extends Number> c) {
					c.next();
					for (Number number : c.getAddedSubList()) {
						Platform.runLater(() -> {

							if (list.size() == Configurations.maxChartNodes) {
								list.remove(0);
//								((NumberAxis) temperatureChart.getYAxis())
//										.setLowerBound(list.remove(0).getXValue().doubleValue());
							}
							list.add(new XYChart.Data<Number, Number>(tick.incrementAndGet(), number));
						});
					}
				}
			});
		});
	}
}
