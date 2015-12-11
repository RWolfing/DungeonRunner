package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.SceneNode;

public abstract class GameEntity extends SceneNode {

	private Vector2f mVelocity;
	private AnimationNode mActiveAnimation;
	
	protected List<SceneNode> mCollisionObjects;

	public GameEntity() {
		mVelocity = Vector2f.ZERO;
		mCollisionObjects = new ArrayList<>();
	}

	@Override
	protected void updateCurrent(Time dt) {
		move(Vector2f.mul(mVelocity, dt.asSeconds()));
	}

	public void setAnimation(AnimationNode activeAnimation) {
		if (mActiveAnimation != null) {
			detachChild(mActiveAnimation);
		}
		mActiveAnimation = activeAnimation;
		if (mActiveAnimation != null) {
			attachChild(mActiveAnimation);
		}
	}

	@Override
	public FloatRect getBoundingRect() {
		return mActiveAnimation.getBoundingRect();
	}

	public void setVelocity(Vector2f velocity) {
		mVelocity = velocity;
	}

	public void setVelocity(float vx, float vy) {
		mVelocity = new Vector2f(vx, vy);
	}

	public Vector2f getVelocity() {
		return mVelocity;
	}
}
