package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireBulletCommand;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class Unit extends GameEntity {

	public enum ANIM_ID {
		WALK, JUMP, IDLE, SHOOT
	}

	public enum STATE {
		IDLE, WALKING, JUMPING, SHOOTING
	}

	private Vector2f mProjectileSpawn;
	private boolean mIsShooting;

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

	public Unit(TextureID textureID) {
		super();
		mProperties.setProperty(Constants.UNIT_VOLUME, "true");
		mNodeType = NodeType.UNIT;
		mAnimState = STATE.IDLE;
		mIsJumping = false;
		mIsAirborne = false;
		mIsShooting = false;
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
		computeAnimationState();
		mActiveAnimation.setOrientation(getOrientation());

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
		System.out.println("Requested State: " + animState);
		switch (animState) {
		case IDLE:
			mActiveAnimation = mIdleAnimation;
			mAnimState = STATE.IDLE;
			break;
		case WALKING:
			mActiveAnimation = mWalkAnimation;
			mAnimState = STATE.WALKING;
			break;
		case JUMPING:
			mActiveAnimation = mJumpAnimation;
			mAnimState = STATE.JUMPING;
			break;
		case SHOOTING:
			mActiveAnimation = mShootAnimation;
			//TODO in PlayerUnit auslagern
			mShootAnimation.setAnimationListener(new AnimationNode.AnimationListener() {

				private GameEntity mEntity;

				@Override
				public void onFrame(AnimationNode node, int frame) {
					System.out.println(node.getNumFrames() + "/" + frame);
					if (node.getNumFrames() - 1 == 5) {
						System.out.println("Executing now");
						FireBulletCommand command = new FireBulletCommand(mEntity, NodeType.WORLD,
								Vector2f.add(getPosition(), mProjectileSpawn), getOrientation().getValue());
						addCommand(command);
						mIsShooting = false;
					}
				}

				private AnimationListener init(GameEntity ctx) {
					mEntity = ctx;
					return this;
				}
			}.init(this));
			mAnimState = STATE.SHOOTING;
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
		if (!mIsShooting) {
			mIsShooting = true;
		}
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
		default:
			break;
		}
	}

	public void setProjectileSpawn(Vector2f spawn) {
		mProjectileSpawn = spawn;
	}
}
