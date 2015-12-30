package de.dungeonrunner.view;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.event.Event;

/**
 * Abstract base class of every UI component.
 * 
 * @author Robert Wolfinger
 *
 */
public abstract class Component extends BasicTransformable implements Drawable {

	//Is the component selected
	private boolean mIsSelected;
	//Is the component active
	private boolean mIsActive;

	//Width and height of the component
	protected float mWidth;
	protected float mHeight;

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// TODO Auto-generated method stub

	}

	/**
	 * Is the component selectable, has to be overwritten by every subclass.
	 * 
	 * @return is the component selectable
	 */
	public abstract boolean isSelectable();

	/**
	 * Returns if the component is selected.
	 * 
	 * @return is the component selected
	 */
	public boolean isSelected() {
		return mIsSelected;
	}

	/**
	 * Selects the component.
	 */
	public void select() {
		mIsSelected = true;
	}

	/**
	 * Deselects the component.
	 */
	public void deselect() {
		mIsSelected = false;
	}

	/**
	 * Returns if the component is active.
	 * 
	 * @return if the component is active
	 */
	public boolean isActive() {
		return mIsActive;
	}

	/**
	 * Activates the component.
	 */
	public void activate() {
		mIsActive = true;
	}

	/**
	 * Deactivates the component.
	 */
	public void deactivate() {
		mIsActive = false;
	}

	/**
	 * Method to handle click and key events.
	 * 
	 * @param event the event to handle
	 */
	public abstract void handleEvent(Event event);

	/**
	 * Returns the width of the component
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return mWidth;
	}

	/**
	 * Sets the width of the component.
	 * 
	 * @param width the width
	 */
	public void setWidth(float width) {
		mWidth = width;
	}

	/**
	 * Returns the height of the component.
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * Sets the height of the component.
	 * 
	 * @param height the height
	 */
	public void setHeight(float height) {
		mHeight = height;
	}

}
