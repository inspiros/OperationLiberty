package com.hust.view;

public interface Drawable <D>{

	public abstract void render(D drawer);
	
	public abstract void setupDrawer(D drawer);
}
