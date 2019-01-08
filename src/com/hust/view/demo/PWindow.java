package com.hust.view.demo;

import java.util.LinkedList;

import com.hust.core.Configurations;
import com.hust.core.Main;
import com.hust.model.Models;
import com.hust.utils.algebra.FloatVector3;

import g4p_controls.G4P;
import g4p_controls.GCScheme;
import processing.opengl.PJOGL;

public class PWindow extends HApplet {

	private Models data;
	public PWindow app = this;

	public final String appPath = sketchPath();

	/**
	 * Camera control.
	 */
	private FloatVector3 cameraPosition, centerPosition;

	private LinkedList<Target> targets = new LinkedList<Target>();

	public PWindow() {
		data = Main.model;
		componentsDrawer.addDrawable(this.data.robot);
		HApplet.runSketch(new String[] { this.getClass().getSimpleName() }, this);
	}

	@Override
	public void settings() {
		size(Integer.parseInt(Configurations.PROPERTIES.getProperty("demo.width")),
				Integer.parseInt(Configurations.PROPERTIES.getProperty("demo.height")), P3D);
		try {
			PJOGL.setIcon("\\resources\\icon.png");
		} catch (Exception e) {
		}
		smooth(8);
	}

	@Override
	public void setup() {
		// window.setFullscreen(true);
		surface.setTitle(Configurations.PROPERTIES.getProperty("demo.title"));
		surface.setFrameRate(30);

		cameraPosition = new FloatVector3(width / 3, height / 3, data.robot.getEndEffector().z);
		centerPosition = new FloatVector3();

		createGui();
	}

	public void render() {
		background(60);
		cameraTransform();
		drawGround();
		drawBaseAxis();
		if (targets.size() > 0) {
			for (Target target : targets) {
				target.render();
			}
		}
	}

	private void drawGround() {
		noFill();
		strokeWeight(0.5f);
		stroke(255);
		for (int x = -200; x < 200; x += 40) {
			beginShape(QUAD_STRIP);
			for (int y = -200; y < 200; y += 40) {
				vertex(x, y, 0);
				vertex(x + 40, y, 0);
				vertex(x, y + 40, 0);
				vertex(x + 40, y + 40, 0);
			}
			endShape();
		}
	}

	private void drawBaseAxis() {
		strokeWeight(1.5f);
		stroke(255, 0, 0);
		line(0, 0, 0, 100, 0, 0);
		stroke(0, 255, 0);
		line(0, 0, 0, 0, 100, 0);
		stroke(0, 0, 255);
		line(0, 0, 0, 0, 0, 100);
		stroke(0);
	}

	public void cameraTransform() {
		camera(cameraPosition.x, cameraPosition.y, cameraPosition.z, // eyeX, eyeY, eyeZ
				centerPosition.x, centerPosition.y, centerPosition.z, // centerX, centerY, centerZ
				0, 0, 1); // upX, upY, upZ
	}

	@Override
	public void mouseDragged() {
		if (mouseButton == LEFT) {
			cameraPosition = FloatVector3.rotateZDegs(cameraPosition, (float) (pmouseX - mouseX) / 2);
			cameraPosition = FloatVector3.rotateAboutAxisDegs(cameraPosition, (float) (pmouseY - mouseY) / 2,
					cameraPosition.cross(FloatVector3.Z_AXIS).normalized());
		}
	}

	@Override
	public void mouseWheel(processing.event.MouseEvent event) {
		cameraPosition = cameraPosition.mul((cameraPosition.length() + 3 * event.getCount()) / cameraPosition.length());
	}

	/**
	 * GUI
	 */
	private void createGui() {
		G4P.messagesEnabled(false);
		G4P.setGlobalColorScheme(GCScheme.CYAN_SCHEME);

//		guiController = new ControlP5(this, createFont("Cambria", 12));

//		for (int i = 0; i < data.getDof(); i++) {
//			Slider slider = guiController.addSlider("sliderAngle" + i)
//					.setPosition(width / 10, height / 8 + height / 10 * i).setSize(width / 4, height / 12)
//					.setValue(data.getArm().getBone(i).joint.angle.get())
//					.setRange(data.getArm().getBone(i).joint.lowerLimit, data.getArm().getBone(i).joint.upperLimit)
//					.setLabel("θ" + i).addCallback(new IdCallbackListener(i) {
//
//						@Override
//						public void controlEvent(CallbackEvent event) {
//							if (event.getAction() == 100) {
//								targets.clear();
//
//								data.setTargetDegs(getId(), event.getController().getValue());
//							}
//						}
//					});
//			sliders.add(slider);
//		}
//		sliders.trimToSize();

//		guiController.addButton("forwardKinematics").setPosition(width / 10, height / 20).setSize(width / 10, 20)
//				.setLabel("FK").addCallback(new CallbackListener() {
//
//					@Override
//					public void controlEvent(CallbackEvent event) {
//						if (event.getAction() == 100) {
//
//						}
//					}
//				});
//		guiController.addButton("inverseKinematics").setPosition(width * 3 / 12, height / 20).setSize(width / 10, 20)
//				.setLabel("IK").addCallback(new CallbackListener() {
//
//					@Override
//					public void controlEvent(CallbackEvent event) {
//						if (event.getAction() == 100) {
//							new CoordinatePicker(app);
//						}
//					}
//				});
	}

	public void updateTarget(FloatVector3 t) {
		targets.clear();
		targets.add(new Target(app, t.x, t.y, t.z));
	}
}
