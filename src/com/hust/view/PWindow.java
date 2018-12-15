package com.hust.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hust.core.Controller;
import com.hust.core.DataBuffer;
import com.hust.robot.DataChangeListener;
import com.jogamp.newt.opengl.GLWindow;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.event.MouseEvent;

public class PWindow extends HApplet implements DataChangeListener<Float> {
	private DataBuffer data;
	public PWindow app = this;

	public final String appPath = sketchPath();
	public GLWindow window;
	private ControlP5 guiController;

	private List<Slider> sliders = new ArrayList<Slider>();

	private List<Target> targets = new LinkedList<Target>();

	private RobotModel robotModel;

	private float cameraX;
	private float cameraY;
	private float cameraZ = 100;

	private float centerX = 100;
	private float centerY;
	private float centerZ;

	public PWindow(DataBuffer data) {
		this.data = data;
		componentsDrawer.addDrawable(this.data.getArm());
		componentsDrawer.setupDrawables();
		HApplet.runSketch(new String[] { this.getClass().getSimpleName() }, this);
	}

	@Override
	public void settings() {
		size(800, 600, P3D);
	}

	@Override
	public void setup() {
		window = (GLWindow) surface.getNative();
		surface.setTitle("Operation Liberty");
		surface.setFrameRate(24);

		// noLoop();
		robotModel = new RobotModel(this);
		robotModel.updateModel(data.getArm().getEndPoints());
		cameraX = width / 3;
		cameraY = height / 3;
		// camera();

		createGui();
	}

	public void render() {
		background(200);
		cameraTransform();
		drawGround();
		drawBaseAxis();
		//robotModel.render();
		if (targets.size() > 0) {
			for (Target target : targets) {
				target.render();
			}
		}
	}

	private void drawGround() {
		pushMatrix();
		translate(0, 0, -20);
		fill(140);
		noStroke();
		box(200, 200, 40);
		popMatrix();
	}

	private void drawBaseAxis() {
		stroke(255, 0, 0);
		line(0, 0, 0, 100, 0, 0);
		stroke(0, 255, 0);
		line(0, 0, 0, 0, 100, 0);
		stroke(0, 0, 255);
		line(0, 0, 0, 0, 0, 100);
		stroke(0);
	}

	public void cameraTransform() {
		// translate(cameraX, cameraY, cameraZ);
		// rotateX(NumericUtils.parseRadian(-90));
		// rotateZ(NumericUtils.parseRadian(135));
		camera(cameraX, cameraY, cameraZ, // eyeX, eyeY, eyeZ
				centerX, centerY, centerZ, // centerX, centerY, centerZ
				0, 0, 1); // upX, upY, upZ
	}

	public DataBuffer getModel() {
		return data;
	}

	@Override
	public void mouseDragged() {
		if (mouseButton == RIGHT) {
			centerX += mouseX - pmouseX;
			centerZ -= mouseY - pmouseY;
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		cameraZ += 5 * event.getCount();
	}

	/**
	 * GUI
	 */
	private void createGui() {

		guiController = new ControlP5(this, createFont("Cambria", 12));

		for (int i = 0; i < data.getDof(); i++) {
			Slider slider = guiController.addSlider("sliderAngle" + i)
					.setPosition(width / 10, height / 8 + height / 10 * i).setSize(width / 4, height / 12)
					.setValue(data.getArm().getBone(i).joint.angle)
					.setRange(data.getArm().getBone(i).joint.lowerLimit, data.getArm().getBone(i).joint.upperLimit)
					.setLabel("θ" + i).addCallback(new IdCallbackListener(i) {

						@Override
						public void controlEvent(CallbackEvent event) {

							if (event.getAction() == 100) {
								try {
									Controller.updateDegreeAngle(getId(), event.getController().getValue());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
			sliders.add(slider);
		}

		guiController.addButton("forwardKinematics").setPosition(width / 10, height / 20).setSize(width / 10, 20)
				.setLabel("FK").addCallback(new CallbackListener() {

					@Override
					public void controlEvent(CallbackEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == 100) {
							try {
								// Controller.changeDegreeAngles(-1);
							} catch (Exception e) {
							}
						}
					}
				});
		guiController.addButton("inverseKinematics").setPosition(width * 3 / 12, height / 20).setSize(width / 10, 20)
				.setLabel("IK").addCallback(new CallbackListener() {

					@Override
					public void controlEvent(CallbackEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == 100) {
							try {
								// targets.clear();
								new CoordinatePicker(app);
								// Controller.changeDegreeAngles(1);
							} catch (Exception e) {
							}
						}
					}
				});
	}

	public void updateSliders(float[] values) {
		for (int i = 0; i < values.length; i++) {
			sliders.get(i).changeValue(values[i]);
		}
	}

	public void updateSlider(int id, float value) {
		sliders.get(id).changeValue(value);
	}

	public void updateTarget(float[] t) {
		if (t.length != 3) {
			throw new IllegalArgumentException("Must be point of 3D");
		}
		targets.clear();
		targets.add(new Target(app, t[0], t[1], t[2]));
		Controller.test(targets.get(targets.size() - 1).getPosition());
		// Controller.test(t[0], t[1], t[2]);
	}

	@Override
	public void dataChanged(int id) {
	}

	@Override
	public void dataChangedTo(int id, Float value) {
		sliders.get(id).changeValue(value);
	}
}
