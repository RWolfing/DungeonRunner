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
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Helper;

/**
 * An entity in the game, it provides all the necessary 
 * functionality needed by every entity.
 * 
 * @author Robert Wolfinger
 *
 */
public abstract class GameEntity extends SceneNode {

	/**
	 * Enum for the orientation of the entity.
	 * 
	 * @author Robert Wolfinger
	 *
	 */
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

	private ORIENTATION mOrientation;

	private Vector2f mVelocity;
	private SpriteNode mSprite;
	private boolean mIsDestroyed;
	
	//Commands that should be executed in the next update cycle
	private List<SceneCommand> mPendingCommands;
	
	private FloatRect mCollisionRect;
	protected List<SceneNode> mCollisionObjects;

	/**
	 * Default constructor, creates the entity from the given parameters.
	 * 
	 * @param props the properties of the node
	 */
	public GameEntity(Properties props) {
		super(props);
		mOrientation = ORIENTATION.RIGHT;
		mVelocity = Vector2f.ZERO;
		mCollisionObjects = new ArrayList<>();
		mPendingCommands = new ArrayList<>();
		mCollisionRect = FloatRect.EMPTY;
	}

	@Override
	protected void updateCurrent(Time dt) {
		//Add the gravity to the movement of the entity
		Vector2f movement = Vector2f.add(mVelocity, Constants.GRAVITY);
		move(Vector2f.mul(movement, dt.asSeconds()));
	}

	@Override
	protected void collectCommand(CommandStack commandStack) {
		//Add every pending command to the command stack
		for (SceneCommand command : mPendingCommands) {
			commandStack.push(command);
		}
		mPendingCommands.clear();
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		super.processCollision(node);
		//Per default we check if the entity collides with a blocking volume and reset it to the previous position accordingly
		if (Boolean.valueOf(node.getProperty(Constants.BLOCK_VOLUME))) {
			return Helper.resetEntityByCollision(this, getBoundingRect().intersection(node.getBoundingRect()));
		}
		return CollisionType.NONE;
	}

	/**
	 * Sets the sprite used to display this entity.
	 * 
	 * @param activeAnimation the sprite used for the entity
	 */
	public void setSprite(SpriteNode activeAnimation) {
		if (mSprite != null) {
			//If we have a sprite already attached we need to remove it
			detachChild(mSprite);
		}
		mSprite = activeAnimation;
		if (mSprite != null) {
			//We attach the new sprite
			attachChild(mSprite);
		}
	}

	@Override
	public FloatRect getBoundingRect() {
		//The bounding rectangle of an entity is its collision rectangle
		if (mSprite != null) {
			return new FloatRect(mSprite.getBoundingRect().left + mCollisionRect.left,
					mSprite.getBoundingRect().top + mCollisionRect.top, mCollisionRect.width, mCollisionRect.height);
		}
		return mCollisionRect;
	}

	/**
	 * Sets the velocity of the entity.
	 * 
	 * @param velocity the velocity
	 */
	public void setVelocity(Vector2f velocity) {
		checkOrientationChange(velocity.x);
		mVelocity = velocity;
	}

	/**
	 * Sets the velocity of the entity.
	 * 
	 * @param vx the velocity in x direction
	 * @param vy the velocity in y direction
	 */
	public void setVelocity(float vx, float vy) {
		checkOrientationChange(vx);
		mVelocity = new Vector2f(vx, vy);
	}

	/**
	 * Retrieves the velocity of the entity.
	 * 
	 * @return the velocity
	 */
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

	/**
	 * Adds a new command to the pending commands.
	 * 
	 * @param command a command
	 */
	public void addCommand(SceneCommand command) {
		mPendingCommands.add(command);
	}

	/**
	 * Sets the collision rectangle of the entity.
	 * 
	 * @param collisionRect the collision rectangle
	 */
	public void setCollisionRect(FloatRect collisionRect) {
		mCollisionRect = collisionRect;
	}

	/**
	 * Returns the orientation of the entity.
	 * 
	 * @return the orientation
	 */
	public ORIENTATION getOrientation() {
		return mOrientation;
	}

	/**
	 * Returns the sprite of the entity.
	 * 
	 * @return the sprite
	 */
	public SpriteNode getSprite() {
		return mSprite;
	}

	/**
	 * This method checks if the orientation has to change with the given
	 * velocity.
	 * 
	 * @param vx the velocity
	 */
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
