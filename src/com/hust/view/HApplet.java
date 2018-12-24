package com.hust.view;

import com.hust.utils.data.FloatVector3;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * An abstract class to fix z-coordinate of rendered objects in PApplet.
 * 
 * @author Inspiros
 *
 */
public abstract class HApplet extends PApplet {

	protected ComponentsDrawer componentsDrawer = new ComponentsDrawer(this);

	/**
	 * Nested class to draw the robotic arm.
	 * 
	 * @author Inspiros
	 *
	 */
	protected class ComponentsDrawer extends Drawer<HApplet> {

		public ComponentsDrawer(HApplet container) {
			this.container = container;
		}
	}

	@Override
	public void draw() {
		hint(DISABLE_DEPTH_TEST);
		pushMatrix();
		render();
		hint(ENABLE_DEPTH_TEST);
		ambientLight(127, 127, 127);
		directionalLight(180, 180, 180, -1, -1, 1);
		componentsDrawer.renderDrawables();
		popMatrix();
		noLights();
	}

	public abstract void render();

	@Override
	public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX,
			float upY, float upZ) {
		super.camera(eyeX, eyeY, -eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	@Override
	public void translate(float x, float y, float z) {
		super.translate(x, y, -z);
	}

	@Override
	public void point(float x, float y, float z) {
		super.point(x, y, -z);
	}

	@Override
	public void vertex(float x, float y, float z) {
		super.vertex(x, y, -z);
	}

	/**
	 * 
	 * @param p
	 */
	public void point(FloatVector3 p) {
		super.point(p.x, p.y, -p.z);
	}

	@Override
	public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
		super.line(x1, y1, -z1, x2, y2, -z2);
	}

	public void line(FloatVector3 p1, FloatVector3 p2) {
		super.line(p1.x, p1.y, -p1.z, p2.x, p2.y, -p2.z);
	}

	/**
	 * @param p1 first point
	 * @param p2 second point
	 */
	public void line(float[] p1, float[] p2) {
		if (p1.length != p2.length) {
			throw new IllegalArgumentException("Expecting points from same dimension!");
		}
		if (p1.length == 2) {
			line(p1[0], p1[1], p2[0], p2[1]);
		} else if (p1.length == 3) {
			line(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2]);
		} else {
			throw new IllegalArgumentException("Must be 2D or 3D");
		}
	}

	/**
	 * @param x1 x-coordinate of the first point
	 * @param y1 y-coordinate of the first point
	 * @param x2 x-coordinate of the second point
	 * @param y2 y-coordinate of the second point
	 */
	public void dashLine(float x1, float y1, float x2, float y2) {
		PVector start = new PVector(x1, y1);
		PVector end = new PVector(x2, y2);
		PVector line = end.sub(start);
		PVector dir = line.normalize().mult(5);
		PVector curPoint = start;
		PVector nextPoint = start.add(dir);
		while (nextPoint.sub(start).mag() < line.mag()) {
			line(curPoint.array(), nextPoint.array());
			curPoint = nextPoint.add(dir);
			nextPoint.set(curPoint.add(dir));
		}
		if (curPoint.sub(start).mag() < line.mag()) {
			line(curPoint.array(), end.array());
		}
	}

	/**
	 * @param x1 x-coordinate of the first point
	 * @param y1 y-coordinate of the first point
	 * @param z1 z-coordinate of the first point
	 * @param x2 x-coordinate of the second point
	 * @param y2 y-coordinate of the second point
	 * @param z2 z-coordinate of the second point
	 */
	public void dashLine(float x1, float y1, float z1, float x2, float y2, float z2) {
		PVector start = new PVector(x1, y1, z1);
		PVector end = new PVector(x2, y2, z2);
		PVector line = end.sub(start);
		PVector dir = line.normalize().mult(5);
		PVector curPoint = start;
		PVector nextPoint = start.add(dir);
		while (nextPoint.sub(start).mag() < line.mag()) {
			line(curPoint.array(), nextPoint.array());
			curPoint = nextPoint.add(dir);
			nextPoint.set(curPoint.add(dir));
		}
		if (curPoint.sub(start).mag() < line.mag()) {
			line(curPoint.array(), end.array());
		}
	}

	/**
	 * @param p1 first point
	 * @param p2 second point
	 */
	public void dashLine(float[] p1, float[] p2) {
		if (p1.length != p2.length) {
			throw new IllegalArgumentException("Expecting points from same dimension!");
		}
		if (p1.length == 2) {

		} else if (p1.length == 3) {
			dashLine(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2]);
		} else {
			throw new IllegalArgumentException("Must be 2D or 3D");
		}
	}

	/**
	 * Draw a circle in 3D
	 * 
	 * @param center
	 * @param normal
	 */
	public void regularPolygon(FloatVector3 center, float radius, FloatVector3 normal, int sides) {
		pushMatrix();
		FloatVector3 zAxis = normal.normalized();
		FloatVector3 xAxis = new FloatVector3(normal.x, 0, -normal.z).normalized();
		FloatVector3 yAxis = zAxis.mul(xAxis);

		float angle;
		float[] x = new float[sides + 1];
		float[] y = new float[sides + 1];
		float[] z = new float[sides + 1];
		for (int i = 0; i < x.length; i++) {
			angle = TWO_PI / (sides) * i;
			x[i] = center.x + radius * (xAxis.x * (float) Math.cos(angle) + yAxis.x * (float) Math.sin(angle));
			y[i] = center.y + radius * (xAxis.y * (float) Math.cos(angle) + yAxis.y * (float) Math.sin(angle));
			z[i] = center.z + radius * (xAxis.z * (float) Math.cos(angle) + yAxis.z * (float) Math.sin(angle));
		}
		beginShape(TRIANGLE_FAN);

		vertex(center.x, center.y, center.z);

		for (int i = 0; i < sides + 1; i++) {
			vertex(x[i], y[i], z[i]);
		}
		endShape();
		popMatrix();
	}

	/**
	 * Draw a cylinder
	 * 
	 * @param center1
	 * @param center2
	 * @param radius1
	 * @param radius2
	 * @param sides
	 */
	public void cylinder(FloatVector3 center1, FloatVector3 center2, FloatVector3 prependicularAxis, float radius1,
			float radius2, int sides) {
		pushMatrix();
		FloatVector3 zAxis = center2.sub(center1).normalized();
		FloatVector3 xAxis = prependicularAxis;
		FloatVector3 yAxis = zAxis.cross(xAxis);

		float angle;
		float[] x1 = new float[sides + 1];
		float[] y1 = new float[sides + 1];
		float[] z1 = new float[sides + 1];

		float[] x2 = new float[sides + 1];
		float[] y2 = new float[sides + 1];
		float[] z2 = new float[sides + 1];

		for (int i = 0; i < x1.length; i++) {
			angle = TWO_PI / (sides) * i;
			float xBase = xAxis.x * cos(angle) + yAxis.x * sin(angle);
			float yBase = xAxis.y * cos(angle) + yAxis.y * sin(angle);
			float zBase = xAxis.z * cos(angle) + yAxis.z * sin(angle);
			x1[i] = center1.x + radius1 * xBase;
			y1[i] = center1.y + radius1 * yBase;
			z1[i] = center1.z + radius1 * zBase;

			x2[i] = center2.x + radius2 * xBase;
			y2[i] = center2.y + radius2 * yBase;
			z2[i] = center2.z + radius2 * zBase;
		}

		beginShape(TRIANGLE_FAN);
		vertex(center1.x, center1.y, center1.z);
		for (int i = 0; i < x1.length; i++) {
			vertex(x1[i], y1[i], z1[i]);
		}
		endShape();

		beginShape(QUAD_STRIP);
		for (int i = 0; i < x1.length; i++) {
			vertex(x1[i], y1[i], z1[i]);
			vertex(x2[i], y2[i], z2[i]);
		}
		endShape();

		beginShape(TRIANGLE_FAN);
		vertex(center2.x, center2.y, center2.z);
		for (int i = 0; i < x2.length; i++) {
			vertex(x2[i], y2[i], z2[i]);
		}
		endShape();

		popMatrix();
	}

	/**
	 * Draw a cylinder.
	 * 
	 * @param bottom
	 * @param top
	 * @param h
	 * @param sides
	 */
	public void cylinder(float bottom, float top, float h, int sides) {
		pushMatrix();
		// rotateX((float) Math.toRadians(-90));
		// translate(0, h / 2, 0);

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

		vertex(0, 0, -h / 2);

		for (int i = 0; i < x.length; i++) {
			vertex(x[i], z[i], -h / 2);
		}

		endShape();

		// draw the center of the cylinder
		beginShape(QUAD_STRIP);

		for (int i = 0; i < x.length; i++) {
			vertex(x[i], z[i], -h / 2);
			vertex(x2[i], z2[i], h / 2);
		}

		endShape();

		// draw the top of the cylinder
		beginShape(TRIANGLE_FAN);

		vertex(0, 0, h / 2);

		for (int i = 0; i < x.length; i++) {
			vertex(x2[i], z2[i], h / 2);
		}

		endShape();

		popMatrix();
	}

	/**
	 * Fix the z-coordinate to match with render mode
	 * 
	 * @param point
	 * @return
	 */
	public float[] fix(float[] point) {
		if (point.length != 3) {
			throw new IllegalArgumentException("Only points in 3D dimension needed to be fix");
		}
		return new float[] { point[0], point[1], point[2] };
	}
}
