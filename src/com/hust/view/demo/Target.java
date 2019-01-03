package com.hust.view.demo;

public class Target {

	private PWindow app;
	private float x, y, z;

	public Target(PWindow app, float x, float y, float z) {
		this.app = app;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float[] getPosition() {
		return new float[] { x, y, z };
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void render() {
		app.pushMatrix();
		app.pushStyle();
		app.strokeWeight(0.5f);
		app.stroke(0);
		app.line(x, y, z, x, y, 0);
		if (x != 0 && y != 0) {
			app.stroke(0, 255, 0);
			app.line(x, y, 0, 0, y, 0);
			app.stroke(255, 0, 0);
			app.line(x, y, 0, x, 0, 0);
		}
		app.stroke(0);
		app.line(x, y, z, x, 0, z);
		if (x != 0 && z != 0) {
			app.stroke(0, 0, 255);
			app.line(x, 0, z, 0, 0, z);
			app.stroke(255, 0, 0);
			app.line(x, 0, z, x, 0, 0);
		}
		app.stroke(0);
		app.line(x, y, z, 0, y, z);
		if (y != 0 && z != 0) {
			app.stroke(0, 0, 255);
			app.line(0, y, z, 0, 0, z);
			app.stroke(0, 255, 0);
			app.line(0, y, z, 0, y, 0);
		}
//		if (distance > arm.getSolveDistanceThreshold()) {
//			stroke(255, 255, 0);
//			strokeWeight(1);
//			// dashLine(arm.getEffectorLocation().toArray(), target.toArray());
//		}
		app.strokeWeight(10);
		app.stroke(220, 220, 0, 200);
		app.point(x, y, z);
		app.popMatrix();
		app.popStyle();
	}
}
