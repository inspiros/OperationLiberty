package com.hust.view;

import processing.core.PApplet;

public class Keyboard {
	
	private PWindow app;
	private float x;
	private float y;
	private float width;
	private float height;
	
	private KeyButton[] keys = new KeyButton[12];
	
	public Keyboard(PWindow app, float x, float y, float width, float height) {
		this.app = app;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setupKeyButtons();
	}
	
	public void setBounds(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setupKeyButtons();
	}
	
	private void setupKeyButtons() {
		
	}
	
	public void render() {
		app.rect(x, y, width, height);
		app.textMode(PApplet.CENTER);
		for (KeyButton keyButton : keys) {
			keyButton.render();
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
}
