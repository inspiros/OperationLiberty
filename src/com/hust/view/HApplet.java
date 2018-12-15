package com.hust.view;

import com.hust.utils.FloatVector3;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * An abstract class to fix z-coordinate of rendered objects in PApplet.
 * 
 * @author Inspiros
 *
 */
public abstract class HApplet extends PApplet {

	public boolean lightWeight = true;

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
		// hint(DISABLE_DEPTH_TEST);
		pushMatrix();
		render();
		componentsDrawer.renderDrawables();
		popMatrix();
		// hint(ENABLE_DEPTH_TEST);
	}

	public abstract void render();

	@Override
	public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX,
			float upY, float upZ) {
		// TODO Auto-generated method stub
		super.camera(eyeX, eyeY, -eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	@Override
	public void translate(float x, float y, float z) {
		// TODO Auto-generated method stub
		super.translate(x, y, -z);
	}

	@Override
	public void point(float x, float y, float z) {
		// TODO Auto-generated method stub
		super.point(x, y, -z);
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
		// TODO Auto-generated method stub
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
	 * Draw a cylinder.
	 * 
	 * @param bottom
	 * @param top
	 * @param h
	 * @param sides
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
