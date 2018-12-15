package com.hust.robot;

import com.hust.utils.FloatMatrix3;
import com.hust.utils.FloatMatrix4;
import com.hust.utils.FloatVector3;
import com.hust.utils.Utils;

public class Bone implements Comparable<Bone>, Lockable {
	public int id;

	public Chain chain;
	public Bone prev;

	public Link link;
	public Joint joint;

	private FloatMatrix4 globalTransformation;

	/**
	 * Base constructor with all values set to default.
	 * 
	 * @param chain Chain containing this bone
	 */
	public Bone(Chain chain) {
		this.chain = chain;
		this.id = chain.getDof();
		if (this.id > 0) {
			prev = chain.getBone(this.id - 1);
		}
		recalculateTransformation();
	}

	/**
	 * Complete constructor.
	 * 
	 * @param chain        Chain containing this bone
	 * @param direction    Direction of the link of this bone
	 * @param rotationAxis Rotation axis vector from which the joint of this bone
	 *                     can rotate around
	 * @param angleDegs    The default angle
	 * @param lowerLimit   The lower limit to the angle the joint of this bone can
	 *                     rotate
	 * @param upperLimit   The upper limit to the angle the joint of this bone can
	 *                     rotate
	 */
	public Bone(Chain chain, FloatVector3 direction, FloatVector3 rotationAxis, float angleDegs, float lowerLimit,
			float upperLimit) {
		this.chain = chain;
		this.joint = new Joint(this, rotationAxis, angleDegs, lowerLimit, upperLimit);
		this.link = new Link(direction);
		this.id = chain.getDof();
		if (this.id > 0) {
			prev = chain.getBone(this.id - 1);
		}
		recalculateTransformation();
	}

	/**
	 * Another constructer setting angle limits to default values (-180 and 180
	 * degrees).
	 * 
	 * @param chain        Chain containing this bone
	 * @param direction    Direction of the link of this bone
	 * @param rotationAxis Rotation axis vector from which the joint of this bone
	 *                     can rotate around
	 * @param angleDegs    The default angle
	 */
	public Bone(Chain chain, FloatVector3 direction, FloatVector3 rotationAxis, float angleDegs) {
		this.chain = chain;
		this.joint = new Joint(this, rotationAxis, angleDegs);
		this.link = new Link(direction);
		this.id = chain.getDof();
		if (this.id > 0) {
			prev = chain.getBone(this.id - 1);
		}
		recalculateTransformation();
	}

	public FloatMatrix4 getLocalTransformation() {
		return FloatMatrix4.createTransformationMatrixDegs(link.direction, joint.rotationAxis, joint.angle);
	}

	public FloatMatrix4 getGlobalTransformation() {
		return globalTransformation;
	}

	public FloatMatrix3 getLocalRotation() {
		return FloatMatrix3.createRotationMatrixDegs(joint.rotationAxis, joint.angle);
	}

	public FloatMatrix3 getGlobalRotation() {
		return globalTransformation.toFloatMatrix3();
	}

	public FloatVector3 getLocalTranslation() {
		return link.direction;
	}

	public FloatVector3 getGlobalTranslation() {
		return globalTransformation.getOrigin();
	}

	public FloatMatrix4 transform(FloatMatrix4 transformation) {
		return transformation.transformDegs(link.direction, joint.rotationAxis, joint.angle);
	}

	public FloatVector3 getLocalDirectionUV() {
		return link.direction.normalize();
	}

	public FloatVector3 getGlobalDirectionUV() {
		return globalTransformation.toFloatMatrix3().mul(FloatVector3.Z_AXIS).normalize();
	}

	public FloatVector3 getEndPoint() {
		return globalTransformation.getOrigin();
	}

	public FloatVector3 getStartPoint() {
		if (id > 0) {
			return prev.getEndPoint();
		}
		return FloatVector3.ZERO;
	}

	public boolean hasPrevBone() {
		if (id > 0) {
			return true;
		}
		return false;
	}

	public Bone getPrevBone() {
		return prev;
	}

	public void recalculateTransformation() {
		if (id > 0) {
			globalTransformation = prev.globalTransformation.transformDegs(link.direction, joint.rotationAxis,
					joint.angle);
		} else {
			globalTransformation = chain.getGlobalTransformation().mul(getLocalTransformation());
		}
	}

	public FloatVector3 getLocalRotationAxis() {
		return joint.rotationAxis;
	}

	public FloatVector3 getGlobalRotationAxis() {
		return globalTransformation.toFloatMatrix3().mul(joint.rotationAxis);
	}

	public float getAngleRads() {
		return joint.angle * Utils.DEGS_TO_RADS;
	}

	public float getAngleDegs() {
		return joint.angle;
	}

	public void setAngleRads(float angleRads) {
		joint.setAngleRads(angleRads);
	}

	public void setAngleDegs(float angleDegs) {
		joint.setAngleDegs(angleDegs);
	}

	public void updateAngleRads(float changeRads) {
		joint.updateAngleRads(changeRads);
	}

	public void updateAngleDegs(float changeDegs) {
		joint.updateAngleDegs(changeDegs);
	}

	public void setTargetRads(float angleRads) {
		joint.setTargetRads(angleRads);
	}

	public void setTargetDegs(float angleDegs) {
		joint.setTargetDegs(angleDegs);
	}

	@Override
	public int compareTo(Bone o) {
		return (int) Math.signum(this.id - o.id);
	}

	@Override
	public void lock() {
		joint.lock();
	}

	@Override
	public void unlock() {
		joint.unlock();
	}
}
