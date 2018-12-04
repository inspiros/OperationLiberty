package com.hust.robot;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class Arm {
	private String friendlyName;

	private int dof;

	private List<Articulation> articulations = new ArrayList<Articulation>();

	public Arm() {
		articulations.add(Articulation.getBase());
	}

	public void setName(String name) {
		this.friendlyName = name;
	}
	
	public String getName() {
		return friendlyName;
	}

	public void plug(Articulation articulation) {
		articulations.add(articulation);
		articulation.setPrevJoint(articulations.get(articulations.size() - 2));
		dof++;
	}

	public void plugTo(Articulation articulation) {
		articulations.add(0, articulation);
		articulation.setPrevJoint(articulations.get(articulations.size() - 2));
		dof++;
	}

	public Articulation get(int i) {
		return articulations.get(i);
	}

	public int getDof() {
		return dof;
	}

	public void printEndEffectorCoordinate() {
		articulations.get(articulations.size() - 1).getEndPointCoordinate().print();
	}

	public void printEndEffectorDirection() {
		articulations.get(articulations.size() - 1).getGlobalDirection().print();
	}

	public SimpleMatrix[] getEndPoints() {
		SimpleMatrix[] res = new SimpleMatrix[dof];
		for (int i = 0; i < dof; i++) {
			res[i] = articulations.get(i + 1).getEndPointCoordinate();
		}
		return res;
	}

	public SimpleMatrix forwardKinematics() {
		for (int i = 1; i < articulations.size(); i++) {
			articulations.get(i).updateTransformation();
		}
		return articulations.get(articulations.size() - 1).getEndPointCoordinate();
	}

	public SimpleMatrix forwardKinematics(int id) {
		for (int i = id; i < articulations.size(); i++) {
			articulations.get(i).updateTransformation();
		}
		return articulations.get(articulations.size() - 1).getEndPointCoordinate();
	}

	public float[] getAngles() {
		int arrLength = articulations.size() - 1;
		float[] res = new float[arrLength];
		for (int i = 0; i < arrLength; i++) {
			res[i] = articulations.get(i + 1).getAngle();
		}
		return res;
	}

	public void updateAngles(float... angles) {
		for (int i = 1; i < articulations.size(); i++) {
			try {
				articulations.get(i).updateAngle(angles[i - 1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				break;
			}
		}
		forwardKinematics();
	}

	public void updateDegreeAngles(float... angles) {
		float[] rAngles = new float[angles.length];
		for (int i = 0; i < rAngles.length; i++) {
			rAngles[i] = (float) Math.toRadians(angles[i]);
		}
		updateAngles(rAngles);
	}

	public void updateAngle(int id, float angle) {
		articulations.get(id).updateAngle(angle);
		forwardKinematics(id);
	}

	public void updateDegreeAngle(int id, float angle) {
		updateAngle(id, (float) Math.toRadians(angle));
	}
}
