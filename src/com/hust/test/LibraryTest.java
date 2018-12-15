package com.hust.test;

import com.hust.utils.FloatQuaternion;
import com.hust.utils.FloatVector3;

public class LibraryTest {

	public static void main(String[] args) {
		FloatQuaternion quaternion = FloatQuaternion.createQuaternionDegs(FloatVector3.Z_AXIS, 0);
		System.out.println(quaternion.rotateDegs(FloatVector3.Y_AXIS, 10).getAngleDegs());
		
	}

}
