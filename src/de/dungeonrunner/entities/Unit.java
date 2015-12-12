package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireBulletCommand;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class Unit extends GameEntity {

	public enum ORIENTATION {
		LEFT, RIGHT
	}

	private Vector2f mProjectileSpawn;
	private ORIENTATION mOrientation = ORIENTATION.RIGHT;

	private final int mJumpTime = 800;
	private float mLeftJumpTime = mJumpTime;
	private float mJumpVelocity = -180;
	private boolean mIsJumping;
	protected boolean mIsJumpPossible;
	private int mHitPointsTotal;
	private int mHitPoints;

	public Unit(TextureID textureID) {
		super();
		mProperties.setProperty(Constants.UNIT_VOLUME, "true");
		mNodeType = NodeType.UNIT;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID))));
		mProjectileSpawn = new Vector2f(127, 64);
		mIsJumping = false;
		mIsJumpPossible = false;
		mHitPointsTotal = mHitPoints = 0;
		setVelocity(0, Constants.GRAVITY.y);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);

		if (mIsJumping) {
			mLeftJumpTime = mLeftJumpTime - dt.asMilliseconds();
			if (mLeftJumpTime < 0) {
				mLeftJumpTime = 0;
				mIsJumping = false;
				setVelocity(getVelocity().x, Constants.GRAVITY.y);
			} else {
				float velY = (mLeftJumpTime / mJumpTime) * mJumpVelocity;
				setVelocity(getVelocity().x, velY);
			}
		}
		mIsJumpPossible = false;
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (Boolean.valueOf(node.getProperty("BlockVolume"))) {
			FloatRect intersection1 = node.getBoundingRect().intersection(getBoundingRect());
			if (intersection1 == null) {
				return;
			}

			//Round the collision TODO why is this necessary
			if (intersection1.width > 3 || intersection1.height > 3) {
				if (intersection1.width > intersection1.height) {
					// Player inbound from Top or Bottom
					if (getBoundingRect().top < intersection1.top) {
						// Collision from top
						mIsJumpPossible = true;
						setPosition(getWorldPosition().x, getWorldPosition().y - intersection1.height);
					} else {
						// Collision from bottom
						setPosition(getWorldPosition().x, getWorldPosition().y + intersection1.height);
					}
				} else {
					if (getBoundingRect().left < intersection1.left) {
						// Collision from the right
						setPosition(getWorldPosition().x - intersection1.width, getWorldPosition().y);
					} else {
						setPosition(getWorldPosition().x + intersection1.width, getWorldPosition().y);
					}
				}
			}
		}
	}

	public boolean jump() {
		if (!mIsJumping && mIsJumpPossible) {
			mIsJumping = true;
			mLeftJumpTime = mJumpTime;
			return true;
		}
		return false;
	}

	public boolean canJump() {
		return mIsJumpPossible;
	}

	public boolean isJumping() {
		return mIsJumping;
	}

	public void shoot() {
		FireBulletCommand command = new FireBulletCommand(this, NodeType.WORLD,
				Vector2f.add(getPosition(), mProjectileSpawn));
		addCommand(command);
	}

	@Override
	public void setVelocity(Vector2f velocity) {
		super.setVelocity(velocity);
		checkOrientationChange(velocity.x);
	}

	@Override
	public void setVelocity(float vx, float vy) {
		super.setVelocity(vx, vy);
		checkOrientationChange(vx);
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

	public ORIENTATION getOrientation() {
		return mOrientation;
	}

	private void calculateOrientation(float vx) {
		if (vx < 0) {
			if (mOrientation != ORIENTATION.LEFT) {
				mOrientation = ORIENTATION.LEFT;
				setVelocity(-getVelocity().x, getVelocity().y);
			}
		} else if (vx > 0) {
			if (mOrientation != ORIENTATION.RIGHT) {
				mOrientation = ORIENTATION.RIGHT;
				setVelocity(-getVelocity().x, getVelocity().y);
			}
		}
	}
	
	public void damage(int damage){
		mHitPoints -= damage;
		if(mHitPoints < 0){
			destroy();
		}
	}
	
	public void setTotalHP(int hp){
		mHitPointsTotal = mHitPoints = hp;
	}
	
	public int getTotalHP(){
		return mHitPointsTotal;
	}
	
	public void setHitpoints(int hp){
		if(mHitPoints + hp > mHitPointsTotal){
			mHitPoints = mHitPointsTotal;
		} else {
			mHitPoints = hp;
		}
	}
	
	public int getHitpoints(){
		return mHitPoints;
	}
}
