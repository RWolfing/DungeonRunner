package de.dungeonrunner;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;

public class PlayerEntity extends GameEntity {

	public PlayerEntity(TextureID textureID) {
		mProperties.setProperty("BlockVolume", "true");

		AnimationNode mIdleAnimation = new AnimationNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setAnimation(mIdleAnimation);
		setVelocity(new Vector2f(25f, 3f));
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
	}

	@Override
	protected void onCollision(SceneNode node) {
//		FloatRect boundsNode = node.getBoundingRect();
//		FloatRect playerRect = getBoundingRect();
//
//		float left, top;
//		left = playerRect.left;
//		top = playerRect.top;
//
//		if (playerRect.left + playerRect.width > boundsNode.left) {
//			left = boundsNode.left - boundsNode.width;
//
//		} else if (playerRect.left < boundsNode.left + boundsNode.width) {
//			left = boundsNode.left + boundsNode.width;
//		}
//
//		if (playerRect.top < boundsNode.top + boundsNode.height) {
//			top = boundsNode.top + boundsNode.height;
//		} else if (playerRect.top + playerRect.height > boundsNode.top) {
//			top = boundsNode.top - playerRect.height;
//		}
//		//System.out.println("Position: " + left + "/" + top);
//
//		//setVelocity(-1* getVelocity().x, -1* getVelocity().y);
//		// Check movement collision

	}
}
