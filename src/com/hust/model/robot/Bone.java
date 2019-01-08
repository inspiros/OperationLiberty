package com.hust.model.robot;

import com.hust.utils.Lockable;
import com.hust.utils.Utils;
import com.hust.utils.algebra.FloatMatrix4;
import com.hust.utils.algebra.FloatQuaternion;
import com.hust.utils.algebra.FloatVector3;
import com.hust.view.demo.Drawable;
import com.hust.view.demo.HApplet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Bone implements Comparable<Bone>, Drawable<HApplet>, Lockable {
	public int id;

	public Chain chain;

	public Bone prev;

	public final Link link;
	public final Joint joint;

	/**
	 * Global rotation after
	 */
	public ObjectProperty<FloatQuaternion> globalRotation = new SimpleObjectProperty<FloatQuaternion>();
	/**
	 * Global translation after
	 */
	public ObjectProperty<FloatVector3> globalTranslation = new SimpleObjectProperty<FloatVector3>();
	// private FloatVector3 globalTranslation;

	/**
	 * Lockable implementation.
	 */
	private FloatQuaternion globalRotationLock;
	private FloatVector3 globalTranslationLock;
	private boolean locked;

	/**
	 * Base constructor with all values set to default.
	 * 
	 * @param chain Chain containing this bone
	 */
	public Bone(Chain chain) {
		this.chain = chain;
		this.joint = new Joint(this);
		this.link = new Link();
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
		return FloatMatrix4.createTransformationMatrixDegs(link.direction, joint.rotationAxis, joint.angle.get());
	}

	public FloatMatrix4 getGlobalTransformation() {
		return new FloatMatrix4(globalRotation.get(), globalTranslation.get());
	}

	public FloatQuaternion getLocalRotation() {
		return FloatQuaternion.createQuaternionDegs(joint.rotationAxis, joint.angle.get());
	}

	public FloatQuaternion getGlobalRotation() {
		return globalRotation.get();
	}

	public FloatVector3 getLocalTranslation() {
		return link.direction;
	}

	public FloatVector3 getGlobalTranslation() {
		return globalTranslation.get();
	}

	public FloatVector3 getLocalDirectionUV() {
		return link.direction.normalized();
	}

	public FloatVector3 getGlobalDirectionUV() {
		return globalRotation.get().mul(FloatVector3.Z_AXIS).normalized();
	}

	public FloatVector3 getLocalXAxis() {
		return globalRotation.get().mul(FloatVector3.X_AXIS).normalized();
	}

	public FloatVector3 getLocalYAxis() {
		return globalRotation.get().mul(FloatVector3.Y_AXIS).normalized();
	}

	public FloatVector3 getLocalZAxis() {
		return globalRotation.get().mul(FloatVector3.Z_AXIS).normalized();
	}

	public FloatVector3 getEndPoint() {
		return globalTranslation.get();
	}

	public FloatVector3 getStartPoint() {
		if (id > 0) {
			return prev.globalTranslation.get();
		}
		return chain.globalTranslation;
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
			globalRotation.set(prev.globalRotation.get()
					.mul(FloatQuaternion.createQuaternionDegs(joint.rotationAxis, joint.angle.get())));
			globalTranslation.set(prev.globalTranslation.get().add(globalRotation.get().mul(link.direction)));
		} else {
			globalRotation.set(chain.globalRotation
					.mul(FloatQuaternion.createQuaternionDegs(joint.rotationAxis, joint.angle.get())));
			globalTranslation.set(chain.globalTranslation.add(globalRotation.get().mul(link.direction)));
		}
	}

	public FloatVector3 getLocalRotationAxis() {
		return joint.rotationAxis;
	}

	public FloatVector3 getGlobalRotationAxis() {
		return globalRotation.get().mul(joint.rotationAxis);
	}

	public float getAngleRads() {
		return joint.angle.get() * Utils.DEGS_TO_RADS;
	}

	public float getAngleDegs() {
		return joint.angle.get();
	}

//	Removed in purpose of improving performance.
//	public void setAngleRads(float angleRads) {
//		joint.setAngleRads(angleRads);
//	}
//
//	public void setAngleDegs(float angleDegs) {
//		joint.setAngleDegs(angleDegs);
//	}
//
//	public void updateAngleRads(float changeRads) {
//		joint.updateAngleRads(changeRads);
//	}
//
//	public void updateAngleDegs(float changeDegs) {
//		joint.updateAngleDegs(changeDegs);
//	}
//
//	public void setTargetRads(float angleRads) {
//		joint.setTargetRads(angleRads);
//	}
//
//	public void setTargetDegs(float angleDegs) {
//		joint.setTargetDegs(angleDegs);
//	}
//	
//	public void prepareTargetRads(float angleRads) {
//		joint.prepareTargetRads(angleRads);
//	}
//	
//	public void prepareTargetDegs(float angleDegs) {
//		joint.prepareTargetDegs(angleDegs);
//	}

	@Override
	public int compareTo(Bone o) {
		return (int) Math.signum(this.id - o.id);
	}

	@Override
	public void lock() {
		globalRotationLock = globalRotation.get();
		globalTranslationLock = globalTranslation.get();
		joint.lock();
		locked = true;
	}

	@Override
	public void unlock() {
		globalRotation.set(globalRotationLock);
		globalTranslation.set(globalTranslationLock);
		joint.unlock();
		locked = false;
	}

	@Override
	public void render(HApplet drawer) {
		renderJoint(drawer);
		renderLink(drawer);
	}

	public void renderLink(HApplet drawer) {
		drawer.stroke(20);

		if (!locked) {
			drawer.strokeWeight(chain.getDof() + 2 - id);
			drawer.line(getStartPoint(), getEndPoint());
		} else {
			drawer.strokeWeight(chain.getDof() + 2 - id);
			if (id > 0) {
				drawer.line(prev.globalTranslationLock, globalTranslationLock);
			} else {
				drawer.line(chain.globalTranslation, globalTranslationLock);
			}
		}
		drawer.strokeWeight(1);
	}

	public void renderJoint(HApplet drawer) {
		drawer.fill(200);
		drawer.stroke(0, 180);

		FloatQuaternion rot;
		if (!locked) {
			rot = globalRotation.get();
		} else {
			rot = globalRotationLock;
		}

		FloatVector3 prependicular = rot.mul(FloatVector3.Z_AXIS);
		if (joint.rotationAxis.equals(FloatVector3.Z_AXIS)) {
			prependicular = rot.mul(FloatVector3.Y_AXIS);
		}

		FloatVector3 start;
		if (id > 0) {
			if (!locked) {
				start = prev.globalTranslation.get();
			} else {
				start = prev.globalTranslationLock;
			}
		} else {
			start = chain.globalTranslation;
		}

		FloatVector3 norm = rot.mul(joint.rotationAxis);
		drawer.noStroke();
		drawer.cylinder(start.add(norm.mul(2)), start.add(norm.mul(-2)), prependicular, 5, 5, 12);
	}

}
