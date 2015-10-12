package de.dungeonrunner;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;

public class PlayerEntity extends GameEntity {

	private Animation mIdleAnimation;

	public PlayerEntity(TextureID textureID) {
		mProperties.setProperty("BlockVolume", "true");
		mIdleAnimation = new Animation(textureID);
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setPosition(20f, 0f);
		mIdleAnimation.setPosition(getPosition());
		setVelocity(new Vector2f(10f, 0f));
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		mIdleAnimation.draw(target, states);
		super.drawCurrent(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		mIdleAnimation.move(Vector2f.mul(getVelocity(), dt.asSeconds()));
		mIdleAnimation.update(dt);
	}

	public FloatRect getBoundingRect() {
		return getWorldTransform().transformRect(mIdleAnimation.getGlobalBounds());
	}

	@Override
	protected void onCollision(SceneNode node) {
		FloatRect boundsNode = node.getBoundingRect();
		FloatRect playerRect = getBoundingRect();
		
		float left, top;
		left = playerRect.left;
		top = playerRect.top;
		
		if(playerRect.left + playerRect.width > boundsNode.left){
			left = boundsNode.left - boundsNode.width;
			
		} else if(playerRect.left < boundsNode.left + boundsNode.width){
			left = boundsNode.left + boundsNode.width;
		}
		
		if(playerRect.top < boundsNode.top + boundsNode.height){
			top = boundsNode.top + boundsNode.height;
		} else if(playerRect.top + playerRect.height > boundsNode.top){
			top = boundsNode.top - playerRect.height;
		}
		mIdleAnimation.setPosition(left, top);
		
		//Check movement collision
		
	}
}
