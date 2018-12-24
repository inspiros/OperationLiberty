package com.hust.utils.data;

import java.text.DecimalFormat;

import com.hust.utils.Utils;

/**
 * A class to represent a 4x4 matrix.
 * <p>
 * The FloatMatrix4 class contains 16 public member properties m00 through m15
 * which are <strong>column major</strong>, so our matrix looks like this:
 * <p>
 * m00 m10 m20 m30
 * <p>
 * m01 m11 m21 m31
 * <p>
 * m02 m12 m22 m32
 * <p>
 * m03 m13 m23 m33
 * <p>
 * For each property the first digit is the column, and the second digit is the
 * row.
 * 
 * The identity matrix specifies a left-handed coordinate system whereby if you
 * put your left hand in front of you with your thumb pointing towards yourself,
 * your index finger pointing directly up and your middle finger pointing to the
 * right, then:
 * <ul>
 * <li>The direction of the middle finger is the positive X-axis,</li>
 * <li>The direction of the index finger is the positive Y-axis, and</li>
 * <li>The direction of the thumb is the positive Z-axis.</li>
 * </ul>
 * <p>
 * This corresponds to the coordinate system used by OpenGL.
 * <p>
 * Rotations are performed in accordance with the right-hand rule, that is, if
 * you were to grip an axis with your thumb pointing towards the positive end of
 * the axis, rotating in a positive direction would follow the curl of your
 * fingers in an anti-clockwise direction, and rotation by a negative amount
 * would go in the opposite (clockwise) direction.
 * 
 * @author Al Lansley
 * @version 0.5 - 22/12/2014
 */
public class FloatMatrix4 {
	private static final float FLOAT_EQUALITY_TOLERANCE = 0.0001f;

	// ---------- Public Properties ---------

	// These properties are deliberately declared as public for fast access without
	// the need
	// to use getters and setters.

	public float m00, m01, m02, m03; // First row
	public float m10, m11, m12, m13; // Second row
	public float m20, m21, m22, m23; // Third row
	public float m30, m31, m32, m33; // Fourth row

	// ---------- Constructors ----------

	/**
	 * Default constructor.
	 * <p>
	 * All member properties are implicitly set to zero by Java as that is the
	 * default value of a float primitive.
	 */
	public FloatMatrix4() {
	}

	/**
	 * Copy constructor.
	 * <p>
	 * Copies all properties from the source matrix into the newly constructed
	 * matrix.
	 * 
	 * @param source The matrix to copy the values from.
	 */
	public FloatMatrix4(FloatMatrix4 source) {
		m00 = source.m00;
		m01 = source.m01;
		m02 = source.m02;
		m03 = source.m03;

		m10 = source.m10;
		m11 = source.m11;
		m12 = source.m12;
		m13 = source.m13;

		m20 = source.m20;
		m21 = source.m21;
		m22 = source.m22;
		m23 = source.m23;

		m30 = source.m30;
		m31 = source.m31;
		m32 = source.m32;
		m33 = source.m33;
	}

	/**
	 * Constructor which sets the matrix from sixteen floats.
	 * 
	 * @param m00
	 * @param m01
	 * @param m02
	 * @param m03
	 * @param m10
	 * @param m11
	 * @param m12
	 * @param m13
	 * @param m20
	 * @param m21
	 * @param m22
	 * @param m23
	 * @param m30
	 * @param m31
	 * @param m32
	 * @param m33
	 */
	public FloatMatrix4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		super();
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Constructor to create a FloatMatrix4 from a FloatQuaternion and an origin.
	 * 
	 * @param rotationQuat The FloatQuaternion to use.
	 * @param origin       The FloatVector3 to use as the origin.
	 */
	public FloatMatrix4(FloatQuaternion rotationQuat, FloatVector3 origin) {
		m00 = 1.0f - 2.0f * (rotationQuat.y * rotationQuat.y + rotationQuat.z * rotationQuat.z);
		m01 = 2.0f * (rotationQuat.x * rotationQuat.y + rotationQuat.z * rotationQuat.w);
		m02 = 2.0f * (rotationQuat.x * rotationQuat.z - rotationQuat.y * rotationQuat.w);
		m03 = origin.x;
		
		m10 = 2.0f * (rotationQuat.x * rotationQuat.y - rotationQuat.z * rotationQuat.w);
		m11 = 1.0f - 2.0f * (rotationQuat.x * rotationQuat.x + rotationQuat.z * rotationQuat.z);
		m12 = 2.0f * (rotationQuat.y * rotationQuat.z + rotationQuat.x * rotationQuat.w);
		m13 = origin.y;
		
		m20 = 2.0f * (rotationQuat.x * rotationQuat.z + rotationQuat.y * rotationQuat.w);
		m21 = 2.0f * (rotationQuat.y * rotationQuat.z - rotationQuat.x * rotationQuat.w);
		m22 = 1.0f - 2.0f * (rotationQuat.x * rotationQuat.x + rotationQuat.y * rotationQuat.y);
		m23 = origin.z;
		
		m33 = 1.0f;
	}

	/**
	 * Constructor to create a FloatMatrix4 from a FloatMatrix3 and an origin.
	 * 
	 * @param rotationMatrix The FloatMatrix3 to use for the X/Y/Z axes.
	 * @param origin         The FloatVector3 to use as the origin.
	 */
	public FloatMatrix4(FloatMatrix3 rotationMatrix, FloatVector3 origin) {
		m00 = rotationMatrix.m00;
		m01 = rotationMatrix.m01;
		m02 = rotationMatrix.m02;
		m03 = origin.x;

		m10 = rotationMatrix.m10;
		m11 = rotationMatrix.m11;
		m12 = rotationMatrix.m12;
		m13 = origin.y;

		m20 = rotationMatrix.m20;
		m21 = rotationMatrix.m21;
		m22 = rotationMatrix.m22;
		m23 = origin.z;

		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Float array constructor.
	 * <p>
	 * The matrix is set a column at a time, so the first four floats from the
	 * source array are set on m0 to m03, the next four on m10 to m13 and so on.
	 * <p>
	 * If the source array is not an array of precisely 16 floats then a
	 * IllegalArgumentException is thrown.
	 * 
	 * @param source The array of 16 floats used as the source values to set on this
	 *               FloatMatrix4.
	 */
	public FloatMatrix4(float[] source) {
		// If we have an array of precisely 16 floats then proceed with initialisation
		if (source.length == 16) {
			m00 = source[0];
			m01 = source[1];
			m02 = source[2];
			m03 = source[3];

			m10 = source[4];
			m11 = source[5];
			m12 = source[6];
			m13 = source[7];

			m20 = source[8];
			m21 = source[9];
			m22 = source[10];
			m23 = source[11];

			m30 = source[12];
			m31 = source[13];
			m32 = source[14];
			m33 = source[15];
		} else {
			throw new IllegalArgumentException("Source array must contain precisely 16 floats.");
		}
	}

	/**
	 * One parameter constructor.
	 * <p>
	 * Sets the provided value diagonally across the matrix. For example, to create
	 * an identity matrix with 1.0f across the diagonal then you can call:
	 * <p>
	 * {@code FloatMatrix4 m = new FloatMatrix4(1.0f)}
	 * <p>
	 * Which will result in a matrix with the following properties:
	 * <p>
	 * 1.0f 0.0f 0.0f 0.0f
	 * <p>
	 * 0.0f 1.0f 0.0f 0.0f
	 * <p>
	 * 0.0f 0.0f 1.0f 0.0f
	 * <p>
	 * 0.0f 0.0f 0.0f 1.0f
	 * 
	 * @param value The value to set across the diagonal of the matrix.
	 */
	public FloatMatrix4(float value) {
		// Set diagonal
		m00 = m11 = m22 = m33 = value;
	}

	// ---------- Methods ----------

	/**
	 * Zero all properties of a matrix.
	 */
	public void zero() {
		m00 = m01 = m02 = m03 = m10 = m11 = m12 = m13 = m20 = m21 = m22 = m23 = m30 = m31 = m32 = m33 = 0.0f;
	}

	/**
	 * Reset a matrix to the identity matrix.
	 * <p>
	 * The matrix is set as follows:
	 * <p>
	 * 1.0f 0.0f 0.0f 0.0f
	 * <p>
	 * 0.0f 1.0f 0.0f 0.0f
	 * <p>
	 * 0.0f 0.0f 1.0f 0.0f
	 * <p>
	 * 0.0f 0.0f 0.0f 1.0f
	 */
	public void setIdentity() {
		// Set diagonal
		m00 = m11 = m22 = m33 = 1.0f;

		// Zero the rest of the matrix
		m10 = m20 = m30 = m01 = m21 = m31 = m02 = m12 = m32 = m03 = m13 = m23 = 0.0f;
	}

	/**
	 * Set the details of this FloatMatrix4 from an array of 16 floats.
	 * <p>
	 * The matrix is set a column at a time, so the first four floats from the
	 * source array are set on m0 to m03, the next four on m10 to m13 and so on.
	 * <p>
	 * If the source array is not an array of precisely 16 floats then a
	 * IllegalArgumentException is thrown.
	 * 
	 * @param source The array of 16 floats used as the source values to set on this
	 *               FloatMatrix4.
	 */
	public void setFromArray(float[] source) {
		/// If we have an array of precisely 16 floats then proceed with setting
		if (source.length == 16) {
			// First column (x-axis)
			m00 = source[0];
			m01 = source[1];
			m02 = source[2];
			m03 = source[3];

			// Second column (y-axis)
			m10 = source[4];
			m11 = source[5];
			m12 = source[6];
			m13 = source[7];

			// Third column (z-axis)
			m20 = source[8];
			m21 = source[9];
			m22 = source[10];
			m23 = source[11];

			// Fourth column (origin)
			m30 = source[12];
			m31 = source[13];
			m32 = source[14];
			m33 = source[15];
		} else // Bad array size? Throw an exception.
		{
			throw new IllegalArgumentException("Source array must contain precisely 16 floats.");
		}
	}

	/**
	 * Return the x-basis of this FloatMatrix4 as a FloatVector3.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02
	 * properties.
	 * 
	 * @return A FloatVector3 containing the x-basis of this FloatMatrix4.
	 */
	public FloatVector3 getXBasis() {
		return new FloatVector3(m00, m10, m20);
	}

	/**
	 * Return the x-basis of this FloatMatrix4 as an array of three floats.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02
	 * properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not
	 * want to use the {@link FloatVector3} class.
	 * 
	 * @return An array of three floats containing the x-basis of this FloatMatrix4.
	 */
	public float[] getXBasisArray() {
		return new float[] { m00, m10, m20 };
	}

	/**
	 * Set the x-basis of this FloatMatrix4 from a provided FloatVector3.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02
	 * properties.
	 * <p>
	 * To ensure the legality of the x-basis, the provided FloatVector3 is
	 * normalised if required before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a
	 * rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis
	 * methods and instead set the matrix properties via other methods which accept
	 * a float array and do not attempt to enforce rotation matrix legality such as
	 * {@link #setFromArray(float[])} and {@link #FloatMatrix4(float[])}.
	 * 
	 * @param v The FloatVector3 holding the x-basis to set.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setXBasis(FloatVector3 v) {
		// If the provided FloatVector3 is not normalised then normalise it
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the x-basis
		m00 = v.x;
		m10 = v.y;
		m20 = v.z;
	}

	/**
	 * Set the x-basis of this FloatMatrix4 from a provided array of three floats.
	 * <p>
	 * The x-basis is the orientation of the x-axis, as held by the m00, m01 and m02
	 * properties.
	 * <p>
	 * To ensure the legality of the x-basis, the provided array is converted into a
	 * FloatVector3 and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a
	 * rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis
	 * methods and instead set the matrix properties via other methods which do not
	 * enforce normalisation such as the {@link #setFromArray(float[])} and
	 * {@link #FloatMatrix4(float[])} methods.
	 * 
	 * @param f The array of three floats to set as the x-basis of this
	 *          FloatMatrix4.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setXBasis(float[] f) {
		// Array is correct size? If not then return without setting any values
		if (f.length != 3) {
			return;
		}

		// Construct a FloatVector3 from the array and normalise it if required
		FloatVector3 v = new FloatVector3(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the x-basis
		m00 = v.x;
		m10 = v.y;
		m20 = v.z;
	}

	/**
	 * Return the y-basis of this FloatMatrix4 as a FloatVector3.
	 * <p>
	 * The y-basis is the orientation of theyx-axis, as held by the m10, m11 and m12
	 * properties.
	 * 
	 * @return A FloatVector3 containing the y-basis of this FloatMatrix4.
	 */
	public FloatVector3 getYBasis() {
		return new FloatVector3(m01, m11, m21);
	}

	/**
	 * Return the y-basis of this FloatMatrix4 as an array of three floats.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12
	 * properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not
	 * want to use the {@link FloatVector3} class.
	 * 
	 * @return An array of three floats containing the y-basis of this FloatMatrix4.
	 */
	public float[] getYBasisArray() {
		return new float[] { m01, m11, m21 };
	}

	/**
	 * Set the y-basis of this FloatMatrix4 from a provided FloatVector3.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12
	 * properties.
	 * <p>
	 * To ensure the legality of the y-basis, the provided FloatVector3 is
	 * normalised if required before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a
	 * rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis
	 * methods and instead set the matrix properties via other methods which do not
	 * enforce normalisation such as the {@link #setFromArray(float[])} and
	 * {@link #FloatMatrix4(float[])} methods.
	 * 
	 * @param v The FloatVector3 holding the y-basis to set.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setYBasis(FloatVector3 v) {
		// If the provided FloatVector3 is not normalised then normalise it
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the y-basis
		m10 = v.x;
		m11 = v.y;
		m12 = v.z;
	}

	/**
	 * Set the y-basis of this FloatMatrix4 from a provided array of three floats.
	 * <p>
	 * The y-basis is the orientation of the y-axis, as held by the m10, m11 and m12
	 * properties.
	 * <p>
	 * To ensure the legality of the y-basis, the provided array is converted into a
	 * FloatVector3 and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a
	 * rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis
	 * methods and instead set the matrix properties via other methods which do not
	 * enforce normalisation such as the {@link #setFromArray(float[])} and
	 * {@link #FloatMatrix4(float[])} methods.
	 * 
	 * @param f The array of three floats to set as the y-basis of this
	 *          FloatMatrix4.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setYBasis(float[] f) {
		// Array is correct size? If not then return without setting any values
		if (f.length != 3) {
			return;
		}

		// Construct a FloatVector3 from the array and normalise it if required
		FloatVector3 v = new FloatVector3(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the y-basis
		m01 = v.x;
		m11 = v.y;
		m21 = v.z;
	}

	/**
	 * Return the z-basis of this FloatMatrix4 as a FloatVector3.
	 * <p>
	 * The z-basis is the orientation of the x-axis, as held by the m20, m21 and m22
	 * properties.
	 * 
	 * @return A FloatVector3 containing the z-basis of this FloatMatrix4.
	 */
	public FloatVector3 getZBasis() {
		return new FloatVector3(m02, m12, m22);
	}

	/**
	 * Return the z-basis of this FloatMatrix4 as an array of three floats.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22
	 * properties.
	 * <p>
	 * This method is provided to allow for interoperability for users who do not
	 * want to use the {@link FloatVector3} class.
	 * 
	 * @return An array of three floats containing the y-basis of this FloatMatrix4.
	 */
	public float[] getZBasisArray() {
		return new float[] { m02, m12, m22 };
	}

	/**
	 * Set the z-basis of this FloatMatrix4 from a provided FloatVector3.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22
	 * properties.
	 * <p>
	 * To ensure the legality of the z-basis, the provided FloatVector3 is
	 * normalised if required before being set. If you wish to use this class for
	 * storing matrices which do not represent a rotation matrix then you should
	 * avoid the setXBasis/setYBasis/setZBasis methods and instead set the matrix
	 * properties via other methods which do not enforce normalisation such as the
	 * {@link #setFromArray(float[])} and {@link #FloatMatrix4(float[])} methods.
	 * 
	 * @param v The FloatVector3 holding the z-basis to set.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setZBasis(FloatVector3 v) {
		// If the provided FloatVector3 is not normalised then normalise it
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the y-basis
		m02 = v.x;
		m12 = v.y;
		m22 = v.z;
	}

	/**
	 * Set the z-basis of this FloatMatrix4 from a provided array of three floats.
	 * <p>
	 * The z-basis is the orientation of the z-axis, as held by the m20, m21 and m22
	 * properties.
	 * <p>
	 * To ensure the legality of the z-basis, the provided array is converted into a
	 * FloatVector3 and normalised (if required) before being set.
	 * <p>
	 * If you wish to use this class for storing matrices which do not represent a
	 * rotation matrix then you should avoid the setXBasis/setYBasis/setZBasis
	 * methods and instead set the matrix properties via other methods which do not
	 * enforce normalisation such as the {@link #setFromArray(float[])} and
	 * {@link #FloatMatrix4(float[])} methods.
	 * 
	 * @param f The array of three floats to set as the z-basis of this
	 *          FloatMatrix4.
	 * @see #setFromArray(float[])
	 * @see #FloatMatrix4(float[])
	 */
	public void setZBasis(float[] f) {
		// Array is correct size? If not then return without setting any values
		if (f.length != 3) {
			return;
		}

		// Construct a FloatVector3 from the array and normalise it if required
		FloatVector3 v = new FloatVector3(f[0], f[1], f[2]);
		if (!v.lengthIsApproximately(1.0f, FLOAT_EQUALITY_TOLERANCE)) {
			v.normalize();
		}

		// Set the y-basis
		m02 = v.x;
		m12 = v.y;
		m22 = v.z;
	}

	/**
	 * Return the origin of this FloatMatrix4.
	 * 
	 * @return A FloatVector3 of the origin location of this FloatMatrix4, as stored
	 *         in the m30, m31 and m32 properties.
	 */
	public FloatVector3 getOrigin() {
		return new FloatVector3(m03, m13, m23);
	}

	/**
	 * Set the origin of this FloatMatrix4.
	 * <p>
	 * The origin is stored in the matrix properties m30 (x location), m31 (y
	 * location) and m32 (z location).
	 * <p>
	 * As the origin may be at any location, as such it is <em>not</em> normalised
	 * before being set.
	 * 
	 * @param v The origin of this FloatMatrix4 as a FloatVector3.
	 */
	public void setOrigin(FloatVector3 v) {
		// Set the origin
		m03 = v.x;
		m13 = v.y;
		m23 = v.z;
	}

	/**
	 * Return whether or not all three axes of this FloatMatrix4 are orthogonal
	 * (i.e. at 90 degrees to each other).
	 * <p>
	 * Any two axes, such as x/y, x/z or y/z will be orthogonal if their dot product
	 * is zero. However, to account for floating point precision errors, this method
	 * accepts that two axes are orthogonal if their dot product is less than or
	 * equal to 0.01f.
	 * <p>
	 * If you want to find out if any two specific axes are orthogonal, then you can
	 * use code similar to the following:
	 * <p>
	 * {@code boolean xDotYOrthogonal = Math.abs( xAxis.dot(yAxis) ) <= 0.01f;}
	 * 
	 * @return A boolean indicating whether this FloatMatrix4 is orthogonal or not.
	 */
	public boolean isOrthogonal() {
		// Get the x, y and z axes of the matrix as FloatVector3 objects
		FloatVector3 xAxis = new FloatVector3(m00, m10, m20);
		FloatVector3 yAxis = new FloatVector3(m01, m11, m21);
		FloatVector3 zAxis = new FloatVector3(m02, m12, m22);

		// As exact floating point comparisons are a bad idea, we'll accept that a float
		// is
		// approximately equal to zero if it is +/- this epsilon.
		float epsilon = 0.01f;

		// Check whether the x/y, x/z and y/z axes are orthogonal.
		// If two axes are orthogonal then their dot product will be zero (or
		// approximately zero in this case).
		// Note: We could have picked y/x, z/x, z/y but it's the same thing - they're
		// either orthogonal or they're not.
		boolean xDotYOrthogonal = Math.abs(FloatVector3.dotProduct(xAxis, yAxis)) <= epsilon;
		boolean xDotZOrthogonal = Math.abs(FloatVector3.dotProduct(xAxis, zAxis)) <= epsilon;
		boolean yDotZOrthogonal = Math.abs(FloatVector3.dotProduct(yAxis, zAxis)) <= epsilon;

		// All three axes are orthogonal? Return true
		return (xDotYOrthogonal && xDotZOrthogonal && yDotZOrthogonal);
	}

	/**
	 * Transpose this FloatMatrix4.
	 * <p>
	 * <strong>This</strong> FloatMatrix4 is transposed, not a copy.
	 * <p>
	 * If you want to return a transposed version of a FloatMatrix4 <em>without
	 * modifying</em> the source then use the {@link #transposed()} method instead.
	 * 
	 * @return Return this FloatMatrix4 for chaining.
	 * @see #transposed()
	 */
	public FloatMatrix4 transpose() {
		// Take a copy of this matrix as a series of floats.
		// Note: We do not need the m00, m11, m22, or m33 values as they do not change
		// places.

		float m01copy = m01;
		float m02copy = m02;
		float m03copy = m03;

		float m10copy = m10;
		float m12copy = m12;
		float m13copy = m13;

		float m20copy = m20;
		float m21copy = m21;
		float m23copy = m23;

		float m30copy = m30;
		float m31copy = m31;
		float m32copy = m32;

		// Assign the values back to this matrix in transposed order

		this.m01 = m10copy;
		this.m02 = m20copy;
		this.m03 = m30copy;

		this.m10 = m01copy;
		this.m12 = m21copy;
		this.m13 = m31copy;

		this.m20 = m02copy;
		this.m21 = m12copy;
		this.m23 = m32copy;

		this.m30 = m03copy;
		this.m31 = m13copy;
		this.m32 = m23copy;

		// Return this for chaining
		return this;
	}

	/**
	 * Return a transposed version of this FloatMatrix4.
	 * <p>
	 * This FloatMatrix4 is <strong>not</strong> modified during the transposing
	 * process.
	 * <p>
	 * If you want to transpose 'this' FloatMatrix4 rather than return a transposed
	 * copy then use the {@link #transpose()} method instead.
	 * 
	 * @return A transposed version of this FloatMatrix4.
	 */
	public FloatMatrix4 transposed() {
		// Create a new FloatMatrix4 to hold the transposed version of this matrix
		FloatMatrix4 result = new FloatMatrix4();

		// Assign the values of this matrix to the result matrix in transposed order.
		// Note: We don't really need to specify 'this.mXX' (the 'this' is implied) but
		// there's no harm in doing so, and it helps to clarify what's going on.
		result.m00 = this.m00;
		result.m01 = this.m10;
		result.m02 = this.m20;
		result.m03 = this.m30;

		result.m10 = this.m01;
		result.m11 = this.m11;
		result.m12 = this.m21;
		result.m13 = this.m31;

		result.m20 = this.m02;
		result.m21 = this.m12;
		result.m22 = this.m22;
		result.m23 = this.m32;

		result.m30 = this.m03;
		result.m31 = this.m13;
		result.m32 = this.m23;
		result.m33 = this.m33;

		// Return the transposed matrix
		return result;
	}

	/**
	 * Create a translation matrix from a given translate vector.
	 * 
	 * @param trans The translate vector
	 * @return The created translation matrix.
	 */
	public static FloatMatrix4 createTranslationMatrix(FloatVector3 trans) {
		FloatMatrix4 transMat = new FloatMatrix4(1);

		transMat.m03 = trans.x;
		transMat.m13 = trans.y;
		transMat.m23 = trans.z;

		return transMat;
	}

	/**
	 * Create a transformation matrix from translation vector, rotation axis and
	 * angle in radians
	 * 
	 * @param trans        The translation vector
	 * @param rotationAxis The rotation axis vector
	 * @param angleDegs    The angle
	 * @return The created transformation matrix.
	 */
	public static FloatMatrix4 createTransformationMatrixRads(FloatVector3 trans, FloatVector3 rotationAxis,
			float angleRads) {
		return FloatMatrix3.createRotationMatrixRads(rotationAxis, angleRads).toFloatMatrix4().translate(trans);
	}

	/**
	 * Create a transformation matrix from translation vector, rotation axis and
	 * angle in degrees
	 * 
	 * @param trans        The translation vector
	 * @param rotationAxis The rotation axis vector
	 * @param angleDegs    The angle
	 * @return The created transformation matrix.
	 */
	public static FloatMatrix4 createTransformationMatrixDegs(FloatVector3 trans, FloatVector3 rotationAxis,
			float angleDegs) {
		return createTransformationMatrixRads(trans, rotationAxis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Method to multiply this FloatMatrix4 by another FloatMatrix4 and return the
	 * result.
	 * <p>
	 * Neither this matrix or the provided matrix argument are modified by the
	 * multiplication process, instead a new FloatMatrix4 containing the resulting
	 * matrix is returned.
	 * <p>
	 * Matrix multiplication is <strong>not commutative</strong> (i.e.
	 * {@code AB != BA} - that is, the result of multiplying matrix A by matrix B is
	 * <em>not</em> the same as multiplying matrix B by matrix A). As such, to
	 * construct a ModelView or ModelViewProjection matrix you must specify them in
	 * that precise order <strong>reversed</strong> (because of the order in which
	 * the calculations take place), for example:
	 * <p>
	 * {@code FloatMatrix4 modelViewMatrix = viewMatrix.mul(modelMatrix); }
	 * <p>
	 * {@code FloatMatrix4 modelViewProjectionMatrix = projectionMatrix.mul(viewMatrix).mul(modelMatrix); }
	 * <p>
	 * Although matrix multiplication is <strong>not</strong> commutative, it
	 * <strong>is</strong> associative (i.e. {@code A(BC) == (AB)C}), so you can
	 * quite happily combine matrices like this:
	 * <p>
	 * {@code FloatMatrix4 modelViewMatrix = viewMatrix.mul(modelMatrix); }
	 * <p>
	 * {@code FloatMatrix4 modelViewProjectionMatrix = projectionMatrix.mul(modelViewMatrix); }
	 * 
	 * @param m The matrix to multiply this matrix by.
	 * @return A FloatMatrix4 which is the result of multiplying this matrix by the
	 *         provided matrix.
	 */
	public FloatMatrix4 mul(FloatMatrix4 m) {
		// Create a new FloatMatrix4 to hold the resulting matrix
		FloatMatrix4 result = new FloatMatrix4();

		// Multiply this matrix by the provided matrix
		// Note: This multiplication cannot be performed 'inline' on the same object,
		// hence the need to
		// calculate the and place them into a new matrix which we return.
		result.m00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30;
		result.m01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31;
		result.m02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32;
		result.m03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33;

		result.m10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30;
		result.m11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31;
		result.m12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32;
		result.m13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33;

		result.m20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30;
		result.m21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31;
		result.m22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32;
		result.m23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33;

		result.m30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30;
		result.m31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31;
		result.m32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32;
		result.m33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33;

		return result;
	}

	/**
	 * Transform a point in 3D space.
	 * <p>
	 * This method multiplies the provided FloatVector3 by this matrix. This is
	 * commonly used to transform vertices between coordinate spaces, for example
	 * you might multiply a vertex location by a model matrix to transform the
	 * vertex from model space into world space.
	 * <p>
	 * The difference between this method and the {@link #transformDirection} method
	 * is that this method takes the origin of this FloatMatrix4 into account when
	 * performing the transformation, whilst transformDirection does not.
	 * 
	 * @param v The FloatVector3 location to transform.
	 * @return The transformed FloatVector3 location.
	 */
	public FloatVector3 transformPoint(FloatVector3 v) {
		// Create a new FloatVector3 with all properties set to zero
		FloatVector3 result = new FloatVector3();

		// Apply the transformation of the given point using this matrix
		result.x = this.m00 * v.x + this.m01 * v.y + this.m02 * v.z + this.m03; // * 1.0f; - no need as w is 1.0 for a
																				// location
		result.y = this.m10 * v.x + this.m11 * v.y + this.m12 * v.z + this.m13; // * 1.0f; - no need as w is 1.0 for a
																				// location
		result.z = this.m20 * v.x + this.m21 * v.y + this.m22 * v.z + this.m23; // * 1.0f; - no need as w is 1.0 for a
																				// location

		return result;
	}

	/**
	 * Transform a direction in 3D space taking into account the orientation of this
	 * FloatMatrix4s x/y/z axes.
	 * <p>
	 * The difference between this method and the {@link #transformPoint} method is
	 * that this method does not take the origin of this FloatMatrix4 into account
	 * when performing the transformation, whilst transformPoint does.
	 * 
	 * @param v The FloatVector3 to transform.
	 * @return The transformed FloatVector3.
	 */
	public FloatVector3 transformDirection(FloatVector3 v) {
		FloatVector3 result = new FloatVector3(0.0f);

		// NO!
		result.x = this.m00 * v.x + this.m01 * v.y + this.m02 * v.z; // + this.m30 * 0.0; - no need as w is 0.0 for a
																		// direction
		result.y = this.m10 * v.x + this.m11 * v.y + this.m12 * v.z; // + this.m31 * 0.0; - no need as w is 0.0 for a
																		// direction
		result.z = this.m20 * v.x + this.m21 * v.y + this.m22 * v.z; // + this.m32 * 0.0; - no need as w is 0.0 for a
																		// direction

		return result;
	}

	/**
	 * Calculate and return the inverse of a FloatMatrix4.
	 * <p>
	 * Only matrices which do not have a {@link #determinant()} of zero can be
	 * inverted. If the determinant of the provided matrix is zero then an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param m The matrix to invert.
	 * @return The inverted matrix.
	 */
	public static FloatMatrix4 inverse(FloatMatrix4 m) {
		FloatMatrix4 temp = new FloatMatrix4();

		temp.m00 = m.m12 * m.m23 * m.m31 - m.m13 * m.m22 * m.m31 + m.m13 * m.m21 * m.m32 - m.m11 * m.m23 * m.m32
				- m.m12 * m.m21 * m.m33 + m.m11 * m.m22 * m.m33;
		temp.m01 = m.m13 * m.m22 * m.m30 - m.m12 * m.m23 * m.m30 - m.m13 * m.m20 * m.m32 + m.m10 * m.m23 * m.m32
				+ m.m12 * m.m20 * m.m33 - m.m10 * m.m22 * m.m33;
		temp.m02 = m.m11 * m.m23 * m.m30 - m.m13 * m.m21 * m.m30 + m.m13 * m.m20 * m.m31 - m.m10 * m.m23 * m.m31
				- m.m11 * m.m20 * m.m33 + m.m10 * m.m21 * m.m33;
		temp.m03 = m.m12 * m.m21 * m.m30 - m.m11 * m.m22 * m.m30 - m.m12 * m.m20 * m.m31 + m.m10 * m.m22 * m.m31
				+ m.m11 * m.m20 * m.m32 - m.m10 * m.m21 * m.m32;

		temp.m10 = m.m03 * m.m22 * m.m31 - m.m02 * m.m23 * m.m31 - m.m03 * m.m21 * m.m32 + m.m01 * m.m23 * m.m32
				+ m.m02 * m.m21 * m.m33 - m.m01 * m.m22 * m.m33;
		temp.m11 = m.m02 * m.m23 * m.m30 - m.m03 * m.m22 * m.m30 + m.m03 * m.m20 * m.m32 - m.m00 * m.m23 * m.m32
				- m.m02 * m.m20 * m.m33 + m.m00 * m.m22 * m.m33;
		temp.m12 = m.m03 * m.m21 * m.m30 - m.m01 * m.m23 * m.m30 - m.m03 * m.m20 * m.m31 + m.m00 * m.m23 * m.m31
				+ m.m01 * m.m20 * m.m33 - m.m00 * m.m21 * m.m33;
		temp.m13 = m.m01 * m.m22 * m.m30 - m.m02 * m.m21 * m.m30 + m.m02 * m.m20 * m.m31 - m.m00 * m.m22 * m.m31
				- m.m01 * m.m20 * m.m32 + m.m00 * m.m21 * m.m32;

		temp.m20 = m.m02 * m.m13 * m.m31 - m.m03 * m.m12 * m.m31 + m.m03 * m.m11 * m.m32 - m.m01 * m.m13 * m.m32
				- m.m02 * m.m11 * m.m33 + m.m01 * m.m12 * m.m33;
		temp.m21 = m.m03 * m.m12 * m.m30 - m.m02 * m.m13 * m.m30 - m.m03 * m.m10 * m.m32 + m.m00 * m.m13 * m.m32
				+ m.m02 * m.m10 * m.m33 - m.m00 * m.m12 * m.m33;
		temp.m22 = m.m01 * m.m13 * m.m30 - m.m03 * m.m11 * m.m30 + m.m03 * m.m10 * m.m31 - m.m00 * m.m13 * m.m31
				- m.m01 * m.m10 * m.m33 + m.m00 * m.m11 * m.m33;
		temp.m23 = m.m02 * m.m11 * m.m30 - m.m01 * m.m12 * m.m30 - m.m02 * m.m10 * m.m31 + m.m00 * m.m12 * m.m31
				+ m.m01 * m.m10 * m.m32 - m.m00 * m.m11 * m.m32;

		temp.m30 = m.m03 * m.m12 * m.m21 - m.m02 * m.m13 * m.m21 - m.m03 * m.m11 * m.m22 + m.m01 * m.m13 * m.m22
				+ m.m02 * m.m11 * m.m23 - m.m01 * m.m12 * m.m23;
		temp.m31 = m.m02 * m.m13 * m.m20 - m.m03 * m.m12 * m.m20 + m.m03 * m.m10 * m.m22 - m.m00 * m.m13 * m.m22
				- m.m02 * m.m10 * m.m23 + m.m00 * m.m12 * m.m23;
		temp.m32 = m.m03 * m.m11 * m.m20 - m.m01 * m.m13 * m.m20 - m.m03 * m.m10 * m.m21 + m.m00 * m.m13 * m.m21
				+ m.m01 * m.m10 * m.m23 - m.m00 * m.m11 * m.m23;
		temp.m33 = m.m01 * m.m12 * m.m20 - m.m02 * m.m11 * m.m20 + m.m02 * m.m10 * m.m21 - m.m00 * m.m12 * m.m21
				- m.m01 * m.m10 * m.m22 + m.m00 * m.m11 * m.m22;

		// Get the determinant of this matrix
		float determinant = temp.determinant();

		// Each property of the inverse matrix is multiplied by 1.0f divided by the
		// determinant.
		// As we cannot divide by zero, we will throw an IllegalArgumentException if the
		// determinant is zero.
		if (Float.compare(determinant, 0.0f) == 0) {
			throw new IllegalArgumentException("Cannot invert a matrix with a determinant of zero.");
		}

		// Otherwise, calculate the value of one over the determinant and scale the
		// matrix by that value
		float oneOverDeterminant = 1.0f / temp.determinant();

		temp.m00 *= oneOverDeterminant;
		temp.m01 *= oneOverDeterminant;
		temp.m02 *= oneOverDeterminant;
		temp.m03 *= oneOverDeterminant;

		temp.m10 *= oneOverDeterminant;
		temp.m11 *= oneOverDeterminant;
		temp.m12 *= oneOverDeterminant;
		temp.m13 *= oneOverDeterminant;

		temp.m20 *= oneOverDeterminant;
		temp.m21 *= oneOverDeterminant;
		temp.m22 *= oneOverDeterminant;
		temp.m23 *= oneOverDeterminant;

		temp.m30 *= oneOverDeterminant;
		temp.m31 *= oneOverDeterminant;
		temp.m32 *= oneOverDeterminant;
		temp.m33 *= oneOverDeterminant;

		// Finally, return the inverted matrix
		return temp;
	}

	/**
	 * Calculate the determinant of this matrix.
	 * 
	 * @return The determinant of this matrix.
	 */
	public float determinant() {
		return m30 * m21 * m12 * m03 - m20 * m31 * m21 * m03 - m30 * m11 * m22 * m03 + m10 * m31 * m22 * m03
				+ m20 * m11 * m32 * m03 - m10 * m21 * m32 * m03 - m30 * m21 * m02 * m13 + m20 * m31 * m02 * m13
				+ m30 * m01 * m22 * m13 - m00 * m31 * m22 * m13 - m20 * m01 * m32 * m13 + m00 * m21 * m32 * m13
				+ m30 * m11 * m02 * m23 - m10 * m31 * m02 * m23 - m30 * m01 * m12 * m23 + m00 * m31 * m12 * m23
				+ m10 * m01 * m32 * m23 - m00 * m11 * m32 * m23 - m20 * m11 * m02 * m33 + m10 * m21 * m02 * m33
				+ m20 * m01 * m12 * m33 - m00 * m21 * m12 * m33 - m10 * m01 * m22 * m33 + m00 * m11 * m22 * m33;
	}

	/**
	 * Translate this matrix by a provided FloatVector3.
	 * <p>
	 * The changes made are to <strong>this</strong> FloatMatrix4, in the coordinate
	 * space of this matrix.
	 * 
	 * @param v The vector to translate this matrix by.
	 * @return This FloatMatrix4 for chaining.
	 */
	public FloatMatrix4 translate(FloatVector3 v) {
		// Unlike in rotation, the translation procedure can be applied 'inline'
		// so we are able to/ work directly on this FloatMatrix4, rather than a copy.
		this.m03 += this.m00 * v.x + this.m01 * v.y + this.m02 * v.z;
		this.m13 += this.m10 * v.x + this.m11 * v.y + this.m12 * v.z;
		this.m23 += this.m20 * v.x + this.m21 * v.y + this.m22 * v.z;
		this.m33 += this.m30 * v.x + this.m31 * v.y + this.m32 * v.z;

		// Return this for chaining
		return this;
	}

	/**
	 * Translate this matrix by separate x, y and z amounts specified as floats.
	 * <p>
	 * The changes made are to <strong>this</strong> FloatMatrix4.
	 * 
	 * @param x The amount to translate on the X-axis.
	 * @param y The amount to translate on the Y-axis.
	 * @param z The amount to translate on the Z-axis.
	 * @return This translated FloatMatrix4 for chaining.
	 */
	public FloatMatrix4 translate(float x, float y, float z) {
		return this.translate(new FloatVector3(x, y, z));
	}

	/*
	 * public static FloatMatrix4 rotateAroundArbitraryAxisRads(float angleRads,
	 * FloatVector3 axis) { FloatMatrix4 result = new FloatMatrix4();
	 * 
	 * float sinTheta = (float)Math.sin(angleRads); float cosTheta =
	 * (float)Math.cos(angleRads); float oneMinusCosTheta = 1.0f - cosTheta;
	 * 
	 * result.m00 = m00 * m00 * oneMinusCosTheta + cosTheta; result.m01 = }
	 */

	/**
	 * Rotate this matrix about a local axis by an angle specified in radians.
	 * <p>
	 * By a 'local' axis we mean that for example, if you rotated this matrix about
	 * the positive X-axis (1,0,0), then rotation would occur around the positive
	 * X-axis of <strong>this matrix</strong>, not the <em>global / world-space</em>
	 * X-axis.
	 * 
	 * @param rotationAxis The local axis around which to rotate the matrix.
	 * @param angleRads    The angle to rotate the matrix in radians.
	 * @return This rotated matrix
	 */
	// Method to rotate a matrix around an arbitrary axis
	public FloatMatrix4 rotateAboutLocalAxisRads(FloatVector3 rotationAxis, float angleRads) {
		FloatMatrix4 dest = this.toFloatMatrix3().mul(FloatMatrix3.createRotationMatrixRads(rotationAxis, angleRads))
				.toFloatMatrix4();

		dest.m03 = m03;
		dest.m13 = m13;
		dest.m23 = m23;
		dest.m33 = m33;

		// Update this matrix to be the rotated matrix
		this.m00 = dest.m00;
		this.m01 = dest.m01;
		this.m02 = dest.m02;

		this.m10 = dest.m10;
		this.m11 = dest.m11;
		this.m12 = dest.m12;

		this.m20 = dest.m20;
		this.m21 = dest.m21;
		this.m22 = dest.m22;

		this.m30 = dest.m30;
		this.m31 = dest.m31;
		this.m32 = dest.m32;

		return this;
	}

	/**
	 * Rotate this matrix about a local axis by an angle specified in degrees.
	 * <p>
	 * By a 'local' axis we mean that for example, if you rotated this matrix about
	 * the positive X-axis (1,0,0), then rotation would occur around the positive
	 * X-axis of <strong>this matrix</strong>, not the <em>global / world-space</em>
	 * X-axis.
	 * 
	 * @param rotationAxis The local axis around which to rotate the matrix.
	 * @param angleDegs    The angle to rotate the matrix in degrees.
	 * @return This rotated matrix
	 */
	public FloatMatrix4 rotateAboutLocalAxisDegs(FloatVector3 rotationAxis, float angleDegs) {
		return rotateAboutLocalAxisRads(rotationAxis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Rotate this matrix about a world-space axis by an angle specified in radians.
	 * <p>
	 * The cardinal 'world-space' axes are defined so that the +X axis runs to the
	 * right, the +Y axis runs upwards, and the +Z axis runs directly outwards from
	 * the screen.
	 *
	 * @param angleRads The angle to rotate the matrix in radians.
	 * @param worldAxis The world-space axis around which to rotate the matrix.
	 * @return This rotated matrix.
	 */
	public FloatMatrix4 rotateAboutWorldAxisRads(float angleRads, FloatVector3 worldAxis) {
		// Extract the x/y/z axes from this matrix
		FloatVector3 xAxis = new FloatVector3(m00, m10, m20);
		FloatVector3 yAxis = new FloatVector3(m01, m11, m21);
		FloatVector3 zAxis = new FloatVector3(m02, m12, m22);

		// Rotate them around the global axis
		FloatVector3 rotatedXAxis = FloatVector3.rotateAboutAxisRads(xAxis, angleRads, worldAxis);
		FloatVector3 rotatedYAxis = FloatVector3.rotateAboutAxisRads(yAxis, angleRads, worldAxis);
		FloatVector3 rotatedZAxis = FloatVector3.rotateAboutAxisRads(zAxis, angleRads, worldAxis);

		// Assign the rotated axes back to the this matrix

		// Set rotated X-axis
		this.m00 = rotatedXAxis.x;
		this.m10 = rotatedXAxis.y;
		this.m20 = rotatedXAxis.z;

		// Set rotated Y-axis
		this.m01 = rotatedYAxis.x;
		this.m11 = rotatedYAxis.y;
		this.m21 = rotatedYAxis.z;

		// Set rotated Z-axis
		this.m02 = rotatedZAxis.x;
		this.m12 = rotatedZAxis.y;
		this.m22 = rotatedZAxis.z;

		// Return the rotated matrix
		return this;
	}

	/**
	 * Rotate a matrix about a given global axis by an angle in radians.
	 * <p>
	 * The matrix provided is NOT modified - a rotated version is returned.
	 * <p>
	 * The axis specified to rotate the matrix about is <strong>not</strong>
	 * specified in the coordinate space of the matrix being rotated - it is
	 * specified in global coordinates, such as used in OpenGL world space. In this
	 * coordinate system:
	 * <ul>
	 * <li>The positive X-axis runs to the right (1,0,0),
	 * <li>The positive Y-axis runs vertically upwards (0,1,0), and</li>
	 * <li>The positive Z-axis runs outwards from the screen (0,0,1).</li>
	 * </ul>
	 * 
	 * @param matrix    The matrix to rotate.
	 * @param angleRads The angle to rotate the matrix in radians.
	 * @param worldAxis The world-space axis to rotate around.
	 * @return The rotated matrix.
	 */
	public static FloatMatrix4 rotateAboutWorldAxisRads(FloatMatrix4 matrix, float angleRads, FloatVector3 worldAxis) {
		// Extract the x/y/z axes from the matrix
		FloatVector3 xAxis = new FloatVector3(matrix.m00, matrix.m10, matrix.m20);
		FloatVector3 yAxis = new FloatVector3(matrix.m01, matrix.m11, matrix.m21);
		FloatVector3 zAxis = new FloatVector3(matrix.m02, matrix.m12, matrix.m22);

		// System.out.println("In rotMat, xAxis is: " + xAxis);

		// Rotate them around the global axis
		FloatVector3 rotatedXAxis = FloatVector3.rotateAboutAxisRads(xAxis, angleRads, worldAxis);
		FloatVector3 rotatedYAxis = FloatVector3.rotateAboutAxisRads(yAxis, angleRads, worldAxis);
		FloatVector3 rotatedZAxis = FloatVector3.rotateAboutAxisRads(zAxis, angleRads, worldAxis);

		// System.out.println("In rotMat, rotated xAxis is: " + rotatedXAxis);

		// Assign them back to the result matrix
		FloatMatrix4 result = new FloatMatrix4();

		// Set rotated X-axis
		result.m00 = rotatedXAxis.x;
		result.m10 = rotatedXAxis.y;
		result.m20 = rotatedXAxis.z;
		result.m30 = matrix.m30;

		// Set rotated Y-axis
		result.m01 = rotatedYAxis.x;
		result.m11 = rotatedYAxis.y;
		result.m21 = rotatedYAxis.z;
		result.m31 = matrix.m31;

		// Set rotated Z-axis
		result.m02 = rotatedZAxis.x;
		result.m12 = rotatedZAxis.y;
		result.m22 = rotatedZAxis.z;
		result.m32 = matrix.m32;

		// The origin does not change
		result.m03 = matrix.m03;
		result.m13 = matrix.m13;
		result.m23 = matrix.m23;
		result.m33 = matrix.m33;

		// Return the rotated matrix
		return result;
	}

	/**
	 * Rotate a matrix about a given global axis by an angle in radians.
	 * <p>
	 * The matrix provided is NOT modified - a rotated version is returned.
	 * <p>
	 * The axis specified to rotate the matrix about is <strong>not</strong>
	 * specified in the coordinate space of the matrix being rotated - it is
	 * specified in global coordinates, such as used in OpenGL world space. In this
	 * coordinate system:
	 * <ul>
	 * <li>The positive X-axis runs to the right (1,0,0),
	 * <li>The positive Y-axis runs vertically upwards (0,1,0), and</li>
	 * <li>The positive Z-axis runs outwards from the screen (0,0,1).</li>
	 * </ul>
	 * 
	 * @param matrix    The matrix to rotate.
	 * @param angleDegs The angle to rotate the matrix in degrees.
	 * @param worldAxis The world-space axis to rotate around.
	 * @return A rotated version of the provided matrix.
	 */
	public static FloatMatrix4 rotateAboutWorldAxisDegs(FloatMatrix4 matrix, float angleDegs, FloatVector3 worldAxis) {
		return rotateAboutWorldAxisRads(matrix, angleDegs * Utils.DEGS_TO_RADS, worldAxis);
	}

	/**
	 * Rotate matrix about a relative axis.
	 * <p>
	 * The matrix is rotated about the provided axis in the coordinate system of the
	 * provided matrix.
	 * 
	 * @param matrix    The matrix to rotate.
	 * @param angleRads The angle to rotate the matrix about in radians.
	 * @param localAxis The relative axis to rotate about.
	 * @return A rotated matrix.
	 */
	public static FloatMatrix4 rotateMatrixAboutLocalAxisRads(FloatMatrix4 matrix, float angleRads,
			FloatVector3 localAxis) {
		// Transform the local rotation axis into world space
		FloatVector3 worldSpaceAxis = matrix.transformDirection(localAxis);

		// With that done, we can now use the world-space axis method to perform the
		// matrix rotation
		return FloatMatrix4.rotateAboutWorldAxisRads(matrix, angleRads, worldSpaceAxis);
	}

	/**
	 * Rotate matrix about a relative axis.
	 * <p>
	 * The matrix is rotated about the provided axis in the coordinate system of the
	 * provided matrix.
	 * 
	 * @param matrix    The matrix to rotate.
	 * @param angleDegs The angle to rotate the matrix about in degrees.
	 * @param localAxis The relative axis to rotate about.
	 * @return A rotated matrix.
	 */
	public static FloatMatrix4 rotateMatrixAboutLocalAxisDegs(FloatMatrix4 matrix, float angleDegs,
			FloatVector3 localAxis) {
		return FloatMatrix4.rotateMatrixAboutLocalAxisDegs(matrix, angleDegs * Utils.DEGS_TO_RADS, localAxis);
	}

	/**
	 * Transform this matrix.
	 * 
	 * @param trans
	 * @param rotationAxis
	 * @param angleRads
	 * @return The transformed matrix.
	 */
	public FloatMatrix4 transformRads(FloatVector3 trans, FloatVector3 rotationAxis, float angleRads) {
		FloatMatrix4 dest = FloatMatrix4.createTransformationMatrixRads(trans, rotationAxis, angleRads);
		return this.mul(dest);
	}

	/**
	 * Transform this matrix.
	 * 
	 * @param trans
	 * @param rotationAxis
	 * @param angleDegs
	 * @return The transformed matrix.
	 */
	public FloatMatrix4 transformDegs(FloatVector3 trans, FloatVector3 rotationAxis, float angleDegs) {
		return transformRads(trans, rotationAxis, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Return a FloatMatrix3 version of this FloatMatrix4.
	 * <p>
	 * The x, y and z axes are returned in the FloatMatrix3, while the w component
	 * of each axis and the origin plus it's w component are discarded.
	 * <p>
	 * In effect, we are extracting the orientation of this FloatMatrix4 into a
	 * FloatMatrix3.
	 * 
	 * @return A FloatMatrix3 version of this FloatMatrix4.
	 */
	public FloatMatrix3 toFloatMatrix3() {
		FloatMatrix3 rotationMatrix = new FloatMatrix3();

		rotationMatrix.m00 = this.m00;
		rotationMatrix.m01 = this.m01;
		rotationMatrix.m02 = this.m02;

		rotationMatrix.m10 = this.m10;
		rotationMatrix.m11 = this.m11;
		rotationMatrix.m12 = this.m12;

		rotationMatrix.m20 = this.m20;
		rotationMatrix.m21 = this.m21;
		rotationMatrix.m22 = this.m22;

		return rotationMatrix;
	}

	/**
	 * Return this FloatMatrix4 as an array of 16 floats.
	 * 
	 * @return This FloatMatrix4 as an array of 16 floats.
	 */
	public float[] toArray() {
		return new float[] { m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 };
	}

	/**
	 * Overridden toString method.
	 * <p>
	 * The matrix values are formatted to three decimal places. If you require the
	 * exact unformatted m<em>XY</em> values then they may be accessed directly.
	 * 
	 * @return A concise, human-readable description of a FloatMatrix4.
	 */
	@Override
	public String toString() {
		// Note: '0' means put a 0 there if it's zero, '#' means omit if zero
		DecimalFormat decimalFormat = Utils.DECIMAL_FORMAT;

		StringBuilder sb = new StringBuilder();

		sb.append("(" + decimalFormat.format(m00) + ", " + decimalFormat.format(m01) + ", " + decimalFormat.format(m02)
				+ ", " + decimalFormat.format(m03) + ")" + Utils.NEW_LINE);
		sb.append("(" + decimalFormat.format(m10) + ", " + decimalFormat.format(m11) + ", " + decimalFormat.format(m12)
				+ ", " + decimalFormat.format(m13) + ")" + Utils.NEW_LINE);
		sb.append("(" + decimalFormat.format(m20) + ", " + decimalFormat.format(m21) + ", " + decimalFormat.format(m22)
				+ ", " + decimalFormat.format(m23) + ")" + Utils.NEW_LINE);
		sb.append("(" + decimalFormat.format(m30) + ", " + decimalFormat.format(m31) + ", " + decimalFormat.format(m32)
				+ ", " + decimalFormat.format(m33) + ")" + Utils.NEW_LINE);

		return sb.toString();
	}

	/**
	 * Print this matrix
	 */
	public void print() {
		System.out.println("Float Matrix 4:\n" + toString());
	}
	// ---------- Static methods ----------

	/**
	 * Construct an orthographic projection matrix.
	 * <p>
	 * Orthographic projections are commonly used when working in 2D or CAD
	 * scenarios. As orthographic projection does not perform foreshortening on any
	 * projected geometry, objects are drawn the same size regardless of their
	 * distance from the camera.
	 * <p>
	 * By specifying the bottom clipping plane to be 0.0f and the top clipping plane
	 * to be the height of the window, the origin of the coordinate space is at the
	 * bottom-left of the window and the positive y-axis runs upwards. To place the
	 * origin at the top left of the window and have the y-axis run downwards,
	 * simply swap the top and bottom values.
	 * <p>
	 * Once you have an orthographic projection matrix, if you are not using any
	 * separate Model or View matrices then you may simply use the orthographic
	 * matrix as a ModelViewProjection matrix.
	 * <p>
	 * If values are passed so that (right - left), (top - bottom) or (far - near)
	 * are zero then an IllegalArgumentException is thrown.
	 * 
	 * @param left   The left clipping plane, typically 0.0f.
	 * @param right  The right clipping plane, typically the width of the window.
	 * @param top    The top clipping plane, typically the height of the window.
	 * @param bottom The bottom clipping plane, typically 0.0f.
	 * @param near   The near clipping plane, typically -1.0f.
	 * @param far    The far clipping plane, typically 1.0f.
	 * @return The constructed orthographic matrix
	 * @see <a href=
	 *      "http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho">http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho</a>
	 */
	public static FloatMatrix4 createOrthographicProjectionMatrix(float left, float right, float top, float bottom,
			float near, float far) {
		// Perform sanity checking to avoid divide by zero errors
		if (Float.compare(right - left, 0.0f) == 0) {
			throw new IllegalArgumentException("(right - left) cannot be zero.");
		}
		if (Float.compare(top - bottom, 0.0f) == 0) {
			throw new IllegalArgumentException("(top - bottom) cannot be zero.");
		}
		if (Float.compare(far - near, 0.0f) == 0) {
			throw new IllegalArgumentException("(far - near) cannot be zero.");
		}

		// Got legal arguments? Construct the orthographic matrix
		FloatMatrix4 m = new FloatMatrix4();

		m.m00 = 2.0f / (right - left);
		m.m10 = 0.0f;
		m.m20 = 0.0f;
		m.m30 = 0.0f;

		m.m01 = 0.0f;
		m.m11 = 2.0f / (top - bottom);
		m.m21 = 0.0f;
		m.m31 = 0.0f;

		m.m02 = 0.0f;
		m.m12 = 0.0f;
		m.m22 = -2.0f / (far - near);
		m.m32 = 0.0f;

		m.m03 = -(right + left) / (right - left);
		m.m13 = -(top + bottom) / (top - bottom);
		m.m23 = -(far + near) / (far - near);
		m.m33 = 1.0f;

		return m;
	}

	/**
	 * Construct a perspective projection matrix.
	 * <p>
	 * The parameters provided are the locations of the
	 * left/right/top/bottom/near/far clipping planes.
	 * <p>
	 * There is rarely any need to specify the bounds of a projection matrix in this
	 * manner, and you are likely to be better served by using the
	 * {@link #createPerspectiveProjectionMatrix(float, float, float, float)} method
	 * instead.
	 * <p>
	 * Once you have a Projection matrix, then it can be combined with a ModelView
	 * or separate Model and View matrices in the following manner (be careful:
	 * multiplication order is important) to create a ModelViewProjection matrix:
	 * <p>
	 * {@code FloatMatrix4 mvpMatrix = projectionMatrix.mul(modelViewMatrix);}
	 * <p>
	 * or
	 * <p>
	 * {@code FloatMatrix4 mvpMatrix = projectionMatrix.mul(viewMatrix).mul(modelMatrix);}
	 * 
	 * @param left   The left clipping plane, typically 0.0f.
	 * @param right  The right clipping plane, typically the width of the window.
	 * @param top    The top clipping plane, typically the height of the window.
	 * @param bottom The bottom clipping plane, typically 0.0f.
	 * @param near   The near clipping plane, typically -1.0f.
	 * @param far    The far clipping plane, typically 1.0f.
	 * @return The constructed orthographic matrix
	 * @see <a href=
	 *      "http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho">http://www.songho.ca/opengl/gl_projectionmatrix.html#ortho</a>
	 */
	public static FloatMatrix4 createPerspectiveProjectionMatrix(float left, float right, float top, float bottom,
			float near, float far) {
		// Instantiate a new matrix, initialised to identity
		FloatMatrix4 p = new FloatMatrix4(1.0f);

		// Set matrix values
		p.m00 = (2.0f * near) / (right - left);

		p.m11 = (2.0f * near) / (top - bottom);

		p.m02 = (right + left) / (right - left);
		p.m12 = (top + bottom) / (top - bottom);
		p.m22 = -(far + near) / (far - near);
		p.m32 = -1.0f;

		p.m23 = (-2.0f * far * near) / (far - near);
		p.m33 = 0.0f;

		return p;
	}

	/***
	 * Construct a perspective projection matrix.
	 * <p>
	 * The vertical and horizontal field of view (FoV) values are related in such a
	 * way that if you know one then you can calculate the other. This method takes
	 * the vertical FoV and allows the horizontal FoV to adapt to it based on the
	 * aspect ratio of the screen in a technique called 'Hor+' (horizontal plus)
	 * scaling.
	 * <p>
	 * If required, the horizontal and vertical FoVs can be calculated via the
	 * following process (note: all angles are specified in
	 * <strong>radians</strong>):
	 * <p>
	 * {@code float horizFoVRads = 2.0f * (float)Math.atan( Math.tan(vertFoVRads  / 2.0f) * aspectRatio);}
	 * <p>
	 * {@code float vertFoVRads  = 2.0f * (float)Math.atan( Math.tan(horizFoVRads / 2.0f) * (1.0f / aspectRatio) ); }
	 * <p>
	 * The aspect ratio can be calculated as:
	 * {@code (float)windowWidth / (float)windowHeight} - if the size of the window
	 * changes then a new projection matrix should be created with the new aspect
	 * ratio of the window.
	 * <p>
	 * {@code zNear} and {@code zFar} represent the near and far clipping distances
	 * outside of which any geometry will be clipped (i.e. not rendered). An
	 * acceptable value for zNear is <strong>1.0f</strong> (0.0f should be avoided),
	 * however, specifying a zNear value of <strong>2.0f</strong> will essentially
	 * double the precision of your depth buffer due to the way in which floating
	 * point values distribute bits between the significand (i.e. the value before
	 * the decimal point) and the exponent (the value to raise or lower the
	 * significand to). Choosing good values for your near and far clipping planes
	 * can often eliminate any z-fighting in your scenes.
	 * <p>
	 * An {@link IllegalArgumentException} is thrown is any of the parameters are
	 * specified with illegal values.
	 * 
	 * @param vertFoVDegs The vertical Field of View angle - must be a positive
	 *                    value between 1.0f and 179.0f. For good choices, see
	 *                    <a href=
	 *                    "http://en.wikipedia.org/wiki/Field_of_view_in_video_games#Choice_of_field_of_view">choice
	 *                    of field of view</a>.
	 * @param aspectRatio The aspect ratio of the window in which drawing will occur
	 *                    - must be a positive value.
	 * @param zNear       The near clipping distance - must be a positive value
	 *                    which is less than zFar.
	 * @param zFar        The far clipping distance - must be a positive value which
	 *                    is greater than zNear.
	 * @return A projection matrix as a FloatMatrix4.
	 * @see <a href=
	 *      "http://en.wikipedia.org/wiki/Field_of_view_in_video_games">http://en.wikipedia.org/wiki/Field_of_view_in_video_games</a>
	 * @see <a href=
	 *      "http://www.songho.ca/opengl/gl_projectionmatrix.html">http://www.songho.ca/opengl/gl_projectionmatrix.html</a>
	 * @see <a href=
	 *      "https://www.opengl.org/archives/resources/faq/technical/depthbuffer.htm">https://www.opengl.org/archives/resources/faq/technical/depthbuffer.htm</a>
	 * @see <a href=
	 *      "http://en.wikipedia.org/wiki/Floating_point">http://en.wikipedia.org/wiki/Floating_point</a>
	 * @see <a href=
	 *      "http://en.wikipedia.org/wiki/Z-fighting">http://en.wikipedia.org/wiki/Z-fighting</a>
	 */
	public static FloatMatrix4 createPerspectiveProjectionMatrix(float vertFoVDegs, float aspectRatio, float zNear,
			float zFar) {
		// Sanity checking
		if (aspectRatio < 0.0f) {
			throw new IllegalArgumentException("Aspect ratio cannot be negative.");
		}
		if (zNear <= 0.0f || zFar <= 0.0f) {
			throw new IllegalArgumentException("The values of zNear and zFar must be positive.");
		}
		if (zNear >= zFar) {
			throw new IllegalArgumentException("zNear must be less than than zFar.");
		}
		if (vertFoVDegs < 1.0f || vertFoVDegs > 179.0f) {
			throw new IllegalArgumentException("Vertical FoV must be within 1 and 179 degrees inclusive.");
		}

		float frustumLength = zFar - zNear;

		// Calculate half the vertical field of view in radians
		float halfVertFoVRads = (vertFoVDegs / 2.0f) * Utils.DEGS_TO_RADS;

		// There is no built in Math.cot() in Java, but co-tangent is simply 1 over
		// tangent
		float cotangent = 1.0f / (float) Math.tan(halfVertFoVRads);

		// Instantiate a new matrix, initialised to identity
		FloatMatrix4 p = new FloatMatrix4(1.0f);

		// Set matrix values and return the constructed projection matrix
		p.m00 = cotangent / aspectRatio;

		p.m11 = cotangent;

		p.m22 = -(zFar + zNear) / frustumLength;
		p.m32 = -1.0f;

		p.m23 = (-2.0f * zNear * zFar) / frustumLength;
		p.m33 = 0.0f;

		return p;
	}

}
