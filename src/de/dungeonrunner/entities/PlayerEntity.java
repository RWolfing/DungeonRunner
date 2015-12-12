package de.dungeonrunner.entities;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireBulletCommand;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class PlayerEntity extends GameEntity {

	private RectangleShape mCollisionShape = new RectangleShape();

	private final int mJumpTime = 800;
	private float mLeftJumpTime = mJumpTime;
	private float mJumpVelocity = -180;
	private boolean mIsJumpPossible = false;

	private final Vector2f mInitialVelocity = new Vector2f(0f, 100f);
	private boolean mIsJumping = false;

	private Vector2f mProjectileSpawn;

	public PlayerEntity(TextureID textureID) {
		super();
		mProperties.setProperty("BlockVolume", "true");
		mNodeType = NodeType.PLAYER;
		AnimationNode mIdleAnimation = new AnimationNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		mProjectileSpawn = new Vector2f(127, 64);
		setSprite(mIdleAnimation);
		setVelocity(mInitialVelocity);
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mCollisionShape);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);

		if (mIsJumping) {
			mLeftJumpTime = mLeftJumpTime - dt.asMilliseconds();
			if (mLeftJumpTime < 0) {
				mLeftJumpTime = 0;
				mIsJumping = false;
				setVelocity(mInitialVelocity);
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
			mCollisionShape.setPosition(new Vector2f(intersection1.left, intersection1.top));
			mCollisionShape.setSize(new Vector2f(intersection1.width, intersection1.height));
			mCollisionShape.setFillColor(Color.WHITE);
			mCollisionShape.setOutlineColor(Color.BLACK);
			mCollisionShape.setOutlineThickness(1.0f);

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

	public void jump() {
		if (!mIsJumping && mIsJumpPossible) {
			mIsJumping = true;
			mLeftJumpTime = mJumpTime;
		}
	}

	public void shoot() {
		FireBulletCommand command = new FireBulletCommand(this, NodeType.WORLD,
				Vector2f.add(getPosition(), mProjectileSpawn));
		addCommand(command);
	}
}
