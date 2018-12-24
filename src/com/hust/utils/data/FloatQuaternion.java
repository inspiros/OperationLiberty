package com.hust.utils.data;

import com.hust.utils.Utils;

/**
 * A custom quaternion.
 * <p>
 * Elements of this Quaternion class are:
 * <p>
 * m00 m01 m02
 * <p>
 * m10 m11 m12
 * <p>
 * m20 m21 m22
 * <p>
 * Quaternions represent rotational transformation, somewhat similar to
 * axis/angle. You can think of the x, y, z as axis and w as angle.
 * <p>
 * Properties are publicly accessible for performance reasons.
 * 
 * @author Al Lansley
 * @version 0.4 - 28/10/2018
 */

public class FloatQuaternion {

	public float x, y, z, w;

	/** Default constructor - all matrix elements are set to 0.0f. */
	public FloatQuaternion() {
	}

	/**
	 * Constructor which sets the quaternion from four floats.
	 * 
	 * @param xAxis The positive X-axis to set.
	 * @param yAxis The positive Y-axis to set.
	 * @param zAxis The positive Z-axis to set.
	 */
	public FloatQuaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param source The source quaternion
	 */
	public FloatQuaternion(FloatQuaternion source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
		this.w = source.w;
	}

	/** Zero all elements of this quaternion. */
	public void zero() {
		x = y = z = w = 0.0f;
	}

	/**
	 * Inverse this quaternion.
	 * 
	 * @return This quaternion after inversion.
	 */
	public FloatQuaternion inverse() {
		float length = 1.0f / (x * x + y * y + z * z + w * w);
		x *= -length;
		y *= -length;
		z *= -length;
		w *= length;
		return this;
	}

	/**
	 * Return a matrix which is the inverse of the provided quaternion.
	 *
	 * @param q The quaternion to invert.
	 * @return The inverse quaternion of of the provided quaternion argument.
	 */
	public static FloatQuaternion inverse(FloatQuaternion q) {
		FloatQuaternion temp = new FloatQuaternion(q);
		float length = 1.0f / (q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w);
		temp.x *= -length;
		temp.y *= -length;
		temp.z *= -length;
		temp.w *= length;
		return temp;
	}

	/**
	 * Normalize this quaternion.
	 * 
	 * @return This quaternion after normalized.
	 */
	public FloatQuaternion normalize() {
		float magnitude = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
		this.x /= magnitude;
		this.y /= magnitude;
		this.z /= magnitude;
		this.w /= magnitude;
		return this;
	}

	/**
	 * Normalize without changing quaternion.
	 * 
	 * @return The normalized version of this quaternion.
	 */
	public FloatQuaternion normalized() {
		FloatQuaternion quaternion = new FloatQuaternion(this);

		float magnitude = (float) Math.sqrt(quaternion.x * quaternion.x + quaternion.y * quaternion.y
				+ quaternion.z * quaternion.z + quaternion.w * quaternion.w);
		quaternion.x /= magnitude;
		quaternion.y /= magnitude;
		quaternion.z /= magnitude;
		quaternion.w /= magnitude;
		return quaternion;
	}

	/**
	 * Return the magnitude of this quaternion.
	 * 
	 * @return Return the magnitude of this quaternion.
	 */
	public float magnitude() {
		return (float) Math.abs(x * x + y * y + z * z + w * w);
	}

	/**
	 * Return the rotation axis extracted from this quaternion.
	 * 
	 * @return Return the axis vector extracted.
	 */
	public FloatVector3 getAxis() {
		FloatVector3 axis = new FloatVector3();

		normalize();
		float sin = (float) Math.sqrt(1.0f - w * w);
		axis.x = x / sin;
		axis.y = y / sin;
		axis.z = z / sin;

		return axis;
	}

	/**
	 * Return the angle in radians extracted from this quaternion.
	 * 
	 * @return Return the angle in radians extracted.
	 */
	public float getAngleRads() {
		return (float) Math.acos(w) * 2;
	}

	/**
	 * Return the angle in degrees extracted from this quaternion.
	 * 
	 * @return Return the angle in degrees extracted.
	 */
	public float getAngleDegs() {
		return (float) Math.acos(w) * 2 * Utils.RADS_TO_DEGS;
	}

	/**
	 * Multiplication of quaternions.
	 * 
	 * @param q The quaternion to multiply with.
	 * @return Result of quaternion multiplication.
	 */
	public FloatQuaternion mul(FloatQuaternion q) {
		FloatQuaternion temp = new FloatQuaternion();
//		temp.x = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
//		temp.y = this.w * q.y - this.x * q.z + this.y * q.w + this.z * q.x;
//		temp.z = this.w * q.z + this.x * q.y - this.y * q.x + this.z * q.w;
//		temp.w = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
		temp.x = this.w * q.x + this.x * q.w + this.z * q.y - this.y * q.z;
		temp.y = this.w * q.y + this.y * q.w + this.x * q.z - this.z * q.x;
		temp.z = this.w * q.z + this.z * q.w + this.y * q.x - this.x * q.y;
		temp.w = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
		return temp;
	}

	/**
	 * Multiplication of quaternion and vector.
	 * 
	 * @param v The vector to multiply with.
	 * @return Result of quaternion and vector multiplication.
	 */
	public FloatVector3 mul(FloatVector3 v) {
		FloatQuaternion vec = new FloatQuaternion();
		vec.x = v.x;
		vec.y = v.y;
		vec.z = v.z;
		FloatQuaternion inverse = FloatQuaternion.inverse(this);
		vec = this.mul(vec.mul(inverse));
		return new FloatVector3(vec.x, vec.y, vec.z);
	}

	/**
	 * Create a quaternion from a rotation axis and an angle in radians.
	 * 
	 * @param axis      The rotation axis vector.
	 * @param angleRads The angle.
	 * @return The quaternion representing the rotation.
	 */
	public static FloatQuaternion createQuaternionRads(FloatVector3 axis, float angleRads) {
		FloatQuaternion quaternion = new FloatQuaternion();
		FloatVector3 normAxis = axis.normalized();
		angleRads *= -0.5f;
		float sin = (float) Math.sin(angleRads);

		quaternion.x = normAxis.x * sin;
		quaternion.y = normAxis.y * sin;
		quaternion.z = normAxis.z * sin;
		quaternion.w = (float) Math.cos(angleRads);

		return quaternion;
	}

	/**
	 * Create a quaternion from a rotation axis and an angle in degrees.
	 * 
	 * @param axis      The rotation axis vector.
	 * @param angleDegs The angle.
	 * @return The quaternion representing the rotation.
	 */
	public static FloatQuaternion createQuaternionDegs(FloatVector3 axis, float angleDegs) {
		return createQuaternionRads(axis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Rotate this quaternion by the provided angle about the specified axis.
	 * 
	 * @param angleRads The angle to rotate the matrix, specified in radians.
	 * @param axis      The axis to rotate this matrix about, relative to the
	 *                  current configuration of this matrix.
	 * @return The rotated version of this quaternion.
	 */
	public FloatQuaternion rotateRads(FloatVector3 axis, float angleRads) {
		FloatQuaternion quaternion = new FloatQuaternion(this);
		return quaternion.mul(createQuaternionRads(axis, angleRads));
	}

	/**
	 * Rotate this quaternion by the provided angle about the specified axis.
	 * 
	 * @param angleDegs The angle to rotate the matrix, specified in degrees.
	 * @param axis      The axis to rotate this matrix about, relative to the
	 *                  current configuration of this matrix.
	 * @return The rotated version of this quaternion.
	 */
	public FloatQuaternion rotateDegs(FloatVector3 axis, float angleDegs) {
		FloatQuaternion quaternion = new FloatQuaternion(this);
		return quaternion.mul(createQuaternionDegs(axis, angleDegs));
	}

	/**
	 * Return the equivalent rotation matrix 3 x 3 version of this quaternion.
	 * 
	 * @return The rotation matrix 3 x 3.
	 */
	public FloatMatrix3 toFloatMatrix3() {
		FloatMatrix3 rotMat = new FloatMatrix3();
		rotMat.m00 = 1.0f - 2.0f * (this.y * this.y + this.z * this.z);
		rotMat.m01 = 2.0f * (this.x * this.y + this.z * this.w);
		rotMat.m02 = 2.0f * (this.x * this.z - this.y * this.w);
		rotMat.m10 = 2.0f * (this.x * this.y - this.z * this.w);
		rotMat.m11 = 1.0f - 2.0f * (this.x * this.x + this.z * this.z);
		rotMat.m12 = 2.0f * (this.y * this.z + this.x * this.w);
		rotMat.m20 = 2.0f * (this.x * this.z + this.y * this.w);
		rotMat.m21 = 2.0f * (this.y * this.z - this.x * this.w);
		rotMat.m22 = 1.0f - 2.0f * (this.x * this.x + this.y * this.y);
		return rotMat;
	}

	/**
	 * Return the equivalent rotation matrix 4 x 4 version of this quaternion.
	 * 
	 * @return The rotation matrix 4 x 4.
	 */
	public FloatMatrix4 toFloatMatrix4() {
		FloatMatrix4 rotMat = new FloatMatrix4();
		rotMat.m00 = 1.0f - 2.0f * (this.y * this.y + this.z * this.z);
		rotMat.m01 = 2.0f * (this.x * this.y + this.z * this.w);
		rotMat.m02 = 2.0f * (this.x * this.z - this.y * this.w);
		rotMat.m10 = 2.0f * (this.x * this.y - this.z * this.w);
		rotMat.m11 = 1.0f - 2.0f * (this.x * this.x + this.z * this.z);
		rotMat.m12 = 2.0f * (this.y * this.z + this.x * this.w);
		rotMat.m20 = 2.0f * (this.x * this.z + this.y * this.w);
		rotMat.m21 = 2.0f * (this.y * this.z - this.x * this.w);
		rotMat.m22 = 1.0f - 2.0f * (this.x * this.x + this.y * this.y);
		rotMat.m33 = 1.0f;
		return rotMat;
	}

	/**
	 * Return the array of this Quaternion
	 */
	public float[] toArray() {
		float[] res = new float[4];
		res[0] = x;
		res[1] = y;
		res[2] = z;
		res[3] = w;
		return res;
	}

	// Overloaded toString method
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + Utils.DECIMAL_FORMAT.format(x) + ", y: " + Utils.DECIMAL_FORMAT.format(y) + ", z: "
				+ Utils.DECIMAL_FORMAT.format(z) + ", w: " + Utils.DECIMAL_FORMAT.format(w));
		return sb.toString();
	}

	/**
	 * Print this quaternion.
	 */
	public void print() {
		System.out.println(this.toString());
	}

}
