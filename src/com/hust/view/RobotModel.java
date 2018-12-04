package com.hust.view;

import org.ejml.simple.SimpleMatrix;

import com.hust.robot.RotationAxis;

public class RobotModel {

	private PWindow app;

	private boolean lightWeight = false;

	private int dof;

	private SimpleMatrix[] points;
	private Joint[] joints;

	public RobotModel(PWindow app) {
		this.app = app;
		points = new SimpleMatrix[app.getModel().getDof() + 1];
		joints = new Joint[app.getModel().getDof()];
		points[0] = new SimpleMatrix(new float[][] { { 0 }, { 0 }, { 0 } });
	}

	public void updateModel(SimpleMatrix... floatMatrixs) {

		for (int i = 0; i < floatMatrixs.length; i++) {
			dof = i + 1;
			points[i + 1] = floatMatrixs[i];
			if (!lightWeight) {
				joints[i] = new Joint(dof);
			}
		}
	}

	public void render() {
		app.pushMatrix();
		if (lightWeight) {
			renderLines();
		} else {
			app.pushMatrix();
			app.fill(160);
			app.stroke(0, 180);
			renderLinks();
			app.stroke(20);
			app.popMatrix();
			renderLines();
		}
		app.popMatrix();
		app.fill(180);
		app.cylinder(14, 12, 6, 10);
	}

	private void renderLinks() {
		for (Joint link : joints) {
			link.render();
		}
	}

	private void renderLines() {
		if (points.length < 2) {
			return;
		}
		for (int i = 1; i < points.length; i++) {
			app.strokeWeight(points.length - i + 1);
			app.line((float) points[i - 1].get(0, 0), (float) points[i - 1].get(1, 0), -(float) points[i - 1].get(2, 0),
					(float) points[i].get(0, 0), (float) points[i].get(1, 0), -(float) points[i].get(2, 0));
		}
		app.strokeWeight(1);
	}

	private class Joint {
		private int id;
		private RotationAxis axis;
		private float angle;
		private float length;
		private Joint prev;

		public Joint(int id) {
			this.id = id;
			this.axis = app.getModel().getRotationAxis(id - 1);
			if(axis == RotationAxis.Z) {
				length = PWindow.dist((float) points[id - 1].get(0, 0), (float) points[id - 1].get(1, 0),
						-(float) points[id - 1].get(2, 0), (float) points[id].get(0, 0), (float) points[id].get(1, 0),
						-(float) points[id].get(2, 0));
			}
			this.angle = (float) Math.toRadians(app.getModel().getDegreeAngle(id - 1));
			if (id > 1) {
				prev = joints[id - 2];
			}
		}

		public void render() {
			switch (axis) {
			case Y:
				yLink();
				break;
			case Z:
				zLink();
				break;
			case X:
				xLink();
				break;
			}
		}

		public void transform() {
			if (prev != null) {
				prev.transform();
			}
			switch (axis) {
			case Y:
				app.rotateY(PWindow.PI / 2 - angle);
				break;
			case Z:
				app.rotateZ(angle);
				break;
			case X:
				app.rotateX(angle);
				break;
			}
		}

		private void zLink() {
			app.pushMatrix();
			app.translate((float) points[id - 1].get(0, 0), (float) points[id - 1].get(1, 0),
					-(float) points[id - 1].get(2, 0));
			if (prev != null) {
				prev.transform();
			}
			if (id != 1) {
				//app.rotateY(PWindow.PI / 2);
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
			app.translate((float) points[id - 1].get(0, 0), (float) points[id - 1].get(1, 0) - 3f,
					-(float) points[id - 1].get(2, 0));
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
			app.translate((float) points[id - 1].get(0, 0), (float) points[id - 1].get(1, 0),
					-(float) points[id - 1].get(2, 0) - 3);
			if (prev != null) {
				prev.transform();
			}
			app.rotateZ(angle);
			app.cylinder(6, 6, 4, 8);
			app.popMatrix();
		}
	}
}
