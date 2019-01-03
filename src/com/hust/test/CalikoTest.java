package com.hust.test;
import com.hust.view.demo.HApplet;
//
//import au.edu.federation.caliko.FabrikBone3D;
//import au.edu.federation.caliko.FabrikChain3D;
//import au.edu.federation.caliko.FabrikChain3D.BaseboneConstraintType3D;
//import au.edu.federation.caliko.FabrikJoint3D.JointType;
//import au.edu.federation.utils.Mat3f;
//import au.edu.federation.utils.Vec3f;
//import processing.event.KeyEvent;
//
public class CalikoTest extends HApplet {
//	private final Vec3f zero = new Vec3f(0, 0, 0);
//	private final Vec3f xAxis = new Vec3f(1, 0, 0);
//	private final Vec3f yAxis = new Vec3f(0, 1, 0);
//	private final Vec3f zAxis = new Vec3f(0, 0, 1);
//
//	private DataBuffer data;
//
//	private FabrikChain3D arm;
//
//	private Vec3f target = new Vec3f();
//	private float distance;
//
//	public static void main(String[] args) {
//		new CalikoTest();
//	}
//
//	public CalikoTest() {
//		super();
//		this.pre();
//		System.out.println(new Vec3f(3, 3, 7).projectOntoPlane(zAxis));
//		System.out.println(Mat3f.rotBetween(xAxis, new Vec3f(1, 0, 0).projectOntoPlane(zAxis))
//				.times(new Vec3f(-1, 0, 0).normalised()));
//		System.out.println(Mat3f.rotBetween(xAxis, new Vec3f(1, 0, 0)));
//		HApplet.runSketch(new String[] { this.getClass().getSimpleName() }, this);
//	}
//
//	boolean dataTest = true;
//
//	public void pre() {
//		arm = new FabrikChain3D();
//
//		arm.setSolveDistanceThreshold(0);
//		arm.setMaxIterationAttempts(50);
//		arm.setMinIterationChange(0.01f);
//
//		arm.setFixedBaseMode(true);
//		arm.setBaseLocation(zero);
//
//		if (dataTest) {
//			data = DataBuffer.setupModel();
//			//constructArm(data.getArm(), data.getRotationAxis(), data.getRangeLimiters(), data.getEndPoints());
//		} else {
//
//			FabrikBone3D baseBone = new FabrikBone3D(zero, zAxis, 50);
//
//			arm.addBone(baseBone);
//			arm.setRotorBaseboneConstraint(BaseboneConstraintType3D.LOCAL_ROTOR, zAxis, 0);
//			// arm.setLocalHingedBasebone(zAxis, 90, 90, xAxis);
//
//			// arm.addConsecutiveRotorConstrainedBone(zAxis, 40, 135);
//			arm.addConsecutiveHingedBone(zAxis, 30, JointType.LOCAL_HINGE, yAxis, 45, 135, zAxis);
//
//			arm.addConsecutiveHingedBone(zAxis, 30, JointType.LOCAL_HINGE, yAxis, 45, 135, zAxis);
//
//			arm.addConsecutiveHingedBone(zAxis, 20, JointType.LOCAL_HINGE, xAxis, 135, 135, zAxis);
//
//			arm.addConsecutiveHingedBone(zAxis, 30, JointType.LOCAL_HINGE, yAxis, 45, 135, zAxis);
//
//			arm.addConsecutiveHingedBone(zAxis, 20, JointType.LOCAL_HINGE, yAxis, 45, 135, zAxis);
//
//			// printChain(arm);
//
//		}
//
//		target = new Vec3f(0, 0, arm.getEffectorLocation().z);
//
//		// System.out.println(chain.getEffectorLocation().toString());
//		// arm.solveForTarget(new Vec3f(0, 30, 40));
//	}
//
//	public void constructArm(Chain robotArm, Vec3f[] axis, float[] bounds, float[][] endPoints) {
//
//		FabrikBone3D baseBone = new FabrikBone3D(zero, zAxis, ArrayUtils.mean(endPoints[0]));
//		arm.addBone(baseBone);
//		arm.setRotorBaseboneConstraint(BaseboneConstraintType3D.LOCAL_ROTOR, zAxis, 0);
//		boolean zRotated = true;
//		for (int i = 1; i < endPoints.length; i++) {
//			float boneLength = ArrayUtils.mean(endPoints[i]);
//			if(axis[i].equals(Vec3f.Y_AXIS)) {
//				if (zRotated) {
//					arm.addConsecutiveRotorConstrainedBone(zAxis, boneLength, Math.max(Math.abs(bounds[0]), Math.abs(bounds[1])));
//					zRotated = false;
//				} else {
//					arm.addConsecutiveHingedBone(zAxis, boneLength, JointType.LOCAL_HINGE, yAxis, 45, 135, zAxis);
//				}
//				break;
//			} else if (axis[i].equals(Vec3f.Z_AXIS)) {
//				arm.addConsecutiveRotorConstrainedBone(zAxis, boneLength, 0);
//				zRotated = true;
//				break;
//			} else if (axis[i].equals(Vec3f.X_AXIS)) {
//				if (zRotated) {
//					arm.addConsecutiveRotorConstrainedBone(zAxis, boneLength, Math.max(Math.abs(bounds[0]), Math.abs(bounds[1])));
//					zRotated = false;
//				} else {
//					arm.addConsecutiveHingedBone(zAxis, boneLength, JointType.LOCAL_HINGE, xAxis, 135, 135, zAxis);
//				}
//				break;
//			}
//		}
//	}
//
//	@Override
//	public void settings() {
//		size(600, 400, P3D);
//	}
//
//	@Override
//	public void setup() {
//		frameRate(30);
//	}
//
	@Override
	public void render() {
		background(150);
//		camera(300, 300, 200, // eyeX, eyeY, eyeZ
//				0, 0, 0, // centerX, centerY, centerZ
//				0, 0, 1); // upX, upY, upZ
//		drawBaseAxis();
//		drawArm();
//		drawTarget();
//
	}
//
//	private void drawArm() {
//		for (int i = 0; i < arm.getNumBones(); i++) {
//			strokeWeight(arm.getNumBones() - i + 1);
//			line(arm.getBone(i).getStartLocation().toArray(), arm.getBone(i).getEndLocation().toArray());
//		}
//	}
//
//	private void drawTarget() {
//		strokeWeight(0.5f);
//		stroke(0);
//		line(target.x, target.y, target.z, target.x, target.y, 0);
//		if (target.x != 0 && target.y != 0) {
//			stroke(0, 255, 0);
//			line(target.x, target.y, 0, 0, target.y, 0);
//			stroke(255, 0, 0);
//			line(target.x, target.y, 0, target.x, 0, 0);
//		}
//		stroke(0);
//		line(target.x, target.y, target.z, target.x, 0, target.z);
//		if (target.x != 0 && target.z != 0) {
//			stroke(0, 0, 255);
//			line(target.x, 0, target.z, 0, 0, target.z);
//			stroke(255, 0, 0);
//			line(target.x, 0, target.z, target.x, 0, 0);
//		}
//		stroke(0);
//		line(target.x, target.y, target.z, 0, target.y, target.z);
//		if (target.y != 0 && target.z != 0) {
//			stroke(0, 0, 255);
//			line(0, target.y, target.z, 0, 0, target.z);
//			stroke(0, 255, 0);
//			line(0, target.y, target.z, 0, target.y, 0);
//		}
//		if (distance > arm.getSolveDistanceThreshold()) {
//			stroke(255, 255, 0);
//			strokeWeight(1);
//			// dashLine(arm.getEffectorLocation().toArray(), target.toArray());
//		}
//		strokeWeight(10);
//		stroke(220, 220, 0, 200);
//		point(target.x, target.y, target.z);
//
//		strokeWeight(5);
//		for (int i = 0; i < 4; i++) {
//			point(getPoint(i).x, getPoint(i).y, 0);
//		}
//	}
//
//	private void drawBaseAxis() {
//		strokeWeight(1.5f);
//		stroke(255, 0, 0);
//		line(-500, 0, 0, 500, 0, 0);
//		stroke(0, 255, 0);
//		line(0, -500, 0, 0, 500, 0);
//		stroke(0, 0, 255);
//		line(0, 0, -500, 0, 0, 500);
//		stroke(0);
//	}
//
//	public static void printChain(FabrikChain3D chain) {
//		System.out.println("Chain locations:");
//		for (int i = 0; i < chain.getNumBones(); i++) {
//			System.out.println(i + " - " + chain.getBone(i).getEndLocation().toString());
//		}
//		System.out.println();
//	}
//
//	public Vec3f getPoint(int index) {
//		if (index == arm.getNumBones()) {
//			return arm.getEffectorLocation();
//		}
//		return arm.getBone(index).getStartLocation();
//	}
//
//	public static boolean isCoplanar(Vec3f p1, Vec3f p2, Vec3f p3, Vec3f p4) {
//		Vec3f planar = p4.minus(p1).times(normalVector(p1, p2, p3));
//		if (planar.x + planar.y + planar.z <= 0.01) {
//			System.err.println(true);
//			return true;
//		}
//		return false;
//	}
//
//	public static Vec3f normalVector(Vec3f p1, Vec3f p2, Vec3f p3) {
//		return (p2.minus(p1).cross((p3.minus(p1))));
//	}
//
//	public void point(float[] loc) {
//		point(loc[0], loc[1], loc[2]);
//	}
//
//	public float[] getAngles() {
//		float[] res = new float[arm.getNumBones() - 1];
//		for (int i = 1; i < arm.getNumBones(); i++) {
//			res[i - 1] = Vec3f.getAngleBetweenDegs(arm.getBone(i).getDirectionUV(),
//					arm.getBone(i - 1).getDirectionUV());
//			res[i - 1] = Float.isNaN(res[i - 1]) ? 0 : res[i - 1];
//		}
//		return res;
//	}
//
//	public void printAngles() {
//		System.out.print("Angles: ");
//		for (float angle : getAngles()) {
//			System.out.print(angle + "  ");
//		}
//		System.out.println();
//	}
//
//	@Override
//	public void keyPressed(KeyEvent event) {
//		if (event.getKeyCode() == UP || event.getKey() == 'w') {
//			target.set(target.plus(new Vec3f(0, 0, 10)));
//		} else if (event.getKeyCode() == DOWN || event.getKey() == 's') {
//			target.set(target.plus(new Vec3f(0, 0, -10)));
//		} else if (event.getKeyCode() == RIGHT) {
//			target.set(target.plus(new Vec3f(-10, 0, 0)));
//		} else if (event.getKeyCode() == LEFT) {
//			target.set(target.plus(new Vec3f(10, 0, 0)));
//		} else if (event.getKey() == 'a') {
//			target.set(target.plus(new Vec3f(0, -10, 0)));
//		} else if (event.getKey() == 'd') {
//			target.set(target.plus(new Vec3f(0, 10, 0)));
//		}
//		try {
//			distance = arm.solveForTarget(target);
//			isCoplanar(arm.getBone(0).getStartLocation(), arm.getBone(1).getStartLocation(),
//					arm.getBone(2).getStartLocation(), arm.getBone(3).getStartLocation());
//			System.out.println(arm.getEffectorLocation().toString());
//
//			printAngles();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		}
//	}
}
