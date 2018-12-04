package com.hust.core;

import com.hust.robot.Arm;
import com.hust.robot.Articulation;
import com.hust.robot.RotationAxis;
import com.hust.utils.ArrayUtils;
import com.hust.utils.Kinematics;
import com.hust.utils.NumericUtils;
import com.hust.utils.RangeLimiter;

public class Data {

	private Arm robot;

	private int dof;
	private RotationAxis[] axis;
	private float[] angles;
	private float[][] endPoints;
	private RangeLimiter[] bounds;

	private float[] targets;
	private float[] changeLimits;
	private AngleChanger[] followers;
	private final int sleepTime = 25;

	public boolean limitSpeed = true;

	public static Data setupModel() {
		Data res = new Data();
		res.dof = 5;
		res.endPoints = new float[][] { { 0, 0, 50 }, { 0, 0, 40 }, { 0, 0, 40 }, { 0, 0, 30 }, { 0, 0, 20 } };
		res.axis = new RotationAxis[] { RotationAxis.Z, RotationAxis.Y, RotationAxis.Y, RotationAxis.Y,
				RotationAxis.Y };
		res.angles = new float[] { 0, 45, 45, 45, 0 };
		res.targets = res.angles.clone();
		res.changeLimits = ArrayUtils.fill(res.dof, 2f);
		res.bounds = new RangeLimiter[res.dof];
		res.followers = new AngleChanger[res.dof];
		for (int i = 0; i < res.angles.length; i++) {
			res.bounds[i] = new RangeLimiter(-180, 180);
			res.followers[i] = res.new AngleChanger(i);
		}
		res.setupRobot();
		return res;
	}

	private Data() {
		// TODO Yes you are not blind; it's a fucking PRIVATE Constructer!
	}

	public void setupRobot() {
		robot = new Arm();

		for (int i = 0; i < dof; i++) {
			robot.plug(new Articulation(axis[i], (float) Math.toRadians(angles[i]), endPoints[i]));
		}
	}

	public Arm getArm() {
		return robot;
	}

	public int getDof() {
		return dof;
	}

	public RotationAxis getRotationAxis(int i) {
		return axis[i];
	}

	public RotationAxis[] getRotationAxis() {
		return axis;
	}

	public RangeLimiter getRangeLimiter(int i) {
		return bounds[i];
	}

	public float getDegreeAngle(int i) {
		return angles[i];
	}

	public float[] getDegreeAngles() {
		return angles;
	}

	public void updateDegreeAngles(float... angles) {
		this.angles = angles;
		robot.updateDegreeAngles(this.angles);
	}

	public void updateDegreeAngle(int id, float angle) {
		if (!limitSpeed) {
			angles[id] = angle;
			robot.updateDegreeAngle(id + 1, angle);
		} else {
			if (!followers[id].abort && followers[id].holder != null) {
				followers[id].abort();
				try {
					followers[id].holder.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			setTarget(id, angle);
			Thread changer = new Thread(followers[id]);
			followers[id].setThread(changer);
			changer.start();

		}
	}

	public void changeDegreeAngles(float... changes) {
		float[] temp = angles;
		for (int i = 0; i < angles.length; i++) {
			try {
				temp[i] = bounds[i].limits(temp[i], changes[i]);
			} catch (Exception e) {
				break;
			}
		}
		angles = temp;
		robot.updateDegreeAngles(angles);
	}

	public void changeDegreeAngle(int id, float change) {
		float temp = angles[id];
		temp = bounds[id].limits(temp, change);
		angles[id] = temp;
		robot.updateDegreeAngle(id, angles[id]);
	}

	public void setTargets(float... targets) {
		this.targets = targets;
	}

	public void setTarget(int id, float target) {
		targets[id] = target;
	}

	public void moveToPosition(float[] newPosition, int ikAlgorithm) {
		float[] currentPosition = NumericUtils.toArray(Kinematics.forwardKinematics(axis, angles, endPoints));
		for (float f : currentPosition) {
			System.out.print(f + " - ");
		}
		System.out.println();

		switch (ikAlgorithm) {
		case Kinematics.JACOBIAN:
			break;
		case Kinematics.GRADIENT_DESCENT:
			angles = gradientDescentIK(newPosition);
			break;
		case Kinematics.FABRIK:
			angles = fabrIK(newPosition);
			break;
		}

		// robot.updateDegreeAngles(angles);

		// for (int i = 0; i < angles.length; i++) {
		// System.out.print(angles[i] + " - ");
		// }
	}

	private float[] gradientDescentIK(float[] newPosition) {
		return Kinematics.gradientDescentIK(axis, angles, bounds, endPoints, newPosition);
	}

	private float[] fabrIK(float[] newPosition) {
		return Kinematics.fabrIK(axis, angles, bounds, endPoints, newPosition);
	}

	private class AngleChanger implements Runnable {
		private int index;
		private boolean abort;

		private Thread holder;

		public AngleChanger(int index) {
			this.index = index;
		}

		public void setThread(Thread holder) {
			this.holder = holder;
		}

		public void abort() {
			abort = true;
		}

		public void run() {
			// TODO Fairly complicated multi-threading
			if (angles[index] < targets[index]) {
				while (angles[index] + changeLimits[index] < targets[index] && !abort) {
					angles[index] += changeLimits[index];
					robot.updateDegreeAngle(index + 1, angles[index]);
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				while (angles[index] - changeLimits[index] > targets[index] && !abort) {
					angles[index] -= changeLimits[index];
					robot.updateDegreeAngle(index + 1, angles[index]);
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (!abort) {
				angles[index] = targets[index];
				robot.updateDegreeAngle(index + 1, angles[index]);
			}
			holder = null;
			abort = false;
		}
	}

}
