package de.dungeonrunner.entities;

import java.util.HashMap;
import java.util.Properties;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.TmxKeys;

/**
 * This class represents a unit in the game.
 * It provides functionality to jump, attack, shoot etc.
 * 
 * @author Robert Wolfinger
 *
 */
public class Unit extends GameEntity {

	/*
	 * Enum for the different animations walk, jump, shoot etc.
	 */
	public enum ANIM_ID {
		WALK, JUMP, IDLE, SHOOT, ATTACK, DEATH
	}

	/*
	 * Enum for the animation state of the unit like idle, walking, jumping etc.
	 */
	public enum STATE {
		IDLE, WALKING, JUMPING, SHOOTING, ATTACKING, DYING
	}

	//Is the unit shooting
	private boolean mIsShooting;
	//Is the unit attacking
	private boolean mIsAttacking;
	//Is the unit jumping
	private boolean mIsJumping;
	//Is the unit dead
	private boolean mIsDead;
	
	private final int mJumpTime = 800;
	private float mLeftJumpTime = mJumpTime;
	//Velocity of the jump in y-direction
	private float mJumpVelocity = -600;
	protected boolean mCanJump;
	private int mHitPointsTotal;
	private int mHitPoints;

	// Animations
	private STATE mAnimState;
	private HashMap<ANIM_ID, AnimationNode> mAnimations;
	private AnimationNode mActiveAnimation;

	/**
	 * Creates a new unit with the given texureID and properties.
	 * 
	 * @param textureID the id of the texture to use
	 * @param props the properties of the unit
	 */
	public Unit(TextureID textureID, Properties props) {
		super(props);
		//Every unit is a unit volume
		mPropertySet.put(Constants.UNIT_VOLUME, "true");
		mNodeType = NodeType.UNIT;
		//Initial setup
		mIsJumping = false;
		mCanJump = true;
		mIsShooting = false;
		mIsAttacking = false;
		mHitPointsTotal = mHitPoints = 0;
		mIsDead = false;
		mAnimations = new HashMap<>();
		//The initial velocity is 0
		setVelocity(0, 0);
		//set the default sprite
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)), null));
		
		//Try to retrieve the spawn position from the given properties
		float spawnX = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_X, "0"));
		float spawnY = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_Y, "0"));
		setPosition(spawnX, spawnY);
	}

	@Override
	protected void updateCurrent(Time dt) {
		if (!mIsDead) {
			//Update the unit if it is not dead
			if (mIsJumping) {
				//if the unit is jumping, we have to check if the jump time is over
				mLeftJumpTime = mLeftJumpTime - dt.asMilliseconds();
				if (mLeftJumpTime < 0) {
					//Jump is over reset the velocity
					mLeftJumpTime = 0;
					mIsJumping = false;
					setVelocity(getVelocity().x, 0);
				} else {
					//compute the velocity in y-direction
					float velY = (mLeftJumpTime / mJumpTime) * mJumpVelocity;
					setVelocity(getVelocity().x, velY);
				}
			}

			//Check if the unit should be dead
			if (mHitPoints < 0) {
				mIsDead = true;
			}

			//Check if we have to change the animation
			computeAnimationState();
			if (mActiveAnimation != null && !mIsDead) {
				//Update the orientation of the animation to the orientation of the unit
				mActiveAnimation.setOrientation(getOrientation());
			}
			// Reset Variables
			mCanJump = false;
		} else {
			//Unit is dead set velocity to 0
			setVelocity(0, 0);	
		}
		super.updateCurrent(dt);
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		CollisionType type = super.processCollision(node);

		//If the bottom of the unit collides with another scene node it can jump
		if (type == CollisionType.BOTTOM) {
			mCanJump = true;
		}
		return type;
	}

	/**
	 * Computes the correct animation state depending on the state 
	 * of the unit.
	 */
	private void computeAnimationState() {
		if (mIsDead) {
			requestState(STATE.DYING);
		} else if (mIsShooting) {
			requestState(STATE.SHOOTING);
		} else if (mIsAttacking) {
			requestState(STATE.ATTACKING);
		} else if (!mCanJump) {
			requestState(STATE.JUMPING);
		} else if (getVelocity().x != 0) {
			requestState(STATE.WALKING);
		} else {
			requestState(STATE.IDLE);
		}
	}

	/**
	 * Requests and changes the used animation depending 
	 * ont the given animState.
	 * 
	 * @param animState the animaState that the unit is in
	 */
	private void requestState(STATE animState) {
		if (mAnimState == STATE.DYING) {
			//if the unit is already dead we have to do nothing
			return;
		}

		//Switch the animation depending on the requested state
		switch (animState) {
		case IDLE:
			if (mAnimations.get(ANIM_ID.IDLE) == null)
				return;
			mActiveAnimation = mAnimations.get(ANIM_ID.IDLE);
			mAnimState = STATE.IDLE;
			break;
		case WALKING:
			if (mAnimations.get(ANIM_ID.WALK) == null)
				return;
			mActiveAnimation = mAnimations.get(ANIM_ID.WALK);
			mAnimState = STATE.WALKING;
			break;
		case JUMPING:
			if (mAnimations.get(ANIM_ID.JUMP) == null)
				return;
			mActiveAnimation = mAnimations.get(ANIM_ID.JUMP);
			mAnimState = STATE.JUMPING;
			break;
		case SHOOTING:
			if (mAnimations.get(ANIM_ID.SHOOT) == null)
				return;
			mActiveAnimation = mAnimations.get(ANIM_ID.SHOOT);
			mAnimState = STATE.SHOOTING;
			break;
		case ATTACKING:
			if (mAnimations.get(ANIM_ID.ATTACK) == null)
				return;
			mActiveAnimation = mAnimations.get(ANIM_ID.ATTACK);
			mAnimState = STATE.ATTACKING;
			break;
		case DYING:
			if (mAnimations.get(ANIM_ID.DEATH) == null) {
				destroy();
				return;
			}
			mActiveAnimation = mAnimations.get(ANIM_ID.DEATH);
			mAnimState = STATE.DYING;
			break;
		default:
			break;
		}
		
		//set the chosen animation as the current sprite and start it
		setSprite(mActiveAnimation);
		mActiveAnimation.start();
	}

	/**
	 * Makes the unit jump, if possible.
	 * 
	 * @return can the unit jump
	 */
	public boolean jump() {
		if (!mIsJumping && canJump()) {
			mIsJumping = true;
			mLeftJumpTime = mJumpTime;
			return true;
		}
		return false;
	}

	/**
	 * Returns if the unit can jump.
	 * A unit can only jump if it is not already airborne.
	 * 
	 * @return can the unit jump
	 */
	public boolean canJump() {
		return mCanJump;
	}

	/**
	 * Returns if the unit is already jumping.
	 * 
	 * @return is the unit jumping
	 */
	public boolean isJumping() {
		return mIsJumping;
	}

	/**
	 * Returns if the unit can shoot.
	 * A unit can only shoot if it is not already shooting.
	 * 
	 * @return can the unit shoot
	 */
	public boolean canShoot() {
		return !mIsShooting;
	}

	/**
	 * Makes the unit shoot, if possible.
	 * 
	 * @return if the shoot was executed
	 */
	public boolean shoot() {
		if (canShoot()) {
			mIsShooting = true;
			return true;
		}
		return false;
	}

	/**
	 * Resets the shooting value of the unit, so
	 * it can shoot again.
	 */
	protected void resetShoot() {
		mIsShooting = false;
	}

	/**
	 * Makes the unit attack, if possible.
	 * A unit can only attack, if it is not already attacking.
	 * 
	 * @return could the attack be executed
	 */
	public boolean attack() {
		if (!mIsAttacking) {
			mIsAttacking = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Resets the attack of a unit, so it 
	 * can attack again.
	 */
	protected void resetAttack() {
		mIsAttacking = false;
	}

	/**
	 * Damages the unit by substracting the given 
	 * value from the hitpoints of the unit.
	 * 
	 * @param damage damage
	 */
	public void damage(int damage) {
		mHitPoints -= damage;
	}

	/**
	 * Sets the total hp of the unit.
	 * 
	 * @param hp the hp of the unit
	 */
	public void setTotalHP(int hp) {
		mHitPointsTotal = mHitPoints = hp;
	}

	/**
	 * Returns the total hp of the unit.
	 * 
	 * @return the total hp
	 */
	public int getTotalHP() {
		return mHitPointsTotal;
	}

	/**
	 * Sets the current hitpoints of the unit.
	 * The value is higher then the total hitpoints, it 
	 * will be rounded to the total hitpoints.
	 * 
	 * @param hp the current hitpoints
	 */
	public void setHitpoints(int hp) {
		if (mHitPoints + hp > mHitPointsTotal) {
			mHitPoints = mHitPointsTotal;
		} else {
			mHitPoints = hp;
		}
	}

	/**
	 * Retrieves the hitpoints of the unit.
	 * 
	 * @return the hitpoints
	 */
	public int getHitpoints() {
		return mHitPoints;
	}

	/**
	 * Sets the given AnimationNode to the given animID.
	 * 
	 * @param node a animation node
	 * @param animID id that the node should be used for
	 */
	public void setAnimation(AnimationNode node, ANIM_ID animID) {
		mAnimations.put(animID, node);
	}

	/**
	 * Retrieves the position where projectiles coming from this unit should be spawned.
	 * 
	 * @return the projectile spawn
	 */
	public Vector2f getProjectileSpawn() {
		return getPosition();
	}
}
