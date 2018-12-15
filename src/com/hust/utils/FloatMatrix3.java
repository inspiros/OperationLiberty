package com.hust.utils;

/**
 * A custom 3x3 matrix.
 * <p>
 * Elements of this FloatMatrix3 class are:
 * <p>
 * m00 m01 m02
 * <p>
 * m10 m11 m12
 * <p>
 * m20 m21 m22
 * <p>
 * Matrices can be considered as are column-major - although when treating them
 * as arrays this makes no difference.
 * <p>
 * mXX properties are publicly accessible for performance reasons.
 * 
 * @author Al Lansley
 * @version 0.4 - 28/10/2018
 */

public class FloatMatrix3 {

	public float m00, m01, m02; // First row
	public float m10, m11, m12; // Second row
	public float m20, m21, m22; // Third row

	/** Default constructor - all matrix elements are set to 0.0f. */
	public FloatMatrix3() {
	}

	/**
	 * Constructor which sets the given value across the diagonal and zeroes the
	 * rest of the matrix.
	 * <p>
	 * So for example to create an identity matrix you could just call: FloatMatrix3
	 * m = new FloatMatrix3(1.0f).
	 * 
	 * @param value The value to set across the diagonal of the constructed matrix
	 *              (i.e. elements m00, m11, and m22).
	 */
	public FloatMatrix3(float value) {
		// Set diagonal
		m00 = m11 = m22 = value;
	}

	/**
	 * Constructor which sets the matrix from nine floats.
	 *
	 * @param m00 The first element.
	 * @param m01 The second element.
	 * @param m02 The third element.
	 * @param m10 The first element.
	 * @param m11 The second element.
	 * @param m12 The third element.
	 * @param m20 The first element.
	 * @param m21 The second element.
	 * @param m22 The third element.
	 */
	public FloatMatrix3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21,
			float m22) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	/**
	 * Constructor which sets the matrix from the three provided axes.
	 * 
	 * @param xAxis The positive X-axis to set.
	 * @param yAxis The positive Y-axis to set.
	 * @param zAxis The positive Z-axis to set.
	 */
	public FloatMatrix3(FloatVector3 xAxis, FloatVector3 yAxis, FloatVector3 zAxis) {
		m00 = xAxis.x;
		m10 = xAxis.y;
		m20 = xAxis.z;

		m01 = yAxis.x;
		m11 = yAxis.y;
		m21 = yAxis.z;

		m02 = zAxis.x;
		m12 = zAxis.y;
		m22 = zAxis.z;
	}

	/** Zero all elements of this matrix. */
	public void zero() {
		m00 = m01 = m02 = m10 = m11 = m12 = m20 = m21 = m22 = 0.0f;
	}

	/** Reset this matrix to identity. */
	public void setIdentity() {
		// Set diagonal and then zero the rest of the matrix
		m00 = m11 = m22 = 1.0f;
		m10 = m20 = m01 = m21 = m02 = m12 = 0.0f;
	}

	/**
	 * Transpose and return this matrix
	 * 
	 * @return This matrix after transposed.
	 */
	public FloatMatrix3 transpose() {
		float temp = m01;
		m01 = m10;
		m10 = temp;

		temp = m02;
		m02 = m20;
		m20 = temp;

		temp = m12;
		m12 = m21;
		m21 = temp;

		return this;
	}

	/**
	 * Return a new matrix which is the transposed version of the provided matrix.
	 * 
	 * @param m The matrix which we will transpose (this matrix is not modified)
	 * @return A transposed version of the provided matrix.
	 */
	public FloatMatrix3 transposed() {
		return new FloatMatrix3(m00, m10, m20, m01, m11, m21, m02, m12, m22);
	}

	/**
	 * Return a new matrix which is the transposed version of the provided matrix.
	 * 
	 * @param m The matrix which we will transpose (this matrix is not modified)
	 * @return A transposed version of the provided matrix.
	 */
	public static FloatMatrix3 transpose(FloatMatrix3 m) {
		return new FloatMatrix3(m.m00, m.m10, m.m20, m.m01, m.m11, m.m21, m.m02, m.m12, m.m22);
	}

	/**
	 * Return the angle in radians extracted from the rotation matrix.
	 * 
	 * @param m The rotation matrix
	 * @return Return the angle in radians extracted from the rotation matrix.
	 */
	public static float getAngleRads(FloatMatrix3 m) {
		float a = m.m21 - m.m12;
		float b = m.m02 - m.m20;
		float c = m.m10 - m.m01;
		return (float) Math.asin(Math.abs(Math.sqrt(a * a + b * b + c * c)) / 2);
	}

	/**
	 * Return the angle in degrees extracted from the rotation matrix.
	 * 
	 * @param m The rotation matrix
	 * @return Return the angle in degrees extracted from the rotation matrix.
	 */
	public static float getAngleDegs(FloatMatrix3 m) {
		return getAngleRads(m) * Utils.RADS_TO_DEGS;
	}

	/**
	 * Calculate and return the angle between 2 rotation matrices in radians
	 * 
	 * @param m1 First rotation matrix
	 * @param m2 Second rotation matrix
	 * @return Angle between rotation matrices
	 */
	public static float getAngleBetweenRads(FloatMatrix3 m1, FloatMatrix3 m2) {
		return (float) Math.acos((m1.mul(m2.transpose()).trace() - 1) / 2);
	}

	/**
	 * Calculate and return the angle between 2 rotation matrices in degrees
	 * 
	 * @param m1 First rotation matrix
	 * @param m2 Second rotation matrix
	 * @return Angle between rotation matrices
	 */
	public static float getAngleBetweenDegs(FloatMatrix3 m1, FloatMatrix3 m2) {
		return getAngleBetweenRads(m1, m2) * Utils.RADS_TO_DEGS;
	}

	/**
	 * Create a rotation matrix from a given rotation axis and angle.
	 * 
	 * @param rotationAxis The vector to use as the rotation Axis.
	 * @param angle        The angle rotated around the rotation Axis in radians.
	 * @return The created rotation matrix.
	 */
	public static FloatMatrix3 createRotationMatrixRads(FloatVector3 rotationAxis, float angleRads) {
		float sin = (float) Math.sin(angleRads);
		float cos = (float) Math.cos(angleRads);
		float nivCos = 1.0f - cos;

		float xy = rotationAxis.x * rotationAxis.y;
		float yz = rotationAxis.y * rotationAxis.z;
		float xz = rotationAxis.x * rotationAxis.z;

		float xs = rotationAxis.x * sin;
		float ys = rotationAxis.y * sin;
		float zs = rotationAxis.z * sin;

		FloatMatrix3 rotMat = new FloatMatrix3();
		rotMat.m00 = cos + rotationAxis.x * rotationAxis.x * nivCos;
		rotMat.m01 = xy * nivCos - zs;
		rotMat.m02 = xz * nivCos + ys;

		rotMat.m10 = xy * nivCos + zs;
		rotMat.m11 = cos + rotationAxis.y * rotationAxis.y * nivCos;
		rotMat.m12 = yz * nivCos - xs;

		rotMat.m20 = xz * nivCos - ys;
		rotMat.m21 = yz * nivCos + xs;
		rotMat.m22 = cos + rotationAxis.z * rotationAxis.z * nivCos;
		return rotMat;
	}

	/**
	 * Create a rotation matrix from a given rotation axis and angle.
	 * 
	 * @param rotationAxis The vector to use as the rotation Axis.
	 * @param angle        The angle rotated around the rotation Axis in degrees.
	 * @return The created rotation matrix.
	 */
	public static FloatMatrix3 createRotationMatrixDegs(FloatVector3 rotationAxis, float angleDegs) {
		return createRotationMatrixRads(rotationAxis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Create a rotation matrix from a given direction.
	 * <p>
	 * The reference direction is aligned to the Z-Axis, and the X-Axis is generated
	 * via the genPerpendicularVectorQuuck() method. The Y-Axis is then the
	 * cross-product of those two axes.
	 * <p>
	 * This method uses the <a href="https://gist.github.com/roxlu/3082114">Frisvad
	 * technique</a> for generating perpendicular axes.
	 * 
	 * @param referenceDirection The vector to use as the Z-Axis
	 * @return The created rotation matrix.
	 *
	 * @see FloatVector3#genPerpendicularVectorQuick(FloatVector3)
	 */
	/*
	 * public static FloatMatrix3 createRotationMatrix(FloatVector3
	 * referenceDirection) { FloatVector3 zAxis = referenceDirection.normalised();
	 * FloatVector3 xAxis =
	 * FloatVector3.genPerpendicularVectorQuick(referenceDirection); // This is
	 * returned normalised FloatVector3 yAxis = FloatVector3.crossProduct(xAxis,
	 * zAxis).normalise(); return new FloatMatrix3(xAxis, yAxis, zAxis); }
	 */

	public static FloatMatrix3 createRotationMatrix(FloatVector3 referenceDirection) {
		/***
		 * OLD VERSION 1.3.4 and earlier FloatVector3 xAxis; FloatVector3 yAxis;
		 * FloatVector3 zAxis = referenceDirection.normalise();
		 * 
		 * // Handle the singularity (i.e. bone pointing along negative Z-Axis)...
		 * if(referenceDirection.z < -0.9999999f) { xAxis = new FloatVector3(1.0f, 0.0f,
		 * 0.0f); // ...in which case positive X runs directly to the right... yAxis =
		 * new FloatVector3(0.0f, 1.0f, 0.0f); // ...and positive Y runs directly
		 * upwards. } else { float a = 1.0f/(1.0f + zAxis.z); float b = -zAxis.x *
		 * zAxis.y * a; xAxis = new FloatVector3(1.0f - zAxis.x * zAxis.x * a, b,
		 * -zAxis.x).normalise(); yAxis = new FloatVector3(b, 1.0f - zAxis.y * zAxis.y *
		 * a, -zAxis.y).normalise(); }
		 * 
		 * return new FloatMatrix3(xAxis, yAxis, zAxis);
		 ***/

		/*** NEW VERSION - 1.3.5 onwards ***/
		FloatMatrix3 rotMat = new FloatMatrix3();

		// Bone direction is bang on the Y-axis singularity? Give it a tiny nudge
		// because you can't cross product two identical vectors.
		if (referenceDirection.y == 1.0f) {
			referenceDirection.y -= 0.0001f;
			referenceDirection.normalize();
		}

		rotMat.setZBasis(referenceDirection);
		rotMat.setXBasis(
				FloatVector3.crossProduct(referenceDirection, new FloatVector3(0.0f, 1.0f, 0.0f)).normalized());
		rotMat.setYBasis(FloatVector3.crossProduct(rotMat.getXBasis(), rotMat.getZBasis()).normalized());

		return rotMat;
	}

//	THIS IS WAAAAAY BETTER THAN THE COMMENTED OUT ABOVE - BUT I THINK REFERENCE ANGLES **MUST** BE HONOURED IN THE
//	FORWARD PASS OR WE END UP WITH JUMPY BULLSHIT RESULTS (SEE LOCAL HINGE WITH REFERENCE CONSTRAINTS)
//
//	PERHAPS genPerpendicularVectorQuick SHOULD USE THE SAME TECHNIQUE TO KEEP EVERYTHING IN ORDER??

	/**
	 * Return the rotation matrix between 2 vectors
	 * 
	 * @param a
	 * @param b
	 * @return Rotation matrix between a and b
	 */
	public static FloatMatrix3 rotBetween(FloatVector3 a, FloatVector3 b) {
		FloatMatrix3 res = new FloatMatrix3(1);
		FloatVector3 mult = a.normalized().mul(b.normalized());
		FloatVector3 cross = a.normalized().cross(b.normalized());
		float cos = mult.x + mult.y + mult.z;
		// float sin = cross.length();
		FloatMatrix3 v = new FloatMatrix3(0, -cross.z, cross.y, cross.z, 0, -cross.x, -cross.y, cross.x, 0);
		/*
		 * res.m00 = res.m11 = cos; res.m01 = -sin; res.m10 = sin; res.m22 = 1;
		 */
		res = new FloatMatrix3(1).add(v).add(v.mul(v).mul(1 / (1 + cos)));
		return res;
	}

	/**
	 * Return whether this matrix consists of three orthogonal axes or not to within
	 * a cross-product of 0.01f.
	 *
	 * @return Whether or not this matrix is orthogonal.
	 */
	public boolean isOrthogonal() {
		float xCrossYDot = FloatVector3.dotProduct(this.getXBasis(), this.getYBasis());
		float xCrossZDot = FloatVector3.dotProduct(this.getXBasis(), this.getZBasis());
		float yCrossZDot = FloatVector3.dotProduct(this.getYBasis(), this.getZBasis());

		if (Utils.approximatelyEquals(xCrossYDot, 0.0f, 0.01f) && Utils.approximatelyEquals(xCrossZDot, 0.0f, 0.01f)
				&& Utils.approximatelyEquals(yCrossZDot, 0.0f, 0.01f)) {
			return true;
		}

		return false;
	}

	public FloatMatrix3 add(FloatMatrix3 m) {
		FloatMatrix3 temp = new FloatMatrix3();

		temp.m00 = this.m00 + m.m00;
		temp.m01 = this.m01 + m.m01;
		temp.m02 = this.m02 + m.m02;

		temp.m10 = this.m10 + m.m10;
		temp.m11 = this.m11 + m.m11;
		temp.m12 = this.m12 + m.m12;

		temp.m20 = this.m20 + m.m20;
		temp.m21 = this.m21 + m.m21;
		temp.m22 = this.m22 + m.m22;

		return temp;
	}

	public FloatMatrix3 mul(float t) {
		FloatMatrix3 temp = new FloatMatrix3();

		temp.m00 = this.m00 * t;
		temp.m01 = this.m01 * t;
		temp.m02 = this.m02 * t;

		temp.m10 = this.m10 * t;
		temp.m11 = this.m11 * t;
		temp.m12 = this.m12 * t;

		temp.m20 = this.m20 * t;
		temp.m21 = this.m21 * t;
		temp.m22 = this.m22 * t;

		return temp;
	}

	/**
	 * Multiply this matrix by another matrix (in effect, combining them) and return
	 * the result as a new FloatMatrix3.
	 * <p>
	 * Neither this matrix or the provided matrix argument are modified by this
	 * process - you must assign the result to your desired combined matrix.
	 * <p>
	 * To create a ModelView matrix using this method you would use
	 * viewMatrix.times(modelMatrix). To create a ModelViewProjection matrix using
	 * this method you would use
	 * projectionMatrix.times(viewMatrix).times(modelMatrix).
	 * 
	 * @param m The matrix to multiply this matrix by.
	 * @return The resulting combined matrix.
	 */
	public FloatMatrix3 mul(FloatMatrix3 m) {
		FloatMatrix3 temp = new FloatMatrix3();

		temp.m00 = this.m00 * m.m00 + this.m01 * m.m10 + this.m02 * m.m20;
		temp.m01 = this.m00 * m.m01 + this.m01 * m.m11 + this.m02 * m.m21;
		temp.m02 = this.m00 * m.m02 + this.m01 * m.m12 + this.m02 * m.m22;

		temp.m10 = this.m10 * m.m00 + this.m11 * m.m10 + this.m12 * m.m20;
		temp.m11 = this.m10 * m.m01 + this.m11 * m.m11 + this.m12 * m.m21;
		temp.m12 = this.m10 * m.m02 + this.m11 * m.m12 + this.m12 * m.m22;

		temp.m20 = this.m20 * m.m00 + this.m21 * m.m10 + this.m22 * m.m20;
		temp.m21 = this.m20 * m.m01 + this.m21 * m.m11 + this.m22 * m.m21;
		temp.m22 = this.m20 * m.m02 + this.m21 * m.m12 + this.m22 * m.m22;

		return temp;
	}

	/**
	 * Multiply a vector by this matrix and return the result as a new FloatVector3.
	 *
	 * @param source The source vector to transform.
	 * @return The provided source vector transformed by this matrix.
	 */
	public FloatVector3 mul(FloatVector3 source) {
		return new FloatVector3(this.m00 * source.x + this.m01 * source.y + this.m02 * source.z,
				this.m10 * source.x + this.m11 * source.y + this.m12 * source.z,
				this.m20 * source.x + this.m21 * source.y + this.m22 * source.z);
	}

	/**
	 * Calculate and return the determinant of this matrix.
	 *
	 * @return The determinant of this matrix.
	 */
	public float determinant() {
		return m02 * m10 * m21 - m02 * m20 * m11 - m01 * m10 * m22 + m01 * m20 * m12 + m00 * m11 * m22
				- m00 * m21 * m12;
	}

	/**
	 * Calculate and return the trace of this matrix
	 * 
	 * @return The trace of this matrix
	 */
	public float trace() {
		return m00 + m11 + m22;
	}

	/**
	 * Return a matrix which is the inverse of the provided matrix.
	 *
	 * @param m The matrix to invert.
	 * @return The inverse matrix of of the provided matrix argument.
	 */
	public static FloatMatrix3 inverse(FloatMatrix3 m) {
		float d = m.determinant();

		FloatMatrix3 temp = new FloatMatrix3();

		temp.m00 = (m.m11 * m.m22 - m.m21 * m.m12) / d;
		temp.m01 = -(-m.m02 * m.m21 + m.m01 * m.m22) / d;
		temp.m02 = (-m.m02 * m.m11 + m.m01 * m.m12) / d;
		temp.m10 = -(m.m10 * m.m22 - m.m20 * m.m12) / d;
		temp.m11 = (-m.m02 * m.m20 + m.m00 * m.m22) / d;
		temp.m12 = -(-m.m02 * m.m10 + m.m00 * m.m12) / d;
		temp.m20 = (m.m10 * m.m21 - m.m20 * m.m11) / d;
		temp.m21 = -(-m.m01 * m.m20 + m.m00 * m.m21) / d;
		temp.m22 = (-m.m10 * m.m20 + m.m00 * m.m11) / d;

		return temp;
	}

	/**
	 * Rotate this matrix by the provided angle about the specified axis.
	 * 
	 * @param angleRads    The angle to rotate the matrix, specified in radians.
	 * @param rotationAxis The axis to rotate this matrix about, relative to the
	 *                     current configuration of this matrix.
	 * @return The rotated version of this matrix.
	 */
	public FloatMatrix3 rotateRads(FloatVector3 rotationAxis, float angleRads) {
		// Note: we need this temporary matrix because we cannot perform this operation
		// 'in-place'.
		FloatMatrix3 dest = new FloatMatrix3();

		float sin = (float) Math.sin(angleRads);
		float cos = (float) Math.cos(angleRads);
		float oneMinusCos = 1.0f - cos;

		float xy = rotationAxis.x * rotationAxis.y;
		float yz = rotationAxis.y * rotationAxis.z;
		float xz = rotationAxis.x * rotationAxis.z;
		float xs = rotationAxis.x * sin;
		float ys = rotationAxis.y * sin;
		float zs = rotationAxis.z * sin;

		float f00 = rotationAxis.x * rotationAxis.x * oneMinusCos + cos;
		float f01 = xy * oneMinusCos - zs;
		float f02 = xz * oneMinusCos + ys;

		float f10 = xy * oneMinusCos + zs;
		float f11 = rotationAxis.y * rotationAxis.y * oneMinusCos + cos;
		float f12 = yz * oneMinusCos - xs;

		float f20 = xz * oneMinusCos - ys;
		float f21 = yz * oneMinusCos + xs;
		float f22 = rotationAxis.z * rotationAxis.z * oneMinusCos + cos;

		float t00 = m00 * f00 + m01 * f10 + m02 * f20;
		float t10 = m10 * f00 + m11 * f10 + m12 * f20;
		float t20 = m20 * f00 + m21 * f10 + m22 * f20;

		float t01 = m00 * f01 + m01 * f11 + m02 * f21;
		float t11 = m10 * f01 + m11 * f11 + m12 * f21;
		float t21 = m20 * f01 + m21 * f11 + m22 * f21;

		// Construct and return rotation matrix
		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = m00 * f02 + m01 * f12 + m02 * f22;

		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = m10 * f02 + m11 * f12 + m12 * f22;

		dest.m20 = t20;
		dest.m21 = t21;
		dest.m22 = m20 * f02 + m21 * f12 + m22 * f22;

		return dest;
	}

	/**
	 * Rotate this matrix by the provided angle about the specified axis.
	 * 
	 * @param angleDegs The angle to rotate the matrix, specified in degrees.
	 * @param localAxis The axis to rotate this matrix about, relative to the
	 *                  current configuration of this matrix.
	 * @return The rotated version of this matrix.
	 */
	public FloatMatrix3 rotateDegs(float angleDegs, FloatVector3 localAxis) {
		return this.rotateRads(localAxis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Set the X basis of this matrix.
	 *
	 * @param v The vector to use as the X-basis of this matrix.
	 */
	public void setXBasis(FloatVector3 v) {
		m00 = v.x;
		m10 = v.y;
		m20 = v.z;
	}

	/**
	 * Get the X basis of this matrix.
	 * 
	 * @return The X basis of this matrix as a FloatVector3
	 **/
	public FloatVector3 getXBasis() {
		return new FloatVector3(m00, m10, m20);
	}

	/**
	 * Set the Y basis of this matrix.
	 *
	 * @param v The vector to use as the Y-basis of this matrix.
	 */
	public void setYBasis(FloatVector3 v) {
		m01 = v.x;
		m11 = v.y;
		m21 = v.z;
	}

	/**
	 * Get the Y basis of this matrix.
	 * 
	 * @return The Y basis of this matrix as a FloatVector3
	 **/
	public FloatVector3 getYBasis() {
		return new FloatVector3(m01, m11, m21);
	}

	/**
	 * Set the Z basis of this matrix.
	 *
	 * @param v The vector to use as the Z-basis of this matrix.
	 */
	public void setZBasis(FloatVector3 v) {
		m02 = v.x;
		m12 = v.y;
		m22 = v.z;
	}

	/**
	 * Get the Z basis of this matrix.
	 * 
	 * @return The Z basis of this matrix as a FloatVector3
	 **/
	public FloatVector3 getZBasis() {
		return new FloatVector3(m02, m12, m22);
	}

	/**
	 * Return a FloatMatrix4 version of this FloatMatrix3.
	 * <p>
	 * The x, y and z axes are returned in the FloatMatrix4, while the w component
	 * of each axis and the origin plus it's w component are zero.
	 * <p>
	 * 
	 * @returnA FloatMatrix4 version of this FloatMatrix3.
	 */
	public FloatMatrix4 toFloatMatrix4() {
		return new FloatMatrix4(this, new FloatVector3(0));
	}

	/**
	 * Return this FloatMatrix3 as an array of 9 floats.
	 * 
	 * @return This FloatMatrix3 as an array of 9 floats.
	 */
	public float[] toArray() {
		return new float[] { m00, m01, m02, m10, m11, m12, m20, m21, m22 };
	}

	// Note: Displays output in COLUMN-MAJOR format!
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + Utils.DECIMAL_FORMAT.format(m00) + ",\t" + Utils.DECIMAL_FORMAT.format(m01) + ",\t"
				+ Utils.DECIMAL_FORMAT.format(m02) + ")" + Utils.NEW_LINE);
		sb.append("(" + Utils.DECIMAL_FORMAT.format(m10) + ",\t" + Utils.DECIMAL_FORMAT.format(m11) + ",\t"
				+ Utils.DECIMAL_FORMAT.format(m12) + ")" + Utils.NEW_LINE);
		sb.append("(" + Utils.DECIMAL_FORMAT.format(m20) + ",\t" + Utils.DECIMAL_FORMAT.format(m21) + ",\t"
				+ Utils.DECIMAL_FORMAT.format(m22) + ")" + Utils.NEW_LINE);
		return sb.toString();
	}

	/**
	 * Print this matrix
	 */
	public void print() {
		System.out.println("Float Matrix 3:\n" + toString());
	}
}
