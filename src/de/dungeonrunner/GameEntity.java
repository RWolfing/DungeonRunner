package de.dungeonrunner;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

public abstract class GameEntity extends SceneNode {

	private Vector2f mVelocity;
	private AnimationNode mActiveAnimation;

	public GameEntity() {
		mVelocity = Vector2f.ZERO;
	}

	@Override
	protected void updateCurrent(Time dt) {
		moveChildren(Vector2f.mul(mVelocity, dt.asSeconds()));
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

	@Override
	public void setPosition(Vector2f v) {
		if (mActiveAnimation != null) {
			mActiveAnimation.setPosition(getWorldPosition());
		}
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

	public void moveChildren(Vector2f velocity) {
		for(SceneNode child : mChildren){
			child.move(velocity);
		}
	}
	
	public void setPoition(float x, float y){
		setPosition(x, y);
		if(mActiveAnimation != null){
			mActiveAnimation.setPosition(x, y);
		}
	}
}
