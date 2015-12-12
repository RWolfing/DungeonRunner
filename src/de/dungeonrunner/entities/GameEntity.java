package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.commands.SceneCommand;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;

public abstract class GameEntity extends SceneNode {

	private Vector2f mVelocity;
	private SpriteNode mSprite;
	private boolean mIsDestroyed;
	private List<SceneCommand> mPendingCommands;

	protected List<SceneNode> mCollisionObjects;

	public GameEntity() {
		mVelocity = Vector2f.ZERO;
		mCollisionObjects = new ArrayList<>();
		mPendingCommands = new ArrayList<>();
	}

	@Override
	protected void updateCurrent(Time dt) {
		move(Vector2f.mul(mVelocity, dt.asSeconds()));
	}

	@Override
	protected void collectCommand(CommandStack commandStack) {
		for(SceneCommand command : mPendingCommands){
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
			return mSprite.getBoundingRect();
		} else {
			return null;
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

	@Override
	public void destroy() {
		mIsDestroyed = true;
	}

	@Override
	protected boolean isDestroyed() {
		return mIsDestroyed;
	}
	
	public void addCommand(SceneCommand command){
		mPendingCommands.add(command);
	}
}
