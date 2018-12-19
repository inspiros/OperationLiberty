package com.hust.robot.trajectory;

public class LSQBTrajectoryPlanner extends JointTrajectoryPlanner {
	private float prevAngle, coefficientA, thetaC, thetaD, factor3ac, factor4ac, factor3df, factor4df, blendTime,
			totalOperationTime;

	public LSQBTrajectoryPlanner(float angle, float target, float blendTimeFactor, float totalOperationTime) {
		this.prevAngle = angle;
		this.target = target;
		this.totalOperationTime = totalOperationTime;
		blendTimeFactor = validateBlendTimeFactor(blendTimeFactor);
		this.blendTime = totalOperationTime * blendTimeFactor;
		this.coefficientA = (target - prevAngle) / (totalOperationTime - blendTime);
		this.thetaC = prevAngle + coefficientA * blendTime / 2;
		this.thetaD = target - coefficientA * blendTime / 2;
		this.factor3ac = coefficientA / (blendTime * blendTime);
		this.factor4ac = -coefficientA / (2 * blendTime * blendTime * blendTime);
		this.factor4df = -(3 * target - 3 * thetaD - 2 * coefficientA * blendTime)
				/ (blendTime * blendTime * blendTime * blendTime);
		this.factor3df = -(coefficientA + 4 * factor4df * blendTime * blendTime * blendTime)
				/ (3 * blendTime * blendTime);
	}

	private float validateBlendTimeFactor(float blendTimeFactor) {
		if (blendTimeFactor < 0) {
			return 0;
		} else if (blendTimeFactor > 0.5f) {
			return 0.5f;
		}
		return blendTimeFactor;
	}

	@Override
	public float angleAt(float time) {
		if (time < blendTime) {
			return prevAngle + factor3ac * time * time * time + factor4ac * time * time * time * time;
		} else if (time <= totalOperationTime - blendTime) {
			return thetaC + coefficientA * (time - blendTime);
		} else if (time < totalOperationTime) {
			float t = time - totalOperationTime + blendTime;
			return thetaD + coefficientA * t + factor3df * t * t * t + factor4df * t * t * t * t;
		}
		return target;
	}

	@Override
	public float speedAt(float time) {
		if (time < blendTime) {
			return 3 * factor3ac * time * time + 4 * factor4ac * time * time * time;
		} else if (time <= totalOperationTime - blendTime) {
			return coefficientA;
		} else {
			float t = time - totalOperationTime + blendTime;
			return 3 * factor3df * t * t + 4 * factor4df * t * t * t;
		}
	}

	@Override
	public float accelerationAt(float time) {
		if (time < blendTime) {
			return 6 * factor3ac * time + 12 * factor4ac * time * time;
		} else if (time <= totalOperationTime - blendTime) {
			return 0;
		} else {
			float t = time - totalOperationTime + blendTime;
			return 6 * factor3df * t + 12 * factor4df * t * t;
		}
	}

}
