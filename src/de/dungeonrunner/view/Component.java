package de.dungeonrunner.view;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transformable;
import org.jsfml.window.event.Event;

public abstract class Component extends BasicTransformable implements Drawable{

	private boolean mIsSelected;
	private boolean mIsActive;
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// TODO Auto-generated method stub
		
	}
	
	public Component(){
		
	}
	
	public abstract boolean isSelectable();
	
	public boolean isSelected(){
		return mIsSelected;
	}
	
	public void select(){
		mIsSelected = true;
	}
	
	public void deselect(){
		mIsSelected = false;
	}
	
	public boolean isActive(){
		return mIsActive;
	}
	
	public void activate(){
		mIsActive = true;
	}
	
	public void deactivate(){
		mIsActive = false;
	}
	
	public abstract void handleEvent(Event event);

}
