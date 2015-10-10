package de.dungeonrunner;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

public abstract class GameEntity extends SceneNode {

	private Vector2f mVelocity;
	private Vector2f mAfterCollVelocity;
	private IntRect mCollisionRectangle;

	public GameEntity() {
		mVelocity = Vector2f.ZERO;
		mAfterCollVelocity = mVelocity;
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

	@Override
	protected void updateCurrent(Time dt) {
		move(Vector2f.mul(mVelocity, dt.asSeconds()));
	}
}
