package com.hust.robot;

import java.util.ArrayList;
import java.util.List;

import com.hust.utils.FloatMatrix3;
import com.hust.utils.FloatMatrix4;
import com.hust.utils.FloatVector3;
import com.hust.view.Drawable;
import com.hust.view.HApplet;
import com.hust.view.PWindow;

public class Chain implements DataChangeListener<Float>, Drawable<HApplet> {
	public String name;

	// private HApplet app;

	private FloatMatrix3 globalRotation;
	private FloatMatrix4 globalTransformation;

	private List<Bone> bones = new ArrayList<Bone>();

	private List<Float> anglesLock = new ArrayList<Float>();
	private List<FloatVector3> pointsLock = new ArrayList<FloatVector3>();
	private boolean locked;

	public Chain() {
		globalRotation = new FloatMatrix3(1);
		globalTransformation = globalRotation.toFloatMatrix4();
	}

	public void addConsecutiveBone(FloatVector3 direction, FloatVector3 rotationAxis, float angle, float lowerLimit,
			float upperLimit) {
		Bone bone = new Bone(this, direction, rotationAxis, angle, lowerLimit, upperLimit);
		bones.add(bone);
	}

	public Bone getBaseBone() {
		return bones.get(0);
	}

	public Bone getLastBone() {
		return bones.get(bones.size() - 1);
	}

	public Bone getBone(int id) {
		return bones.get(id);
	}

	public int getDof() {
		return bones.size();
	}

	public FloatMatrix3 getLocalRotation() {
		return bones.get(bones.size() - 1).getGlobalRotation();
	}

	public FloatMatrix3 getGlobalRotation() {
		return globalRotation;
	}

	public FloatMatrix4 getLocalTransformation() {
		return bones.get(bones.size() - 1).getGlobalTransformation();
	}

	public FloatMatrix4 getGlobalTransformation() {
		return globalTransformation;
	}

	public FloatVector3 getEndPoint() {
		return bones.get(bones.size() - 1).getEndPoint();
	}

	public FloatVector3 getEndEffector() {
		return bones.get(bones.size() - 1).getEndPoint();
	}

	public FloatVector3 getEndEffectorDirectionUV() {
		return bones.get(bones.size() - 1).getGlobalDirectionUV();
	}

	public FloatVector3[] getEndPoints() {
		FloatVector3[] res = new FloatVector3[bones.size()];
		for (int i = 0; i < bones.size(); i++) {
			res[i] = bones.get(i).getEndPoint();
		}
		return res;
	}

	public FloatVector3 getEndPoint(int id) {
		return bones.get(id).getEndPoint();
	}

	public float[] getAnglesRads() {
		float[] res = new float[bones.size()];
		for (int i = 0; i < bones.size(); i++) {
			res[i] = bones.get(i).getAngleRads();
		}
		return res;
	}

	public float[] getAnglesDegs() {
		float[] res = new float[bones.size()];
		for (int i = 0; i < bones.size(); i++) {
			res[i] = bones.get(i).getAngleDegs();
		}
		return res;
	}

	public float getAngleRads(int id) {
		return bones.get(id).getAngleRads();
	}

	public float getAngleDegs(int id) {
		return bones.get(id).getAngleDegs();
	}

	public void setAnglesRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).setAngleRads(anglesRads[i]);
		}
	}

	public void setAnglesDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).setAngleDegs(anglesDegs[i]);
		}
	}

	public void setAngleRads(int id, float angleRads) {
		bones.get(id).setAngleRads(angleRads);
	}

	public void setAngleDegs(int id, float angleDegs) {
		bones.get(id).setAngleDegs(angleDegs);
	}

	public void updateAnglesRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).updateAngleRads(anglesRads[i]);
		}
	}

	public void updateAnglesDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).updateAngleDegs(anglesDegs[i]);
		}
	}

	public void updateAngleRads(int id, float angleRads) {
		bones.get(id).updateAngleRads(angleRads);
	}

	public void updateAngleDegs(int id, float angleDegs) {
		bones.get(id).updateAngleDegs(angleDegs);
	}

	public void setTargetsRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).setTargetRads(anglesRads[i]);
		}
	}

	public void setTargetsDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).setTargetDegs(anglesDegs[i]);
		}
	}

	public void setTargetRads(int id, float angleRads) {
		bones.get(id).setTargetRads(angleRads);
	}

	public void setTargetDegs(int id, float angleDegs) {
		bones.get(id).setTargetDegs(angleDegs);
	}

	public void abortTargetFollowing() {
		for (Bone bone : bones) {
			bone.joint.abortTargetFollowing();
		}
	}

	public FloatVector3 recalculateTransformations() {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).recalculateTransformation();
		}
		return bones.get(bones.size() - 1).getEndPoint();
	}

	public FloatVector3 recalculateTransformation(int id) {
		for (int i = id; i < bones.size(); i++) {
			bones.get(i).recalculateTransformation();
		}
		return bones.get(bones.size() - 1).getEndPoint();
	}

	public void lock() {
		pointsLock.add(bones.get(0).getStartPoint());
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).lock();
			anglesLock.add(bones.get(i).getAngleDegs());
			pointsLock.add(bones.get(i).getEndPoint());
		}
		locked = true;
	}

	public void unlock() {
		for (int i = bones.size() - 1; i >= 0; i--) {
			bones.get(i).setAngleDegs(anglesLock.remove(i));
		}
		recalculateTransformations();
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).unlock();
		}
		locked = false;
		pointsLock.clear();
	}

	@Override
	public void dataChanged(int id) {
		recalculateTransformation(id);
	}

	@Override
	public void dataChangedTo(int id, Float value) {
	}

	public void render(HApplet drawer) {
		drawer.pushMatrix();
		drawer.pushStyle();
		drawer.pushMatrix();
		if (drawer.lightWeight) {
			renderLines(drawer);
		} else {
			drawer.pushMatrix();
			drawer.fill(160);
			drawer.stroke(0, 180);
			renderBones(drawer);
			drawer.stroke(20);
			drawer.popMatrix();
			renderLines(drawer);
		}
		drawer.popMatrix();
		drawer.fill(180);
		drawer.cylinder(14, 12, 6, 10);
		drawer.popMatrix();
		drawer.popStyle();
	}

	private void renderBones(HApplet drawer) {
	}

	private void renderLines(HApplet drawer) {
		int dof = bones.size();
		if (dof < 1) {
			return;
		}
		// Lockable implementation
		if (!locked) {
			for (int i = 0; i < dof; i++) {
				drawer.strokeWeight(dof + 2 - i);
				drawer.line(bones.get(i).getStartPoint(), bones.get(i).getEndPoint());
			}
		} else {
			for (int i = 0; i < dof; i++) {
				drawer.strokeWeight(dof + 2 - i);
				drawer.line(pointsLock.get(i), pointsLock.get(i + 1));
			}
		}
		drawer.strokeWeight(1);
	}

	@Override
	public void setupDrawer(HApplet drawer) {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).joint.addDataAmountChangeListener((PWindow) drawer);
		}
	}

}
