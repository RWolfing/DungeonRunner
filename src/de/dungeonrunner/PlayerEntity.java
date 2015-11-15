package de.dungeonrunner;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;

public class PlayerEntity extends GameEntity {

	private Set<SceneNode> mCollidingNodes;
	private final Vector2f mInitialVelocity = new Vector2f(40f, 5f);
	private Vector2f mPrePosition;

	public PlayerEntity(TextureID textureID) {
		mProperties.setProperty("BlockVolume", "true");
		mCollidingNodes = new HashSet<>();
		mPrePosition = Vector2f.ZERO;
		AnimationNode mIdleAnimation = new AnimationNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setAnimation(mIdleAnimation);
		setVelocity(mInitialVelocity);
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		processCollision();
		super.updateCurrent(dt);
	}

	@Override
	protected void onCollision(SceneNode node) {
		mCollidingNodes.add(node);
	}

	private void processCollision() {
		if (!mCollidingNodes.isEmpty()) {
			for (SceneNode node : mCollidingNodes) {
				FloatRect intersection = node.getBoundingRect().intersection(getBoundingRect());
				if (intersection == null) {
					continue;
				}

				float pixelDelta = 0f;
				float vectorX = 0;
				float vectorY = 0;
				if (intersection.height > intersection.width) {
					// Left or Right Collision
					if (getBoundingRect().left > node.getBoundingRect().left) {
						// Right
						vectorX = intersection.width + pixelDelta;
					} else {
						// Left
						vectorX = -(intersection.width + pixelDelta);
					}
					setVelocity(0, getVelocity().y);
				} else {
					if (getBoundingRect().top < node.getBoundingRect().top) {
						// Top
						vectorY = -(intersection.height + pixelDelta);
					} else {
						// Bottom
						vectorY = intersection.height + pixelDelta;
					}
					setVelocity(getVelocity().x, 0);
				}

				//move(new Vector2f(vectorX, vectorY));
				//moveChildren(mPrePosition);
				//setPosition(mPrePosition.x, mPrePosition.y);
			}
			mCollidingNodes.clear();
		} else {
			mPrePosition = getPosition();
			//setVelocity(mInitialVelocity);
		}
	}
}
