package de.dungeonrunner;

import java.util.List;
import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;
import tiled.core.Map;
import tiled.core.Tile;

public class PlayerEntity extends GameEntity {

	private Animation mIdleAnimation;

	public PlayerEntity(TextureID textureID) {
		mIdleAnimation = new Animation(textureID);
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setVelocity(new Vector2f(100f, 0));
		
		Map map = TmxMapLoader.getMap();
		System.out.println(map.getBounds());
//		checkCollision();
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		mIdleAnimation.draw(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		mIdleAnimation.setPosition(getPosition());
		mIdleAnimation.update(dt);
		System.out.println(getBoundingRect());
	}
	
//	protected void checkCollision(){
//		Properties propsLeftTop = TmxMapLoader.getTileInstancePropertiesAt((int) (getPosition().x + get, (int) getPosition().y);
//		getTransform().
//		Properties propsLeftBottom = TmxMapLoader.getTileInstancePropertiesAt((int) getPosition().x, (int) getPosition().y);
//		Properties propsRightTop = TmxMapLoader.getTileInstancePropertiesAt((int) getPosition().x, (int) getPosition().y);
//		Properties propsRightBottom = TmxMapLoader.getTileInstancePropertiesAt((int) getPosition().x, (int) getPosition().y);
//		
//		boolean isBlocking = Boolean.valueOf(props.getProperty("BlockVolume", "false"));
//		if(isBlocking)
//	}
//	
	public FloatRect getBoundingRect(){
		return getWorldTransform().transformRect(mIdleAnimation.getGlobalBounds());
	}
//	
//	private void setCollisionRectangle(int left, int top, int width, int height){
//		mCollisionRectangle = new IntRect(left, top, width, height);
//	}
}
