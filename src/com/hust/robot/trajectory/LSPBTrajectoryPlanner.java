package com.hust.robot.trajectory;

public class LSPBTrajectoryPlanner extends JointTrajectoryPlanner {
	private float prevAngle, velocity, acceleration, blendTime, totalOperationTime;

	public LSPBTrajectoryPlanner(float angle, float target, float blendTimeFactor, float totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		blendTimeFactor = validateBlendTimeFactor(blendTimeFactor);
		float velocityFactor = 1 + blendTimeFactor * 2;
		this.velocity = (target - angle) / totalOperationTime * velocityFactor;
		this.blendTime = (angle - target + velocity * totalOperationTime) / velocity;
		this.acceleration = velocity / blendTime;
		this.totalOperationTime = totalOperationTime;
	}

	private float validateBlendTimeFactor(float blendTimeFactor) {
		if (blendTimeFactor < 0) {
			return 0;
		} else if (blendTimeFactor > 0.5f) {
			return 0.5f;
		}
		return blendTimeFactor;
	}
	
//	private float validateVelocityFactor(float velocityFactor) {
//		if(velocityFactor > 2) {
//			return 2.0f;
//		} else if (velocityFactor < 1) {
//			return 1.0f;
//		}
//		return velocityFactor;
//	}

	@Override
	public float angleAt(float time) {
		if (time < blendTime) {
			return prevAngle + acceleration / 2 * time * time;
		} else if (time <= totalOperationTime - blendTime) {
			return (prevAngle + target - velocity * totalOperationTime) / 2 + velocity * time;
		} else if (time < totalOperationTime) {
			return target - acceleration / 2 * totalOperationTime * totalOperationTime
					+ acceleration * totalOperationTime * time - acceleration / 2 * time * time;
		}
		return target;
	}

	@Override
	public float speedAt(float time) {
		if (time < blendTime) {
			return acceleration / 2 * time;
		} else if (time <= totalOperationTime - blendTime ) {
			return velocity;
		} else {
			return -acceleration / 2 * time;
		}
	}

	@Override
	public float accelerationAt(float time) {
		if (time < blendTime) {
			return acceleration / 2;
		} else if (time <= totalOperationTime - blendTime ) {
			return 0;
		} else {
			return -acceleration / 2;
		}
	}

}
