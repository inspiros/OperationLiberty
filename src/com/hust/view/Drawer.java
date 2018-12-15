package com.hust.view;

import java.util.ArrayList;
import java.util.List;

public abstract class Drawer<D> {
	protected D container;
	
	protected List<Drawable<D>> drawables = new ArrayList<Drawable<D>>();

	public void addDrawable(Drawable<D> drawable) {
		drawables.add(drawable);
	}
	
	public void setupDrawables() {
		System.out.println(drawables.size());
		for (Drawable<D> drawable : drawables) {
			drawable.setupDrawer(container);
		}
	}

	public void renderDrawables() {
		for (Drawable<D> drawable : drawables) {
			drawable.render(container);
		}
	}
}
