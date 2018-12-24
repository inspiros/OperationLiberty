package com.hust.robot;

import java.util.ArrayList;

import com.hust.utils.DataChangeListener;
import com.hust.utils.data.FloatMatrix4;
import com.hust.utils.data.FloatQuaternion;
import com.hust.utils.data.FloatVector3;
import com.hust.view.Drawable;
import com.hust.view.HApplet;
import com.hust.view.PWindow;

public class Chain implements DataChangeListener<Float>, Drawable<HApplet> {
	public String name;

	// private HApplet app;

	/**
	 * Pre - starting rotation.
	 */
	public FloatQuaternion globalRotation;
	/**
	 * Pre - starting point.
	 */
	public FloatVector3 globalTranslation;

	private ArrayList<Bone> bones = new ArrayList<Bone>();

	private ArrayList<FloatVector3> pointsLock = new ArrayList<FloatVector3>();

	public Chain() {
		globalRotation = new FloatQuaternion(0, 0, 0, 1);
		globalTranslation = FloatVector3.ZERO;
	}

	/**
	 * Add new Bone to the Chain
	 * 
	 * @param direction    The link direction based on the local axis.
	 * @param rotationAxis The rotation axis of the joint.
	 * @param angle        The default angle in degrees.
	 * @param lowerLimit   The lower limit of anlge rotation.
	 * @param upperLimit   The upper limit of anlge rotation.
	 */
	public void addConsecutiveBone(FloatVector3 direction, FloatVector3 rotationAxis, float angle, float lowerLimit,
			float upperLimit) {
		Bone bone = new Bone(this, direction, rotationAxis, angle, lowerLimit, upperLimit);
		bones.add(bone);
		bones.trimToSize();
		
		pointsLock.add(FloatVector3.ZERO);
		pointsLock.trimToSize();
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

	public FloatQuaternion getLocalRotation() {
		return bones.get(bones.size() - 1).getGlobalRotation();
	}

	public FloatQuaternion getGlobalRotation() {
		return globalRotation;
	}

	public FloatMatrix4 getLocalTransformation() {
		return bones.get(bones.size() - 1).getGlobalTransformation();
	}

	public FloatMatrix4 getGlobalTransformation() {
		return new FloatMatrix4(globalRotation, globalTranslation);
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
			pointsLock.add(bones.get(i).getEndPoint());
		}
	}

	public void unlock() {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).unlock();
		}
		recalculateTransformations();
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
		
		//drawer.fill(180);
		renderBones(drawer);

		//drawer.fill(180);
		//drawer.cylinder(14, 12, 6, 10);
		drawer.popMatrix();
		drawer.popStyle();
	}

	private void renderBones(HApplet drawer) {
		for (Bone b : bones) {
			b.render(drawer);
		}
	}

	@Override
	public void setupDrawer(HApplet drawer) {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).joint.addDataAmountChangeListener((PWindow) drawer);
		}
	}

}
