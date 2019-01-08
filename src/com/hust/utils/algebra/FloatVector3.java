package com.hust.utils.algebra;

import com.hust.utils.Utils;

public class FloatVector3 {
	// ----- Static Properties -----
	// Cardinal axes
	public static FloatVector3 ZERO = new FloatVector3(0.0f, 0.0f, 0.0f);
	public static FloatVector3 X_AXIS = new FloatVector3(1.0f, 0.0f, 0.0f);
	public static FloatVector3 Y_AXIS = new FloatVector3(0.0f, 1.0f, 0.0f);
	public static FloatVector3 Z_AXIS = new FloatVector3(0.0f, 0.0f, 1.0f);

	// ----- Properties -----

	public float x, y, z;

	// ----- Methods -----

	/** Default constructor - x, y, and z are initialised to zero. */
	public FloatVector3() {
	}

	/**
	 * Single parameter constructor sets the same value across all three components.
	 *
	 * @param value The value to set on the x, y and z components of this vector.
	 */
	public FloatVector3(float value) {
		x = y = z = value;
	}

	/**
	 * Three parameter constructor.
	 * 
	 * @param x The x component value to set.
	 * @param y The y component value to set.
	 * @param z The z component value to set.
	 */
	public FloatVector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Array constructor.
	 * 
	 * @param fs The array to be converted to vector.
	 */
	public FloatVector3(float... fs) {
		try {
			this.x = fs[0];
			this.y = fs[1];
			this.z = fs[2];
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param source The vector used to set component values on this newly created
	 *               vector.
	 */
	public FloatVector3(FloatVector3 source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}

	/**
	 * Return an identical copy of the provided Vec3f.
	 *
	 * @param source The vector to clone.
	 * @return An identical copy of the provided Vec3f.
	 */
	public static FloatVector3 clone(FloatVector3 source) {
		return new FloatVector3(source.x, source.y, source.z);
	}

	/**
	 * Float setter for convenience. Note: x/y/z properties are public.
	 *
	 * @param x The x value to set.
	 * @param y The y value to set.
	 * @param z The z value to set.
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(FloatVector3 source) {
		x = source.x;
		y = source.y;
		z = source.z;
	}

	public float get(int i) {
		switch (i) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		}
		return Float.NaN;
	}

	public boolean approximatelyEquals(FloatVector3 v, float tolerance) {
		float xDiff = Math.abs(this.x - v.x);
		float yDiff = Math.abs(this.y - v.y);
		float zDiff = Math.abs(this.z - v.z);

		// Return true or false
		return (xDiff < tolerance && yDiff < tolerance && zDiff < tolerance);
	}

	/**
	 * Return whether the two provided vectors are perpendicular (to a dot-product
	 * tolerance of 0.01f).
	 *
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return Whether the two provided vectors are perpendicular (true) or not
	 *         (false).
	 */
	public static boolean perpendicular(FloatVector3 a, FloatVector3 b) {
		return Utils.approximatelyEquals(FloatVector3.dotProduct(a, b), 0.0f, 0.01f) ? true : false;
	}

	/**
	 * Return whether the length of this Vec3f is approximately equal to a given
	 * value to within a given tolerance.
	 * 
	 * @param value     The value to compare the length of this vector to.
	 * @param tolerance The tolerance within which the values must be to return
	 *                  true.
	 * @return A boolean indicating whether the length of this vector is
	 *         approximately the same as that of the provided value.
	 */
	public boolean lengthIsApproximately(float value, float tolerance) {

		if (Math.abs(this.length() - value) < tolerance) {
			return true;
		}

		return false;
	}

	/** Set all components of this vector to 0.0f */
	public void zero() {
		x = y = z = 0.0f;
	}

	/**
	 * Negate and return this vector.
	 * <p>
	 * Note: It is actually <em>this</em> vector which is negated and returned, not
	 * a copy / clone.
	 * 
	 * @return This vector negated.
	 */
	public FloatVector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public FloatVector3 negated() {
		return new FloatVector3(-x, -y, -z);
	}

	/**
	 * Return whether two vectors are approximately equal to within a given
	 * tolerance.
	 *
	 * @param a         The first vector.
	 * @param b         The second vector.
	 * @param tolerance The value which each component of each vector must be within
	 *                  to be considered approximately equal.
	 * @return Whether the two provided vector arguments are approximately equal
	 *         (true) or not (false).
	 */
	public static boolean approximatelyEqual(FloatVector3 a, FloatVector3 b, float tolerance) {
		if ((Math.abs(a.x - b.x) < tolerance) && (Math.abs(a.y - b.y) < tolerance)
				&& (Math.abs(a.z - b.z) < tolerance)) {
			return true;
		}

		return false;
	}

	public FloatVector3 normalize() {
		// Calculate the magnitude of our vector
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

		// As long as the magnitude is greater then zero, divide each element by the
		// magnitude to get the normalised value between -1 and +1.
		// Note: If the vector has a magnitude of zero we simply return it - we
		// could instead throw a RuntimeException here... but it's better to continue.
		if (magnitude > 0.0f) {
			x /= magnitude;
			y /= magnitude;
			z /= magnitude;
		}
		return this;
	}

	/**
	 * Return a normalised version of this vector without modifying 'this' vector.
	 *
	 * @return A normalised version of this vector.
	 */
	public FloatVector3 normalized() {
		return new FloatVector3(this).normalize();
	}

	/**
	 * Return the scalar product of two vectors.
	 * <p>
	 * If the provided vectors are normalised then this will be the same as the dot
	 * product.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The scalar product of the two vectors.
	 */
	public static float scalarProduct(FloatVector3 v1, FloatVector3 v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/**
	 * Return the scalar product of two vectors.
	 * <p>
	 * Normalised versions of the provided vectors are used in the dot product
	 * operation.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The dot product of the two vectors.
	 */
	public static float dotProduct(FloatVector3 v1, FloatVector3 v2) {
		FloatVector3 v1Norm = v1.normalized();
		FloatVector3 v2Norm = v2.normalized();
		float res = v1Norm.x * v2Norm.x + v1Norm.y * v2Norm.y + v1Norm.z * v2Norm.z;
		return res > 1.0f ? 1.0f : res;
	}

	/**
	 * Calculate and return a vector which is the cross product of the two provided
	 * vectors.
	 * <p>
	 * The returned vector is not normalised.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The non-normalised cross-product of the two vectors v1-cross-v2.
	 */
	public static FloatVector3 crossProduct(FloatVector3 v1, FloatVector3 v2) {
		return new FloatVector3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}

	/**
	 * Calculate and return a vector which is the cross product of this vector and
	 * another vector.
	 * <p>
	 * The returned vector is not normalised.
	 * 
	 * @param v The Vec3f with which we will cross product this Vec3f.
	 * @return The non-normalised cross product if the two vectors this-cross-v.
	 */
	public FloatVector3 cross(FloatVector3 v) {
		return new FloatVector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}

	/**
	 * Calculate and return the distance between two points in 3D space.
	 * 
	 * @param v1 The first point.
	 * @param v2 The second point.
	 * @return The distance between the two points.
	 */
	public static float distanceBetween(FloatVector3 v1, FloatVector3 v2) {
		float dx = v2.x - v1.x;
		float dy = v2.y - v1.y;
		float dz = v2.z - v1.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Calculate and return the Manhattan distance between two Vec3f objects.
	 * <p>
	 * The Manhattan distance is an approximate distance between two points, but can
	 * be calculated faster than the exact distance.
	 * <p>
	 * Further reading: http://en.wikipedia.org/wiki/floataxicab_geometry
	 * http://stackoverflow.com/questions/3693514/very-fast-3d-distance-check
	 * 
	 * @param v1 The first point.
	 * @param v2 The second point.
	 * @return The Manhattan distance between the two points.
	 */
	public static float manhattanDistanceBetween(FloatVector3 v1, FloatVector3 v2) {
		return Math.abs(v2.x - v1.x) + Math.abs(v2.x - v1.x) + Math.abs(v2.x - v1.x);
	}

	/**
	 * Return whether two locations are within a given manhattan distance of each
	 * other.
	 * <p>
	 * The manhattan distance is an approximate distance between two points, but can
	 * be calculated faster than the exact distance.
	 * <p>
	 * Further reading: http://en.wikipedia.org/wiki/floataxicab_geometry
	 * http://stackoverflow.com/questions/3693514/very-fast-3d-distance-check
	 * 
	 * @param v1 The first location vector
	 * @param v2 The second location vector
	 * @return boolean
	 */
	boolean withinManhattanDistance(FloatVector3 v1, FloatVector3 v2, float distance) {
		if (Math.abs(v2.x - v1.x) > distance)
			return false; // Too far in x direction
		if (Math.abs(v2.y - v1.y) > distance)
			return false; // Too far in y direction
		if (Math.abs(v2.z - v1.z) > distance)
			return false; // Too far in z direction
		return true;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Return a component-wise absolute version (i.e. all components are positive)
	 * of this vector.
	 * <p>
	 * Note: This vector itself is not modified - a new vector is created, each
	 * component is made positive, and the new vector is returned.
	 *
	 * @param source The vector to make absolute.
	 * @return A component-wise absolute version of this vector.
	 */
	public static FloatVector3 abs(FloatVector3 source) {
		FloatVector3 absVector = new FloatVector3();

		if (source.x < 0.0f) {
			absVector.x = -source.x;
		} else {
			absVector.x = source.x;
		}
		if (source.y < 0.0f) {
			absVector.y = -source.y;
		} else {
			absVector.y = source.y;
		}
		if (source.z < 0.0f) {
			absVector.z = -source.z;
		} else {
			absVector.z = source.z;
		}

		return absVector;
	}

	/**
	 * Return a normalised Vec3f which is perpendicular to the vector provided.
	 * <p>
	 * This is a very fast method of generating a perpendicular vector that works
	 * for any vector which is 5 degrees or more from vertical 'up'.
	 * <p>
	 * The code in this method is adapted from:
	 * http://blog.selfshadow.com/2011/10/17/perp-vectors/
	 *
	 * @param u The vector to use as the basis for generating the perpendicular
	 *          vector.
	 * @return A normalised vector which is perpendicular to the provided vector
	 *         argument.
	 */
	public static FloatVector3 genPerpendicularVectorQuick(FloatVector3 u) {
		FloatVector3 perp;

		if (Math.abs(u.y) < 0.99f) {
			perp = new FloatVector3(-u.z, 0.0f, u.x); // cross(u, UP)
		} else {
			perp = new FloatVector3(0.0f, u.z, -u.y); // cross(u, RIGHT)
		}

		return perp.normalize();
	}

	/**
	 * Method to generate a vector perpendicular to another one using the
	 * Hughes-Muller method.
	 * <p>
	 * The returned vector is normalised.
	 * <p>
	 * The code in this method is adapted from:
	 * http://blog.selfshadow.com/2011/10/17/perp-vectors/
	 * <p>
	 * Further reading: Hughes, J. F., Muller, T., "Building an Orthonormal Basis
	 * from a Unit Vector", Journal of Graphics Tools 4:4 (1999), 33-35.
	 * 
	 * @param u The vector with regard to which we will generate a perpendicular
	 *          unit vector.
	 * @return A normalised vector which is perpendicular to the provided vector
	 *         argument.
	 */
	public static FloatVector3 genPerpendicularVectorHM(FloatVector3 u) {
		// Get the absolute source vector
		FloatVector3 a = FloatVector3.abs(u);
		if (a.x <= a.y && a.x <= a.z) {
			return new FloatVector3(0.0f, -u.z, u.y).normalize();
		} else if (a.y <= a.x && a.y <= a.z) {
			return new FloatVector3(-u.z, 0.0f, u.x).normalize();
		} else {
			return new FloatVector3(-u.y, u.x, 0.0f).normalize();
		}
	}

	// Further reading: Stark, M. M., "Efficient Construction of Perpendicular
	// Vectors without Branching", Journal of Graphics Tools 14:1 (2009), 55-61.
//	Vec3f genPerpendicularVectorStark(Vec3f u)
//	{
//		// Get the absolute source vector
//	    Vec3f a = Vec3f.abs(u);
//
//	    unsigned int = SIGNBIT(a.x - a.y);
//	    uint uzx = SIGNBIT(a.x - a.z);
//	    uint uzy = SIGNBIT(a.y - a.z);
//
//	    uint xm = uyx & uzx;
//	    uint ym = (1^xm) & uzy;
//	    uint zm = 1^(xm & ym);
//
//	    float3 v = cross(u, float3(xm, ym, zm));
//	    return v;
//	}

	/**
	 * Method to generate a vector perpendicular to another one using the Frisvad
	 * method.
	 * <p>
	 * The returned vector is normalised.
	 * 
	 * @param u The vector with regard to which we will generate a perpendicular
	 *          unit vector.
	 * @return A normalised vector which is perpendicular to the provided vector
	 *         argument.
	 */
	public static FloatVector3 genPerpendicularVectorFrisvad(FloatVector3 u) {
		if (u.z < -0.9999999f) // Handle the singularity
		{
			return new FloatVector3(0.0f, -1.0f, 0.0f);
			// b2 = Vec3f(-1.0f, 0.0f, 0.0f);
			// return;
		}

		float a = 1.0f / (1.0f + u.z);
		// float b = -n.x*n.y*a;
		return new FloatVector3(1.0f - u.x * u.x * a, -u.x * u.y * a, -u.x).normalized();
		// b2 = Vec3f(b, 1.0f - n.y*n.y*a, -n.y);
	}

	/**
	 * Return the unit vector between two provided vectors.
	 *
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The unit vector between the two provided vector arguments.
	 */
	public static FloatVector3 getUvBetween(FloatVector3 v1, FloatVector3 v2) {
		return new FloatVector3(v2.sub(v1)).normalize();
	}

	/**
	 * Calculate and return the angle between two vectors in radians.
	 * <p>
	 * The result will always be a positive value between zero and pi (3.14159f)
	 * radians.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the vector in radians.
	 */
	public static float getAngleBetweenRads(FloatVector3 v1, FloatVector3 v2) {
		// Note: a and b are normalised within the dotProduct method.
		return (float) Math.acos(FloatVector3.dotProduct(v1, v2));
	}

	/**
	 * Calculate and return the angle between two vectors in degrees.
	 * <p>
	 * The result will always be a positive value between [0..180) degrees.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the vector in degrees.
	 */
	public static float getAngleBetweenDegs(FloatVector3 v1, FloatVector3 v2) {
		return FloatVector3.getAngleBetweenRads(v1, v2) * Utils.RADS_TO_DEGS;
	}

	/**
	 * Calculate and return the angle between two lines in radians.
	 * <p>
	 * The result will always be a positive value between zero and pi (3.14159f)
	 * radians.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the lines in radians.
	 */
	public static float getLinearAngleBetweenRads(FloatVector3 v1, FloatVector3 v2) {
		return (float) Math.acos(dotProduct(v1, v2) / (v1.length() * v2.length()));
	}

	/**
	 * Calculate and return the angle between two lines in degrees.
	 * <p>
	 * The result will always be a positive value between [0..180) degrees.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the lines in degrees.
	 */
	public static float getLinearAngleBetweenDegs(FloatVector3 v1, FloatVector3 v2) {
		return getLinearAngleBetweenRads(v1, v2) * Utils.RADS_TO_DEGS;
	}

	/**
	 * Calculate and return the angle between a line and a plane in radians.
	 * <p>
	 * The result will always be a positive value between zero and pi (3.14159f)
	 * radians.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the line and the plane in radians.
	 */
	public static float getPlanarAngleBetweenRads(FloatVector3 v1, FloatVector3 v2) {
		return (float) Math.asin(Math.abs(dotProduct(v1, v2)) / (v1.length() * v2.length()));
	}

	/**
	 * Calculate and return the angle between a line and a plane in degrees.
	 * <p>
	 * The result will always be a positive value between [0..180) degrees.
	 * <p>
	 * This method does not modify the provided vectors, but does use normalised
	 * versions of them in the calculations.
	 * 
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The angle between the line and the plane in degrees.
	 */
	public static float getPlanarAngleBetweenDegs(FloatVector3 v1, FloatVector3 v2) {
		return getPlanarAngleBetweenRads(v1, v2) * Utils.RADS_TO_DEGS;
	}

	/**
	 * Return a signed angle between two vectors within the range -179.9f..180.0f
	 * degrees.
	 *
	 * @param referenceVector The baseline vector which we consider to be at zero
	 *                        degrees.
	 * @param otherVector     The vector we will use to calculate the signed angle
	 *                        with respect to the reference vector.
	 * @param normalVector    The normal vector (i.e. vector perpendicular to) both
	 *                        the reference and 'other' vectors.
	 * @return The signed angle from the reference vector to the other vector in
	 *         degrees.
	 **/
	public static float getSignedAngleBetweenDegs(FloatVector3 referenceVector, FloatVector3 otherVector,
			FloatVector3 normalVector) {
		float unsignedAngle = FloatVector3.getAngleBetweenDegs(referenceVector, otherVector);
		float sign = Math
				.signum(FloatVector3.dotProduct(FloatVector3.crossProduct(referenceVector, otherVector), normalVector));
		return unsignedAngle * sign;
	}

	/**
	 * Return an angle limited vector with regard to another vector.
	 * <p>
	 * 
	 * @param vecToLimit     The vector which we will limit to a given angle with
	 *                       regard to the the baseline vector.
	 * @param vecBaseline    The vector which will be used as the baseline /
	 *                       frame-of-reference when rotating the vecToLimit.
	 * @param angleLimitDegs The maximum angle which the vecToLimit may be rotated
	 *                       away from the vecBaseline, in degrees.
	 * @return The rotated vecToLimit, which is constraint to a maximum of the
	 *         angleLimitDegs argument.
	 */
	public static FloatVector3 getAngleLimitedUnitVectorDegs(FloatVector3 vecToLimit, FloatVector3 vecBaseline,
			float angleLimitDegs) {
		// Get the angle between the two vectors
		// Note: This will ALWAYS be a positive value between 0 and 180 degrees.
		float angleBetweenVectorsDegs = FloatVector3.getAngleBetweenDegs(vecBaseline, vecToLimit);

		if (angleBetweenVectorsDegs > angleLimitDegs) {
			// The axis which we need to rotate around is the one perpendicular to the two
			// vectors - so we're
			// rotating around the vector which is the cross-product of our two vectors.
			// Note: We do not have to worry about both vectors being the same or pointing
			// in opposite directions
			// because if they bones are the same direction they will not have an angle
			// greater than the angle limit,
			// and if they point opposite directions we will approach but not quite reach
			// the precise max angle
			// limit of 180.0f (I believe).
			FloatVector3 correctionAxis = FloatVector3.crossProduct(vecBaseline.normalized(), vecToLimit.normalized())
					.normalize();

			// Our new vector is the baseline vector rotated by the max allowable angle
			// about the correction axis
			return FloatVector3.rotateAboutAxisDegs(vecBaseline, angleLimitDegs, correctionAxis).normalized();
		} else // Angle not greater than limit? Just return a normalised version of the
				// vecToLimit
		{
			// This may already BE normalised, but we have no way of knowing without calcing
			// the length, so best be safe and normalise.
			// TODO: If performance is an issue, then I could get the length, and if it's
			// not approx. 1.0f THEN normalise otherwise just return as is.
			return vecToLimit.normalized();
		}
	}

	/**
	 * Return the global pitch of this vector about the global X-Axis. The returned
	 * value is within the range -179.9f..180.0f degrees.
	 *
	 * @return The pitch of the vector in degrees.
	 **/
	public float getGlobalPitchDegs() {
		FloatVector3 xProjected = this.projectOntoPlane(X_AXIS);
		float pitch = FloatVector3.getAngleBetweenDegs(Z_AXIS.negated(), xProjected);
		return xProjected.y < 0.0f ? -pitch : pitch;
	}

	/**
	 * Return the global yaw of this vector about the global Y-Axis. The returned
	 * value is within the range -179.9f..180.0f degrees.
	 *
	 * @return The yaw of the vector in degrees.
	 **/
	public float getGlobalYawDegs() {
		FloatVector3 yProjected = this.projectOntoPlane(Y_AXIS);
		float yaw = FloatVector3.getAngleBetweenDegs(Z_AXIS.negated(), yProjected);
		return yProjected.x < 0.0f ? -yaw : yaw;
	}

	/**
	 * Rotate a Vec3f about the world-space X-axis by a given angle specified in
	 * radians.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleRads The angle to rotate the vector in radians.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateXRads(FloatVector3 source, float angleRads) {
		// Rotation about the x-axis:
		// x' = x
		// y' = y*cos q - z*sin q
		// z' = y*sin q + z*cos q

		float cosTheta = (float) Math.cos(angleRads);
		float sinTheta = (float) Math.sin(angleRads);

		return new FloatVector3(source.x, source.y * cosTheta - source.z * sinTheta,
				source.y * sinTheta + source.z * cosTheta);
	}

	/**
	 * Rotate a Vec3f about the world-space X-axis by a given angle specified in
	 * degrees.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleDegs The angle to rotate the vector in degrees.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateXDegs(FloatVector3 source, float angleDegs) {
		return FloatVector3.rotateXRads(source, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Rotate a Vec3f about the world-space Y-axis by a given angle specified in
	 * radians.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleRads The angle to rotate the vector in radians.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateYRads(FloatVector3 source, float angleRads) {
		// Rotation about the y axis:
		// x' = z*sin q + x*cos q
		// y' = y
		// z' = z*cos q - x*sin q

		float cosTheta = (float) Math.cos(angleRads);
		float sinTheta = (float) Math.sin(angleRads);

		return new FloatVector3(source.z * sinTheta + source.x * cosTheta, source.y,
				source.z * cosTheta - source.x * sinTheta);
	}

	/**
	 * Rotate a Vec3f about the world-space Y-axis by a given angle specified in
	 * degrees.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleDegs The angle to rotate the vector in degrees.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateYDegs(FloatVector3 source, float angleDegs) {
		return FloatVector3.rotateYRads(source, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Rotate a Vec3f about the world-space Z-axis by a given angle specified in
	 * radians.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleRads The angle to rotate the vector in radians.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateZRads(FloatVector3 source, float angleRads) {
		// Rotation about the z-axis:
		// x' = x*cos q - y*sin q
		// y' = x*sin q + y*cos q
		// z' = z

		float cosTheta = (float) Math.cos(angleRads);
		float sinTheta = (float) Math.sin(angleRads);

		return new FloatVector3(source.x * cosTheta - source.y * sinTheta, source.x * sinTheta + source.y * cosTheta,
				source.z);
	}

	/**
	 * Rotate a Vec3f about the world-space Z-axis by a given angle specified in
	 * degrees.
	 * 
	 * @param source    The vector to rotate.
	 * @param angleDegs The angle to rotate the vector in degrees.
	 * @return A rotated version of the vector.
	 */
	public static FloatVector3 rotateZDegs(FloatVector3 source, float angleDegs) {
		return FloatVector3.rotateZRads(source, angleDegs * Utils.DEGS_TO_RADS);
	}

	/**
	 * Rotate a source vector an amount in radians about an arbitrary axis.
	 * 
	 * @param source       The vector to rotate.
	 * @param angleRads    The amount of rotation to perform in radians.
	 * @param rotationAxis The rotation axis.
	 * @return The source vector rotated about the rotation axis.
	 */
	public static FloatVector3 rotateAboutAxisRads(FloatVector3 source, float angleRads, FloatVector3 rotationAxis) {
		FloatMatrix3 rotationMatrix = new FloatMatrix3();

		float sinTheta = (float) Math.sin(angleRads);
		float cosTheta = (float) Math.cos(angleRads);
		float oneMinusCosTheta = 1.0f - cosTheta;

		// It's quicker to pre-calc these and reuse than calculate x * y, then y * x
		// later (same thing).
		float xyOne = rotationAxis.x * rotationAxis.y * oneMinusCosTheta;
		float xzOne = rotationAxis.x * rotationAxis.z * oneMinusCosTheta;
		float yzOne = rotationAxis.y * rotationAxis.z * oneMinusCosTheta;

		// Calculate rotated x-axis
		rotationMatrix.m00 = rotationAxis.x * rotationAxis.x * oneMinusCosTheta + cosTheta;
		rotationMatrix.m01 = xyOne + rotationAxis.z * sinTheta;
		rotationMatrix.m02 = xzOne - rotationAxis.y * sinTheta;

		// Calculate rotated y-axis
		rotationMatrix.m10 = xyOne - rotationAxis.z * sinTheta;
		rotationMatrix.m11 = rotationAxis.y * rotationAxis.y * oneMinusCosTheta + cosTheta;
		rotationMatrix.m12 = yzOne + rotationAxis.x * sinTheta;

		// Calculate rotated z-axis
		rotationMatrix.m20 = xzOne + rotationAxis.y * sinTheta;
		rotationMatrix.m21 = yzOne - rotationAxis.x * sinTheta;
		rotationMatrix.m22 = rotationAxis.z * rotationAxis.z * oneMinusCosTheta + cosTheta;

		// Multiply the source by the rotation matrix we just created to perform the
		// rotation
		return rotationMatrix.mul(source);
	}

	/**
	 * Rotate a source vector an amount in degrees about an arbitrary axis.
	 * 
	 * @param source       The vector to rotate.
	 * @param angleDegs    The amount of rotation to perform in degrees.
	 * @param rotationAxis The rotation axis.
	 * @return The source vector rotated about the rotation axis.
	 */
	public static FloatVector3 rotateAboutAxisDegs(FloatVector3 source, float angleDegs, FloatVector3 rotationAxis) {
		return FloatVector3.rotateAboutAxisRads(source, angleDegs * Utils.DEGS_TO_RADS, rotationAxis);
	}

	// Overloaded toString method
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + Utils.DECIMAL_FORMAT.format(x) + ", y: " + Utils.DECIMAL_FORMAT.format(y) + ", z: "
				+ Utils.DECIMAL_FORMAT.format(z));
		return sb.toString();
	}

	/**
	 * Print this Vector.
	 */
	public void print() {
		System.out.println("Vector 3:\n" + toString());
	}

	public FloatVector3 add(FloatVector3 v) {
		return new FloatVector3(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public FloatVector3 sub(FloatVector3 v) {
		return new FloatVector3(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	/**
	 * Return a vector which is the result of multiplying this vector by another
	 * vector. This vector remains unchanged.
	 * 
	 * @param v The vector to multiply this vector by.
	 * @return The result of multiplying this vector by the 'v' vector.
	 **/
	public FloatVector3 mul(FloatVector3 v) {
		return new FloatVector3(this.x * v.x, this.y * v.y, this.z * v.z);
	}

	public FloatVector3 mul(float scale) {
		return new FloatVector3(this.x * scale, this.y * scale, this.z * scale);
	}

	/**
	 * Multiply the value of a Vec3f in place by a given scaling factor.
	 * 
	 * @param v     The vector to scale.
	 * @param scale The value used to scale each component of the 'v' vector.
	 **/
	public static void times(FloatVector3 v, float scale) {
		v.x *= scale;
		v.y *= scale;
		v.z *= scale;
	}

	/**
	 * Add a vector to a source vector - the source vector is modified.
	 * <p>
	 * This method does not perform any memory allocations - it merely adds 'other'
	 * to 'source'.
	 * 
	 * @param source The vector to which we will add a vector.
	 * @param other  The vector we will add to the 'source' vector.
	 */
	public static void add(FloatVector3 source, FloatVector3 other) {
		source.x += other.x;
		source.y += other.y;
		source.z += other.z;
	}

	/**
	 * Subtract a vector from a source vector - the source vector is modified.
	 * <p>
	 * This method does not perform any memory allocations - it merely subtracts
	 * 'other' from 'source'.
	 * 
	 * @param source The vector to which we will subtract a vector.
	 * @param other  The vector we will suctract from the 'source' vector.
	 */
	public static void sub(FloatVector3 source, FloatVector3 other) {
		source.x -= other.x;
		source.y -= other.y;
		source.z -= other.z;
	}

	public FloatVector3 div(float value) {
		return new FloatVector3(this.x / value, this.y / value, this.z / value);
	}

	/**
	 * Return a vector which is the result of projecting this vector onto a plane
	 * described by the provided surface normal.
	 * <p>
	 * Neither the vector on which this method is called or the provided plane
	 * normal vector are modified.
	 * <p>
	 * If the plane surface normal has a magnitude of zero then an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param planeNormal The normal that describes the plane onto which we will
	 *                    project this vector.
	 * @return A projected version of this vector.
	 */
	public FloatVector3 projectOntoPlane(FloatVector3 planeNormal) {
		if (!(planeNormal.length() > 0.0f)) {
			planeNormal.print();
			throw new IllegalArgumentException("Plane normal cannot be a zero vector.");
		}

		// Projection of vector b onto plane with normal n is defined as: b - ( b.n / (
		// |n| squared )) * n
		// Note: |n| is length or magnitude of the vector n, NOT its (component-wise)
		// absolute value
		FloatVector3 b = this.normalized();
		FloatVector3 n = planeNormal.normalized();
		return b.sub(n.mul(FloatVector3.dotProduct(b, planeNormal))).normalize();

		/**
		 * IMPORTANT: We have to be careful here - even code like the below (where
		 * dotProduct uses normalised versions of 'this' and planeNormal is off by
		 * enough to make the IK solutions oscillate:
		 *
		 * return this.minus( planeNormal.times( Vec3f.dotProduct(this, planeNormal) )
		 * ).normalised();
		 * 
		 */

		// Note: For non-normalised plane vectors we can use:
		// float planeNormalLength = planeNormal.length();
		// return b.minus( n.times( Vec3f.dotProduct(b, n) / (planeNormalLength *
		// planeNormalLength) ).normalised();
	}

	/**
	 * Calculate and return the direction unit vector from point a to point b.
	 * <p>
	 * If the opposite direction is required then the argument order can be swapped
	 * or the the result can simply be negated.
	 * 
	 * @param v1 The first location.
	 * @param v2 The second location.
	 * @return The normalised direction unit vector between point v1 and point v2.
	 */
	public static FloatVector3 getDirectionUV(FloatVector3 v1, FloatVector3 v2) {
		return v2.sub(v1).normalize();
	}

	/**
	 * Randomise the components of this vector to be random values between the
	 * provided half-open range as described by the minimum and maximum value
	 * arguments.
	 *
	 * @param min The minimum value for any given component (inclusive).
	 * @param max The maximum value for any given component (exclusive, i.e. a max
	 *            of 5.0f will be assigned values up to 4.9999f or such).
	 **/
	public void randomise(float min, float max) {
		this.x = Utils.randRange(min, max);
		this.y = Utils.randRange(min, max);
		this.z = Utils.randRange(min, max);
	}

	/**
	 * Return the array of this vector
	 */
	public float[] toArray() {
		float[] res = new float[3];
		res[0] = x;
		res[1] = y;
		res[2] = z;
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FloatVector3 other = (FloatVector3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
			return false;
		}
		return true;
	}

} // End of Vec3f class
