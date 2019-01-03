package com.hust.view.demo;

import java.util.ArrayList;
import java.util.List;

public abstract class Drawer<D> {
	protected D container;
	
	protected List<Drawable<D>> drawables = new ArrayList<Drawable<D>>();

	public void addDrawable(Drawable<D> drawable) {
		drawables.add(drawable);
	}

	public void renderDrawables() {
		for (Drawable<D> drawable : drawables) {
			drawable.render(container);
		}
	}
}
