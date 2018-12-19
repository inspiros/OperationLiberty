package com.hust.view;

import java.util.ArrayList;
import java.util.LinkedList;

import com.hust.core.DataBuffer;
import com.hust.robot.DataChangeListener;
import com.hust.robot.KinematicsSolver;
import com.hust.utils.FloatVector3;
import com.jogamp.newt.opengl.GLWindow;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Slider;
import g4p_controls.G4P;
import g4p_controls.GCScheme;

public class PWindow extends HApplet implements DataChangeListener<Float> {
	private DataBuffer data;
	public PWindow app = this;

	public final String appPath = sketchPath();
	public GLWindow window;
	private ControlP5 guiController;

	/**
	 * Camera control.
	 */
	//private PeasyCam camera;
	private FloatVector3 cameraPosition, centerPosition;

	private ArrayList<Slider> sliders = new ArrayList<Slider>();

	private LinkedList<Target> targets = new LinkedList<Target>();

	public PWindow(DataBuffer data) {
		this.data = data;
		componentsDrawer.addDrawable(this.data.getArm());
		componentsDrawer.setupDrawables();
		HApplet.runSketch(new String[] { this.getClass().getSimpleName() }, this);
	}

	@Override
	public void settings() {
		size(800, 600, P3D);
		smooth(4);
	}

	@Override
	public void setup() {
		window = (GLWindow) surface.getNative();
		// window.setFullscreen(true);
		surface.setTitle("Operation Liberty");
		surface.setFrameRate(24);

		// noLoop();
//		camera = new PeasyCam(this, -100);
//		camera.setViewport(100, 100, -100, 1);
//		camera.lookAt(0, 0, 0);
		cameraPosition = new FloatVector3(width / 3, height / 3, 100);
		centerPosition = new FloatVector3();

		createGui();
	}

	public void render() {
		background(100);
		cameraTransform();
		drawGround();
		drawBaseAxis();
		if (targets.size() > 0) {
			for (Target target : targets) {
				target.render();
			}
		}
	}

	private void drawGround() {
		noFill();
		strokeWeight(0.5f);
		stroke(255);
		for (int x = -200; x < 200; x += 40) {
			beginShape(QUAD_STRIP);
			for (int y = -200; y < 200; y += 40) {
				vertex(x, y, 0);
				vertex(x + 40, y, 0);
				vertex(x, y + 40, 0);
				vertex(x + 40, y + 40, 0);
			}
			endShape();
		}
	}

	private void drawBaseAxis() {
		strokeWeight(1.5f);
		stroke(255, 0, 0);
		line(0, 0, 0, 100, 0, 0);
		stroke(0, 255, 0);
		line(0, 0, 0, 0, 100, 0);
		stroke(0, 0, 255);
		line(0, 0, 0, 0, 0, 100);
		stroke(0);
	}

	public void cameraTransform() {
		camera(cameraPosition.x, cameraPosition.y, cameraPosition.z, // eyeX, eyeY, eyeZ
				centerPosition.x, centerPosition.y, centerPosition.z, // centerX, centerY, centerZ
				0, 0, 1); // upX, upY, upZ
	}

	@Override
	public void mouseDragged() {
		if (mouseButton == RIGHT) {
			cameraPosition = FloatVector3.rotateZDegs(cameraPosition, (float)(pmouseX - mouseX) / 2);
			cameraPosition = FloatVector3.rotateAboutAxisDegs(cameraPosition, (float)(pmouseY - mouseY) / 2,
					cameraPosition.cross(FloatVector3.Z_AXIS).normalized());
		}
	}

	@Override
	public void mouseWheel(processing.event.MouseEvent event) {
		cameraPosition = cameraPosition.mul((cameraPosition.length() + 3 * event.getCount()) / cameraPosition.length());
	}

	/**
	 * GUI
	 */
	private void createGui() {
		G4P.messagesEnabled(false);
		G4P.setGlobalColorScheme(GCScheme.CYAN_SCHEME);

		guiController = new ControlP5(this, createFont("Cambria", 12));
		
		for (int i = 0; i < data.getDof(); i++) {
			Slider slider = guiController.addSlider("sliderAngle" + i)
					.setPosition(width / 10, height / 8 + height / 10 * i).setSize(width / 4, height / 12)
					.setValue(data.getArm().getBone(i).joint.angle)
					.setRange(data.getArm().getBone(i).joint.lowerLimit, data.getArm().getBone(i).joint.upperLimit)
					.setLabel("θ" + i).addCallback(new IdCallbackListener(i) {

						@Override
						public void controlEvent(CallbackEvent event) {
							if (event.getAction() == 100) {
								targets.clear();

								data.setTargetDegs(getId(), event.getController().getValue());
							}
						}
					});
			sliders.add(slider);
		}
		sliders.trimToSize();

		guiController.addButton("forwardKinematics").setPosition(width / 10, height / 20).setSize(width / 10, 20)
				.setLabel("FK").addCallback(new CallbackListener() {

					@Override
					public void controlEvent(CallbackEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == 100) {

						}
					}
				});
		guiController.addButton("inverseKinematics").setPosition(width * 3 / 12, height / 20).setSize(width / 10, 20)
				.setLabel("IK").addCallback(new CallbackListener() {

					@Override
					public void controlEvent(CallbackEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == 100) {
							new CoordinatePicker(app);
						}
					}
				});
	}

	public void updateSliders(float[] values) {
		for (int i = 0; i < values.length; i++) {
			sliders.get(i).changeValue(values[i]);
		}
	}

	public void updateSlider(int id, float value) {
		sliders.get(id).changeValue(value);
	}

	public void updateTarget(float[] t) {
		if (t.length != 3) {
			throw new IllegalArgumentException("Must be point of 3D");
		}
		targets.clear();
		targets.add(new Target(app, t[0], t[1], t[2]));

		data.moveToPosition(new FloatVector3(t[0], t[1], t[2]), KinematicsSolver.IKMethod.GRADIENT_DESCENT);
	}

	@Override
	public void dataChanged(int id) {
	}

	@Override
	public void dataChangedTo(int id, Float value) {
		sliders.get(id).changeValue(value);
	}
}
