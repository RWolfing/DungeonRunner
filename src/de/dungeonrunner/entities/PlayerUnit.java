package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.GameWorld;
import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireProjectileCommand;
import de.dungeonrunner.entities.Projectile.ProjectileType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class PlayerUnit extends Unit {

	private RectangleShape mCollisionShape = new RectangleShape();

	private FloatRect mDefaultCollisionRect;
	private FloatRect mAttackCollisionRect;

	private static final int mShootFrameStart = 3;

	public PlayerUnit(TextureID textureID, Properties props) {
		super(textureID, props);
		mNodeType = NodeType.PLAYER;
		setupAnimations();
		mDefaultCollisionRect = new FloatRect(27, 11, 62, 118);
		mAttackCollisionRect = new FloatRect(15, 15, 105, 118);
		setCollisionRect(mDefaultCollisionRect);
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mCollisionShape);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		// We move the player through commands so reset the velocity
		setVelocity(0, getVelocity().y);
	}

	// TODO Refactor
	@Override
	public boolean attack() {
		if (super.attack()) {
			List<SceneNode> collisions = new ArrayList<>();
			setCollisionRect(mAttackCollisionRect);
			GameWorld.getGame().getCollisionGraph().retrieve(collisions, getBoundingRect());
			for (SceneNode node : collisions) {
				if (node instanceof CrystalItem) {
					if (node.getBoundingRect().intersection(getBoundingRect()) != null) {
						((CrystalItem) node).mine();
					}
				}
			}
			setCollisionRect(mDefaultCollisionRect);
			return true;
		}
		return false;

	}

	private void setupAnimations() {
		// Create all animations
		AnimationNode mIdleAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_IDLE, 1000, true, 4,
				new Vector2i(135, 135));
		AnimationNode mWalkAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_PLAYER_RUN, 1000, true, 12,
				new Vector2i(135, 135));
		AnimationNode mJumpAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_PLAYER_JUMP, 1000, true, 5,
				new Vector2i(135, 135));
		AnimationNode mShootAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_MINER_THROW, 1200, false, 6,
				new Vector2i(135, 135));
		AnimationNode mAttackAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_MINER_ATTACK, 1000, false, 6,
				new Vector2i(135, 135));
		AnimationNode mDeathAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_MINER_DEATH, 1000, false, 5,
				new Vector2i(135, 135));
		// Attach necessary listeners to the animations
		mShootAnimation.setAnimationListener(new AnimationNode.AnimationListener() {
			private Unit mEntity;

			@Override
			public void onFrame(AnimationNode node, int frame) {
				// TODO polishing, verhunzter code sah von der animation her
				// besser aus, nicht abwarten bevor die animation abgelaufen ist
				if (frame == mShootFrameStart) {
					FireProjectileCommand command = new FireProjectileCommand(mEntity, NodeType.WORLD,
							ProjectileType.Dynamite);
					addCommand(command);
				}

				if (node.getNumFrames() - 1 == frame) {
					mEntity.resetShoot();
					;
				}
			}

			private AnimationListener init(Unit ctx) {
				mEntity = ctx;
				return this;
			}
		}.init(this));

		mAttackAnimation.setAnimationListener(new AnimationListener() {
			private Unit mUnit;

			@Override
			public void onFrame(AnimationNode node, int frame) {
				if (node.getNumFrames() - 1 == frame) {
					mUnit.resetAttack();
				}
			}

			private AnimationListener init(Unit unit) {
				mUnit = unit;
				return this;
			}
		}.init(this));

		// Set the animations
		setAnimation(mIdleAnimation, ANIM_ID.IDLE);
		setAnimation(mWalkAnimation, ANIM_ID.WALK);
		setAnimation(mJumpAnimation, ANIM_ID.JUMP);
		setAnimation(mShootAnimation, ANIM_ID.SHOOT);
		setAnimation(mAttackAnimation, ANIM_ID.ATTACK);
		setAnimation(mDeathAnimation, ANIM_ID.DEATH);
	}
}
