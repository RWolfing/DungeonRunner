package de.dungeonrunner.view;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Shape;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;

/**
 * A component to represent a lifebar of a game entity.
 * 
 * @author Robert Wolfinger
 *
 */
public class LifeBar extends SceneNode{

	private Color mColor;
	//The maximum hp of the lifebar
	private float mMaximum;
	//The current hp of the lifebar
	private float mCurrent;
	private Shape mLifeBarShape;
	
	/**
	 * Default constructor. 
	 */
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
	
	/**
	 * Sets the color of the lifebar.
	 * 
	 * @param color the color
	 */
	public void setColor(Color color){
		mColor = color;
		mLifeBarShape.setFillColor(mColor);
	}
	
	/**
	 * Sets the maximum of the life bar.
	 * 
	 * @param maximum the maximum hp amount
	 */
	public void setMaximum(float maximum){
		mMaximum = maximum;
	}

	/**
	 * Sets the current value of the life bar.
	 * 
	 * @param current the current hp amount
	 */
	public void setCurrent(float current){
		mCurrent = current;
	}
	
	/**
	 * Sets the dimension of the lifebar.
	 * 
	 * @param vector the dimensions
	 */
	public void setDimension(Vector2f vector){
		RectangleShape newRect = new RectangleShape(vector);
		newRect.setFillColor(mColor);
		mLifeBarShape = newRect;
	}
}
