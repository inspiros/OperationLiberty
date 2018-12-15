package com.hust.view;

import com.hust.utils.FloatVector3;

public class RobotModel {

	private PWindow app;

	private boolean lightWeight = true;

	private FloatVector3[] points;
	private Articulation[] articulations;

	public RobotModel(PWindow app) {
		this.app = app;
		points = new FloatVector3[app.getModel().getDof() + 1];
		articulations = new Articulation[app.getModel().getDof()];
		points[0] = new FloatVector3(0, 0, 0);
	}

	public void updateModel(FloatVector3... floatMatrixs) {

		for (int i = 0; i < floatMatrixs.length; i++) {
			points[i + 1] = floatMatrixs[i];
			if (!lightWeight) {
				articulations[i] = new Articulation(i);
			}
		}
	}

	public void render() {
		app.pushMatrix();
		app.pushStyle();
		app.pushMatrix();
		if (lightWeight) {
			renderLines();
		} else {
			app.pushMatrix();
			app.fill(160);
			app.stroke(0, 180);
			renderArticulations();
			app.stroke(20);
			app.popMatrix();
			renderLines();
		}
		app.popMatrix();
		app.fill(180);
		app.cylinder(14, 12, 6, 10);
		app.popMatrix();
		app.popStyle();
	}

	private void renderArticulations() {
		for (Articulation articulation : articulations) {
			articulation.render();
		}
	}

	private void renderLines() {
		if (points.length < 2) {
			return;
		}
		for (int i = 1; i < points.length; i++) {
			app.strokeWeight(points.length - i + 1);
			app.line((float) points[i - 1].x, (float) points[i - 1].y, (float) points[i - 1].z,
					(float) points[i].x, (float) points[i].y, (float) points[i].z);
		}
		app.strokeWeight(1);
	}

	private class Articulation {
		private int id;
		private FloatVector3 axis;
		private float angle;
		private float length;
		private Articulation prev;

		public Articulation(int id) {
			this.id = id;
			this.axis = app.getModel().getArm().getBone(id).joint.rotationAxis;
			if (axis.equals(FloatVector3.Z_AXIS)) {
				length = FloatVector3.distanceBetween(points[id], points[id + 1]);
			}
			this.angle = (float) Math.toRadians(app.getModel().getAngleDegs(id));
			if (id > 0) {
				prev = articulations[id - 1];
			}
		}

		public void render() {
//			switch (axis) {
//			case Y:
//				yLink();
//				break;
//			case Z:
//				zLink();
//				break;
//			case X:
//				xLink();
//				break;
//			}
			zLink();
		}

		public void transform() {
			if (prev != null) {
				prev.transform();
			}
//			switch (axis) {
//			case Y:
//				app.rotateY(PWindow.PI / 2 - angle);
//				break;
//			case Z:
//				app.rotateZ(angle);
//				break;
//			case X:
//				app.rotateX(angle);
//				break;
//			}
		}

		private void zLink() {
			app.pushMatrix();
			app.translate((float) points[id - 1].x, (float) points[id - 1].y,
					(float) points[id - 1].z);
			if (prev != null) {
				prev.transform();
			}
			if (id != 1) {
				// app.rotateY(PWindow.PI / 2);
				app.rotateZ(angle);
				app.cylinder(4, 4, length / 3, 6);
			} else {
				app.rotateZ(angle);
				app.cylinder(6, 6, length / 2, 10);
			}
			app.popMatrix();
		}

		private void yLink() {
			app.pushMatrix();
			app.translate((float) points[id - 1].x, (float) points[id - 1].y - 3f,
					(float) points[id - 1].z);
			if (prev != null) {
				prev.transform();
			}
			app.rotateX(PWindow.PI / 2);
			app.rotateZ(angle);
			app.cylinder(6, 6, 4, 8);
			app.popMatrix();
		}

		private void xLink() {
			app.pushMatrix();
			app.translate((float) points[id - 1].x, (float) points[id - 1].y,
					(float) points[id - 1].z + 3f);
			if (prev != null) {
				prev.transform();
			}
			// TODO Check
			app.rotateY(HApplet.PI / 2);
			app.rotateZ(angle);
			app.cylinder(6, 6, 4, 8);
			app.popMatrix();
		}
	}
}
