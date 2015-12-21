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

public class LeashedUnit extends Unit {

	private FloatRect mLeashBounds;

	public LeashedUnit(TextureID textureID, Properties props) {
		super(textureID, props);
		mLeashBounds = FloatRect.EMPTY;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if(!checkInBounds(getBoundingRect())){
			setVelocity(-getVelocity().x, getVelocity().y);
		}
	}

	public void setLeashBounds(float x, float y, float width, float height) {
		mLeashBounds = new FloatRect(x, y, width, height);
	}

	public FloatRect getLeashBounds() {
		return mLeashBounds;
	}

	public float getRandomXInLeash() {
		switch (getOrientation()) {
		case LEFT:
			return mLeashBounds.width;
		default:
			return 0;
		}
	}
	
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
		super.drawDebugging(target, states);
		RectangleShape shape = new RectangleShape(new Vector2f(mLeashBounds.width, mLeashBounds.height));
		shape.setPosition(mLeashBounds.left - getPosition().x, mLeashBounds.top - getPosition().y);
		shape.setOutlineColor(Color.BLUE);
		shape.setOutlineThickness(1f);
		shape.setFillColor(Color.TRANSPARENT);
		shape.draw(target, states);
	}

}
