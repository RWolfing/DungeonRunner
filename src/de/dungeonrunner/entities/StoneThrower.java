package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireProjectileCommand;
import de.dungeonrunner.entities.Projectile.ProjectileType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;

public class StoneThrower extends LeashedUnit {

	private static final int mShootFrameStart = 4;
	private final float mXVelocity = 30f;
	private final float mReloadDuration = 2000;

	private float mJumpStartX = Float.MIN_VALUE;
	private FloatRect mProxRect;
	private float mMinShootDistance = 700;

	private FloatRect mDefaultCollisionRect = new FloatRect(15, 18, 90, 100);
	private FloatRect mDeadCollisionRect = new FloatRect(0, 10, 105, 107);
	private Clock mShootTimer;

	public StoneThrower(TextureID textureID, Properties props) {
		super(textureID, props);
		setupAnimations();
		setVelocity(mXVelocity, getVelocity().y);
		setTotalHP(200);
		setCollisionRect(mDefaultCollisionRect);
		mProxRect = new FloatRect(0, 0, mMinShootDistance, 10);
		mShootTimer = new Clock();
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if (inShootRange()) {
			shoot();
		}
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		CollisionType type = super.processCollision(node);
		if (type == CollisionType.RIGHT || type == CollisionType.LEFT) {
			// AI of the enemy
			if (canJump() && mJumpStartX != getPosition().x) {
				mJumpStartX = getPosition().x;
				jump();
			} else if (!isJumping()) {
				setVelocity(-getVelocity().x, getVelocity().y);
			}
		}
		return type;
	}

	@Override
	public void damage(int damage) {
		super.damage(damage);
		jump();
	}

	private void setupAnimations() {
		AnimationNode idleAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_IDLE, 1000, true,
				4, new Vector2i(135, 120));
		AnimationNode mShootAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_ATTACK, 1000,
				false, 7, new Vector2i(135, 120));
		AnimationNode mWalkAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_WALK, 1000, true,
				11, new Vector2i(135, 120));
		AnimationNode mDeathAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_DEATH, 1000,
				false, 5, new Vector2i(135, 120));

		mShootAnimation.addAnimationListener(new AnimationListener() {
			private Unit mUnit;

			@Override
			public void onFrame(AnimationNode node, int frame) {
				if (frame == mShootFrameStart) {
					FireProjectileCommand command = new FireProjectileCommand(mUnit, NodeType.WORLD,
							ProjectileType.Stone);
					addCommand(command);
				}

				if (node.getNumFrames() - 1 == frame) {
					mUnit.resetShoot();
				}
			}

			private AnimationListener init(Unit unit) {
				mUnit = unit;
				return this;
			}
		}.init(this));

		mDeathAnimation.addAnimationListener(new AnimationListener() {

			@Override
			public void onFrame(AnimationNode node, int frame) {
				if (node.getNumFrames() - 1 == frame) {
					setCollisionRect(mDeadCollisionRect);
				}

			}
		});
		setAnimation(idleAnimation, ANIM_ID.IDLE);
		setAnimation(mShootAnimation, ANIM_ID.SHOOT);
		setAnimation(mWalkAnimation, ANIM_ID.WALK);
		setAnimation(mDeathAnimation, ANIM_ID.DEATH);
	}

	@Override
	public boolean shoot() {
		if (super.shoot()) {
			setVelocity(0, getVelocity().y);
			mShootTimer.restart();
			return true;
		}
		return false;
	}

	@Override
	public boolean canShoot() {
		return super.canShoot() && mShootTimer.getElapsedTime().asMilliseconds() > mReloadDuration;
	}

	@Override
	protected void resetShoot() {
		super.resetShoot();
		setVelocity(mXVelocity, getVelocity().y);
	}

	@Override
	protected void drawDebugging(RenderTarget target, RenderStates states) {
		super.drawDebugging(target, states);
		RectangleShape shape = new RectangleShape(new Vector2f(mProxRect.width, mProxRect.height));
		shape.setPosition(mProxRect.left - getPosition().x, 0);
		shape.setOutlineColor(Color.RED);
		shape.setOutlineThickness(1);
		shape.setFillColor(Color.TRANSPARENT);
		shape.draw(target, states);
	}

	private boolean inShootRange() {
		switch (getOrientation()) {
		case LEFT:
			mProxRect = new FloatRect(getPosition().x - mMinShootDistance, getPosition().y, mProxRect.width,
					mProxRect.height);
			break;
		case RIGHT:
			mProxRect = new FloatRect(getPosition().x, getPosition().y, mProxRect.width, mProxRect.height);
			break;
		default:
			System.err.println(this.getClass().getName() + ": Unkown orientation");
			break;
		}

		List<SceneNode> nodes = new ArrayList<>();
		GameState.getWorld().getCollisionGraph().retrieve(nodes, mProxRect);
		for (SceneNode node : nodes) {
			if (node instanceof PlayerUnit && node.getBoundingRect().intersection(mProxRect) != null) {
				return true;
			}
		}
		return false;
	}

}
