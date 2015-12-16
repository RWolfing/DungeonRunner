package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.commands.SceneCommand;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;

public abstract class GameEntity extends SceneNode {

	public enum ORIENTATION {
		LEFT(-1), RIGHT(1);

		private int mValue;

		ORIENTATION(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}
	}

	private ORIENTATION mOrientation = ORIENTATION.RIGHT;

	private Vector2f mVelocity;
	private SpriteNode mSprite;
	private boolean mIsDestroyed;
	private List<SceneCommand> mPendingCommands;
	private FloatRect mCollisionRect;

	protected List<SceneNode> mCollisionObjects;

	public GameEntity(Properties props) {
		super(props);
		mVelocity = Vector2f.ZERO;
		mCollisionObjects = new ArrayList<>();
		mPendingCommands = new ArrayList<>();
		mCollisionRect = FloatRect.EMPTY;
	}

	@Override
	protected void updateCurrent(Time dt) {
		move(Vector2f.mul(mVelocity, dt.asSeconds()));
	}

	@Override
	protected void collectCommand(CommandStack commandStack) {
		for (SceneCommand command : mPendingCommands) {
			commandStack.push(command);
		}
		mPendingCommands.clear();
	}

	public void setSprite(SpriteNode activeAnimation) {
		if (mSprite != null) {
			detachChild(mSprite);
		}
		mSprite = activeAnimation;
		if (mSprite != null) {
			attachChild(mSprite);
		}
	}

	@Override
	public FloatRect getBoundingRect() {
		if (mSprite != null) {
			return new FloatRect(mSprite.getBoundingRect().left + mCollisionRect.left,
					mSprite.getBoundingRect().top + mCollisionRect.top, mCollisionRect.width, mCollisionRect.height);
		}
		return mCollisionRect;
	}

	public void setVelocity(Vector2f velocity) {
		checkOrientationChange(velocity.x);
		mVelocity = velocity;
	}

	public void setVelocity(float vx, float vy) {
		checkOrientationChange(vx);
		mVelocity = new Vector2f(vx, vy);
	}

	public Vector2f getVelocity() {
		return mVelocity;
	}

	@Override
	public void destroy() {
		mIsDestroyed = true;
	}

	@Override
	protected boolean isDestroyed() {
		return mIsDestroyed;
	}

	public void addCommand(SceneCommand command) {
		mPendingCommands.add(command);
	}

	public void setCollisionRect(FloatRect collisionRect) {
		mCollisionRect = collisionRect;
	}

	public ORIENTATION getOrientation() {
		return mOrientation;
	}

	public SpriteNode getSprite() {
		return mSprite;
	}

	private void checkOrientationChange(float vx) {
		if (vx < 0) {
			if (mOrientation != ORIENTATION.LEFT) {
				mOrientation = ORIENTATION.LEFT;
			}
		} else if (vx > 0) {
			if (mOrientation != ORIENTATION.RIGHT) {
				mOrientation = ORIENTATION.RIGHT;
			}
		}
	}
}
