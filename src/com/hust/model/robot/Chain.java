package com.hust.model.robot;

import java.util.ArrayList;

import com.hust.utils.data.FloatMatrix4;
import com.hust.utils.data.FloatQuaternion;
import com.hust.utils.data.FloatVector3;
import com.hust.view.demo.Drawable;
import com.hust.view.demo.HApplet;

public class Chain implements Drawable<HApplet>, Lockable {
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

	public ArrayList<Bone> bones = new ArrayList<Bone>();

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
			bones.get(i).joint.setAngleRads(anglesRads[i]);
		}
	}

	public void setAnglesDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.setAngleDegs(anglesDegs[i]);
		}
	}

	public void setAngleRads(int id, float angleRads) {
		bones.get(id).joint.setAngleRads(angleRads);
	}

	public void setAngleDegs(int id, float angleDegs) {
		bones.get(id).joint.setAngleDegs(angleDegs);
	}

	public void updateAnglesRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.updateAngleRads(anglesRads[i]);
		}
	}

	public void updateAnglesDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.updateAngleDegs(anglesDegs[i]);
		}
	}

	public void updateAngleRads(int id, float angleRads) {
		bones.get(id).joint.updateAngleRads(angleRads);
	}

	public void updateAngleDegs(int id, float angleDegs) {
		bones.get(id).joint.updateAngleDegs(angleDegs);
	}

	public void setTargetsRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.setTargetRads(anglesRads[i]);
		}
	}

	public void setTargetsDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.setTargetDegs(anglesDegs[i]);
		}
	}

	public void setTargetRads(int id, float angleRads) {
		bones.get(id).joint.setTargetRads(angleRads);
	}

	public void setTargetDegs(int id, float angleDegs) {
		bones.get(id).joint.setTargetDegs(angleDegs);
	}

	public void prepareTargetsRads(float... anglesRads) {
		int loops = Math.min(bones.size(), anglesRads.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.prepareTargetRads(anglesRads[i]);
		}
	}

	public void prepareTargetsDegs(float... anglesDegs) {
		int loops = Math.min(bones.size(), anglesDegs.length);
		for (int i = 0; i < loops; i++) {
			bones.get(i).joint.prepareTargetDegs(anglesDegs[i]);
		}
	}

	/**
	 * Use with prepare target to start trajectory silmutanously in all joints.
	 */
	public void actuate() {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).joint.updateToTarget();
		}
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
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).lock();
		}
	}

	public void unlock() {
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i).unlock();
		}
		recalculateTransformations();
	}

	public void render(HApplet drawer) {
		drawer.pushMatrix();
		drawer.pushStyle();

		for (Bone b : bones) {
			b.render(drawer);
		}
		// drawer.fill(180);
		// drawer.cylinder(14, 12, 6, 10);
		drawer.popMatrix();
		drawer.popStyle();
	}

}
