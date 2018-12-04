package com.hust.view;

import com.hust.core.Controller;
import com.hust.core.Data;
import com.hust.view.utils.IdCallbackListener;
import com.jogamp.newt.opengl.GLWindow;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class PWindow extends PApplet {
	private Data model;

	public final String appPath = sketchPath();
	public GLWindow window;
	private ControlP5 guiController;

	private RobotModel robotModel;

	private float cameraX;
	private float cameraY;
	private float cameraZ = -100;

	private float centerX = 100;
	private float centerY;
	private float centerZ;

	public PWindow(Data model) {
		this.model = model;
		PApplet.runSketch(new String[] { this.getClass().getSimpleName() }, this);
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
		robotModel.updateModel(model.getArm().getEndPoints());
		cameraX = width / 3;
		cameraY = height / 3;
		// camera();

		createGui();
	}

	@Override
	public void draw() {
		// hint(DISABLE_DEPTH_TEST);
		pushMatrix();
		render();
		popMatrix();
		// hint(ENABLE_DEPTH_TEST);
	}

	public void render() {
		background(200);
		cameraTransform();
		drawGround();
		drawBaseAxis();
		pushMatrix();
		robotModel.render();
		popMatrix();

	}

	private void drawGround() {
		pushMatrix();
		translate(0, 0, 20);
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
		line(0, 0, 0, 0, 0, -100);
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

	public Data getModel() {
		return model;
	}

	public void updateRobotModel() {
		// robotModel.updateModel(floatMatrixs);
		robotModel.updateModel(model.getArm().getEndPoints());
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
	 * Additional 3D shapes
	 */
	public void cylinder(float bottom, float top, float h, int sides) {
		pushMatrix();
		rotateX((float) Math.toRadians(-90));
		translate(0, h / 2, 0);

		float angle;
		float[] x = new float[sides + 1];
		float[] z = new float[sides + 1];

		float[] x2 = new float[sides + 1];
		float[] z2 = new float[sides + 1];

		// get the x and z position on a circle for all the sides
		for (int i = 0; i < x.length; i++) {
			angle = TWO_PI / (sides) * i;
			x[i] = sin(angle) * bottom;
			z[i] = cos(angle) * bottom;
		}
		for (int i = 0; i < x.length; i++) {
			angle = TWO_PI / (sides) * i;
			x2[i] = sin(angle) * top;
			z2[i] = cos(angle) * top;
		}
		beginShape(TRIANGLE_FAN);

		vertex(0, -h / 2, 0);

		for (int i = 0; i < x.length; i++) {
			vertex(x[i], -h / 2, z[i]);
		}

		endShape();

		// draw the center of the cylinder
		beginShape(QUAD_STRIP);

		for (int i = 0; i < x.length; i++) {
			vertex(x[i], -h / 2, z[i]);
			vertex(x2[i], h / 2, z2[i]);
		}

		endShape();

		// draw the top of the cylinder
		beginShape(TRIANGLE_FAN);

		vertex(0, h / 2, 0);

		for (int i = 0; i < x.length; i++) {
			vertex(x2[i], h / 2, z2[i]);
		}

		endShape();

		popMatrix();
	}

	/**
	 * GUI
	 */
	private void createGui() {

		guiController = new ControlP5(this, createFont("Cambria", 12));

		for (int i = 0; i < model.getDof(); i++) {
			float[] clamp = model.getRangeLimiter(i).getBounds();
			float[] vals = model.getDegreeAngles();
			guiController.addSlider("sliderAngle" + i).setPosition(width / 10, height / 8 + height / 10 * i)
					.setSize(width / 4, height / 12).setValue(vals[i]).setRange(clamp[0], clamp[1]).setLabel("θ" + i)
					.addCallback(new IdCallbackListener(i) {

						@Override
						public void controlEvent(CallbackEvent event) {
							// TODO Auto-generated method stub
							if (event.getAction() == 100) {
								try {
									Controller.updateDegreeAngle(getId(), event.getController().getValue());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
		}

		guiController.addButton("forwardKinematics").setPosition(width / 10, height / 20).setSize(width / 10, 20)
				.setLabel("FK").addCallback(new CallbackListener() {

					@Override
					public void controlEvent(CallbackEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == 100) {
							try {
								Controller.changeDegreeAngles(-1);
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
								// Controller.changeDegreeAngles(1);
								Controller.test();
							} catch (Exception e) {
							}
						}
					}
				});
	}
}
