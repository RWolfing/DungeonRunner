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

public class Unit extends GameEntity {

	public enum ANIM_ID {
		WALK, JUMP, IDLE, SHOOT, ATTACK, DEATH
	}

	public enum STATE {
		IDLE, WALKING, JUMPING, SHOOTING, ATTACKING, DYING
	}

	private boolean mIsShooting;

	private boolean mIsAttacking;

	private final int mJumpTime = 800;
	private float mLeftJumpTime = mJumpTime;
	private float mJumpVelocity = -600;
	private boolean mIsJumping;
	protected boolean mIsAirborne;
	private int mHitPointsTotal;
	private int mHitPoints;

	private boolean mIsDead;

	// Animations
	private STATE mAnimState;
	private HashMap<ANIM_ID, AnimationNode> mAnimations;
	private AnimationNode mActiveAnimation;

	public Unit(TextureID textureID, Properties props) {
		super(props);
		mPropertySet.put(Constants.UNIT_VOLUME, "true");
		mNodeType = NodeType.UNIT;
		mIsJumping = false;
		mIsAirborne = false;
		mIsShooting = false;
		mIsAttacking = false;
		mHitPointsTotal = mHitPoints = 0;
		mIsDead = false;
		mAnimations = new HashMap<>();
		setVelocity(0, Constants.GRAVITY.y);
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)), null));
		float spawnX = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_X, "0"));
		float spawnY = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_Y, "0"));
		setPosition(spawnX, spawnY);
	}

	@Override
	protected void updateCurrent(Time dt) {

		if (!mIsDead) {
			super.updateCurrent(dt);
			if (mIsJumping) {
				mLeftJumpTime = mLeftJumpTime - dt.asMilliseconds();
				if (mLeftJumpTime < 0) {
					mLeftJumpTime = 0;
					mIsJumping = false;
					setVelocity(getVelocity().x, 0);
				} else {
					float velY = (mLeftJumpTime / mJumpTime) * mJumpVelocity;
					setVelocity(getVelocity().x, velY);
				}
			}

			if (mHitPoints < 0) {
				mIsDead = true;
			}

			computeAnimationState();
			if (mActiveAnimation != null && !mIsDead) {
				mActiveAnimation.setOrientation(getOrientation());
			}
			// Reset Variables
			mIsAirborne = false;
		} else {
			setVelocity(Constants.GRAVITY);
			super.updateCurrent(dt);
		}
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		CollisionType type = super.processCollision(node);

		if (type == CollisionType.BOTTOM) {
			mIsAirborne = true;
		}
		return type;
	}

	private void computeAnimationState() {
		if (mIsDead) {
			requestState(STATE.DYING);
		} else if (mIsShooting) {
			requestState(STATE.SHOOTING);
		} else if (mIsAttacking) {
			requestState(STATE.ATTACKING);
		} else if (!mIsAirborne) {
			requestState(STATE.JUMPING);
		} else if (getVelocity().x != 0) {
			requestState(STATE.WALKING);
		} else {
			requestState(STATE.IDLE);
		}
	}

	//TODO request state is called to often
	private void requestState(STATE animState) {
		if (animState == mAnimState || mAnimState == STATE.DYING) {
			return;
		}

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
		setSprite(mActiveAnimation);
		mActiveAnimation.setOrientation(getOrientation());
		mActiveAnimation.start();
	}

	public boolean jump() {
		if (!mIsJumping && mIsAirborne) {
			mIsJumping = true;
			mLeftJumpTime = mJumpTime;
			return true;
		}
		return false;
	}

	public boolean canJump() {
		return mIsAirborne;
	}

	public boolean isJumping() {
		return mIsJumping;
	}

	public boolean canShoot() {
		return !mIsShooting;
	}

	public boolean shoot() {
		if (canShoot()) {
			mIsShooting = true;
			return true;
		}
		return false;
	}

	protected void resetShoot() {
		mIsShooting = false;
	}

	public boolean attack() {
		if (!mIsAttacking) {
			mIsAttacking = true;
			return true;
		} else {
			return false;
		}
	}

	protected void resetAttack() {
		mIsAttacking = false;
	}

	public void damage(int damage) {
		mHitPoints -= damage;
	}

	public void setTotalHP(int hp) {
		mHitPointsTotal = mHitPoints = hp;
	}

	public int getTotalHP() {
		return mHitPointsTotal;
	}

	public void setHitpoints(int hp) {
		if (mHitPoints + hp > mHitPointsTotal) {
			mHitPoints = mHitPointsTotal;
		} else {
			mHitPoints = hp;
		}
	}

	public int getHitpoints() {
		return mHitPoints;
	}

	public void setAnimation(AnimationNode node, ANIM_ID animID) {
		mAnimations.put(animID, node);
	}

	public Vector2f getProjectileSpawn() {
		return getPosition();
	}
}
