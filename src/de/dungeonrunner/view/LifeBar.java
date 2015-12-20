package de.dungeonrunner.view;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Shape;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;

public class LifeBar extends SceneNode{

	private Color mColor;
	private float mMaximum;
	private float mCurrent;
	private Shape mLifeBarShape;
	
	
	public LifeBar(){
		super(null);
		mLifeBarShape = new RectangleShape();
		mMaximum = 1;
		mCurrent = 1;
		mColor = Color.WHITE;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		mLifeBarShape.setPosition(getPosition());
		mLifeBarShape.setScale(mCurrent / mMaximum, 1);
		mLifeBarShape.draw(target, states);
	}
	
	public void setColor(Color color){
		mColor = color;
		mLifeBarShape.setFillColor(mColor);
	}
	
	public void setMaximum(float maximum){
		mMaximum = maximum;
	}

	public void setCurrent(float current){
		mCurrent = current;
	}
	
	public void setDimension(Vector2f vector){
		RectangleShape newRect = new RectangleShape(vector);
		newRect.setFillColor(mColor);
		mLifeBarShape = newRect;
	}
}
