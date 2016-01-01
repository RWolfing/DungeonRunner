package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * A unit that only moves in its given parameters.
 * 
 * @author Robert Wolfinger
 *
 */
public class LeashedUnit extends Unit {

	//The bounds the unit is allowed to move in
	private FloatRect mLeashBounds;

	/**
	 * Default constructor, creates the unit from the given parameters.
	 * 
	 * @param textureID id of the texture to use
	 * @param props properties of the node
	 */
	public LeashedUnit(TextureID textureID, Properties props) {
		super(textureID, props);
		mLeashBounds = FloatRect.EMPTY;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if(!checkInBounds(getBoundingRect())){
			//If we are not in our bounds anymore, change the direction to the opposite
			setVelocity(-getVelocity().x, getVelocity().y);
		}
	}

	/**
	 * Sets the bounds, the unit is allowed to move in.
	 * 
	 * @param x x position
	 * @param y y position
	 * @param width width of the bounds
	 * @param height height of the bounds
	 */
	public void setLeashBounds(float x, float y, float width, float height) {
		mLeashBounds = new FloatRect(x, y, width, height);
	}

	/**
	 * Returns the bounds the unit is allowed to move in.
	 * 
	 * @return the leash bounds
	 */
	public FloatRect getLeashBounds() {
		return mLeashBounds;
	}
	
	/**
	 * Checks if the given bounds are inside the leash bounds
	 * of the unit.
	 * 
	 * @param bounds bounds to check
	 * @return if the bounds are inside the leash bounds
	 */
	private boolean checkInBounds(FloatRect bounds){
		FloatRect leashIntersection = getLeashBounds().intersection(bounds);
		if(leashIntersection != null){
			//Check if leashIntersection is same as bounds
			if(leashIntersection.width == bounds.width){
				return true;
			} else {
				return false;
			}
		} 
		return false;
	}

	@Override
	protected void drawDebugging(RenderTarget target, RenderStates states) {
		//Debugging only
		super.drawDebugging(target, states);
		RectangleShape shape = new RectangleShape(new Vector2f(mLeashBounds.width, mLeashBounds.height));
		shape.setPosition(mLeashBounds.left - getPosition().x, mLeashBounds.top - getPosition().y);
		shape.setOutlineColor(Color.BLUE);
		shape.setOutlineThickness(1f);
		shape.setFillColor(Color.TRANSPARENT);
		shape.draw(target, states);
	}

}
