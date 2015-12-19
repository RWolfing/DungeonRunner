package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
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
		WALK, JUMP, IDLE, SHOOT, ATTACK
	}

	public enum STATE {
		IDLE, WALKING, JUMPING, SHOOTING, ATTACKING
	}

	private boolean mIsShooting;

	private boolean mIsAttacking;

	private final int mJumpTime = 800;
	private float mLeftJumpTime = mJumpTime;
	private float mJumpVelocity = -180;
	private boolean mIsJumping;
	protected boolean mIsAirborne;
	private int mHitPointsTotal;
	private int mHitPoints;

	// Animations
	private STATE mAnimState;
	private AnimationNode mActiveAnimation;
	private AnimationNode mIdleAnimation;
	private AnimationNode mWalkAnimation;
	private AnimationNode mJumpAnimation;
	private AnimationNode mShootAnimation;
	private AnimationNode mAttackAnimation;

	public Unit(TextureID textureID, Properties props) {
		super(props);
		mPropertySet.put(Constants.UNIT_VOLUME, "true");
		mNodeType = NodeType.UNIT;
		mIsJumping = false;
		mIsAirborne = false;
		mIsShooting = false;
		mIsAttacking = false;
		mHitPointsTotal = mHitPoints = 0;
		setVelocity(0, Constants.GRAVITY.y);
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)), null));
		float spawnX = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_X, "0"));
		float spawnY = Float.valueOf(getProperty(TmxKeys.OBJECT_SPAWN_Y, "0"));
		setPosition(spawnX, spawnY);
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
		computeAnimationState();
		if (mActiveAnimation != null) {
			mActiveAnimation.setOrientation(getOrientation());
		}
		// Reset Variables
		mIsAirborne = false;
		setVelocity(0, getVelocity().y);
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (Boolean.valueOf(node.getProperty("BlockVolume"))) {
			FloatRect intersection1 = node.getBoundingRect().intersection(getBoundingRect());
			if (intersection1 == null) {
				return;
			}

			// Round the collision TODO why is this necessary
			if (intersection1.width > 3 || intersection1.height > 3) {
				if (intersection1.width > intersection1.height) {
					// Player inbound from Top or Bottom
					if (getBoundingRect().top < intersection1.top) {
						// Collision from top
						mIsAirborne = true;
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

	private void computeAnimationState() {
		if (mIsShooting) {
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

	private void requestState(STATE animState) {
		if (animState == mAnimState) {
			return;
		}
		switch (animState) {
		case IDLE:
			if (mIdleAnimation == null)
				return;
			mActiveAnimation = mIdleAnimation;
			mAnimState = STATE.IDLE;
			break;
		case WALKING:
			if (mWalkAnimation == null)
				return;
			mActiveAnimation = mWalkAnimation;
			mAnimState = STATE.WALKING;
			break;
		case JUMPING:
			if (mJumpAnimation == null)
				return;
			mActiveAnimation = mJumpAnimation;
			mAnimState = STATE.JUMPING;
			break;
		case SHOOTING:
			if (mShootAnimation == null)
				return;
			mActiveAnimation = mShootAnimation;
			mAnimState = STATE.SHOOTING;
			break;
		case ATTACKING:
			if (mAttackAnimation == null)
				return;
			mActiveAnimation = mAttackAnimation;
			mAnimState = STATE.ATTACKING;
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

	public void shoot() {
		mIsShooting = true;
	}

	protected void resetShoot() {
		mIsShooting = false;
	}

	public void attack() {
		if (!mIsAttacking) {
			mIsAttacking = true;
		}
	}

	protected void resetAttack() {
		mIsAttacking = false;
	}

	public void damage(int damage) {
		mHitPoints -= damage;
		if (mHitPoints < 0) {
			destroy();
		}
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
		switch (animID) {
		case IDLE:
			mIdleAnimation = node;
			break;
		case WALK:
			mWalkAnimation = node;
			break;
		case JUMP:
			mJumpAnimation = node;
			break;
		case SHOOT:
			mShootAnimation = node;
			break;
		case ATTACK:
			mAttackAnimation = node;
			break;
		default:
			break;
		}
	}

	public Vector2f getProjectileSpawn() {
		return getPosition();
	}
}
