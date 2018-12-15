package com.hust.view;

public class KeyButton {

	private PWindow app;
	private Keyboard keyboard;
	private float x;
	private float y;
	private float width;
	private float height;
	private String key;

	public KeyButton(PWindow app, Keyboard keyboard, String key, float x, float y, float width, float height) {
		this.app = app;
		this.keyboard = keyboard;
		this.key = key;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setBounds(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render() {
		app.rect(keyboard.getX() + x, keyboard.getY() + y, width, height);
		app.text(key, keyboard.getX() + x + width / 2, keyboard.getY() + y + height / 2);
	}
}
