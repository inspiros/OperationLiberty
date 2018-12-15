package com.hust.robot;

import com.hust.utils.FloatVector3;

public class Link {

	public FloatVector3 direction;
	
	public Link() {
		direction = FloatVector3.ZERO;
	}
	
	public Link(FloatVector3 direction) {
		this.direction = direction;
	}
	
	public float length() {
		return direction.length();
	}
}
