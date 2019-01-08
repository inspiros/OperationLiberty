package com.hust.utils.algebra;

public class Transformation {

	public FloatVector3 location;
	public FloatQuaternion rotation;
	
	public Transformation(FloatVector3 location, FloatQuaternion rotation) {
		super();
		this.location = location;
		this.rotation = rotation;
	}
	
}
