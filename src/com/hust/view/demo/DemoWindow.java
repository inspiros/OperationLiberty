package com.hust.view.demo;

import java.util.LinkedList;

import com.hust.core.Configurations;
import com.hust.model.Models;
import com.hust.utils.algebra.FloatVector3;

import processing.opengl.PJOGL;

public class DemoWindow extends HApplet {

	private Models model;
	public DemoWindow app = this;

	public final String appPath = sketchPath();

	/**
	 * Camera control.
	 */
	private FloatVector3 cameraPosition, centerPosition;

	private LinkedList<Target> targets = new LinkedList<Target>();

	public DemoWindow(Models model) {
		this.model = model;
		componentsDrawer.addDrawable(this.model.robot);
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

		cameraPosition = new FloatVector3(width / 3, height / 3, model.robot.getEndEffector().z);
		centerPosition = new FloatVector3();
		
		Configurations.MODULES_INITIALIZATION.get("demo").countDown();
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

	public void updateTarget(FloatVector3 t) {
		targets.clear();
		targets.add(new Target(app, t.x, t.y, t.z));
	}
}
