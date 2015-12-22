package de.dungeonrunner.view;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.event.Event;

public abstract class Component extends BasicTransformable implements Drawable {

	private boolean mIsSelected;
	private boolean mIsActive;

	protected float mWidth;
	protected float mHeight;

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// TODO Auto-generated method stub

	}

	public Component() {

	}

	public abstract boolean isSelectable();

	public boolean isSelected() {
		return mIsSelected;
	}

	public void select() {
		mIsSelected = true;
	}

	public void deselect() {
		mIsSelected = false;
	}

	public boolean isActive() {
		return mIsActive;
	}

	public void activate() {
		mIsActive = true;
	}

	public void deactivate() {
		mIsActive = false;
	}

	public abstract void handleEvent(Event event);

	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float width) {
		mWidth = width;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float height) {
		mHeight = height;
	}

}
