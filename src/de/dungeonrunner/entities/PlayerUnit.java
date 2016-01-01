package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireProjectileCommand;
import de.dungeonrunner.entities.Projectile.ProjectileType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;

/**
 * A unit representing the player in the game.
 * 
 * @author Robert Wolfinger
 *
 */
public class PlayerUnit extends Unit {

	private final int TOTAL_HP = 100;
	private final int mShootFrameStart = 3;
	
	//Different collision rectangles for different states
	private FloatRect mDefaultCollisionRect;
	private FloatRect mAttackCollisionRect;

	//Ammo available to the unit
	private int mCurrentDynamiteAmmo;

	public PlayerUnit(TextureID textureID, Properties props) {
		super(textureID, props);
		mNodeType = NodeType.PLAYER;
		setupAnimations();
		mDefaultCollisionRect = new FloatRect(27, 11, 62, 118);
		mAttackCollisionRect = new FloatRect(15, 15, 105, 118);
		setCollisionRect(mDefaultCollisionRect);
		setTotalHP(TOTAL_HP);
		mCurrentDynamiteAmmo = 3;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		// We move the player through commands so reset the velocity
		setVelocity(0, getVelocity().y);
		
		//Update the game ui
		GameState.getGameUI().getAmmoComponent().setCurrentAmmo(mCurrentDynamiteAmmo);
		GameState.getGameUI().getLifeComponent().setHealthBar((float) getHitpoints() / (float) getTotalHP());
	}

	@Override
	public boolean attack() {
		if (super.attack()) {
			//If we attack we have to switch the collision rectangle
			List<SceneNode> collisions = new ArrayList<>();
			setCollisionRect(mAttackCollisionRect);
			GameState.getWorld().getCollisionGraph().retrieve(collisions, getBoundingRect());
			//And check if we are actually mining any crystals
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

	/**
	 * Creates the animations needed and used for this unit.
	 */
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
		mShootAnimation.addAnimationListener(new AnimationNode.AnimationListener() {
			private Unit mEntity;

			@Override
			public void onFrame(AnimationNode node, int frame) {
				//If shoot frame is reached, create a command to fire the projectile
				if (frame == mShootFrameStart) {
					FireProjectileCommand command = new FireProjectileCommand(mEntity, NodeType.WORLD,
							ProjectileType.Dynamite);
					addCommand(command);
				}

				//if the end of the animation was reached, reset the shooting
				if (node.getNumFrames() - 1 == frame) {
					mEntity.resetShoot();
				}
			}

			private AnimationListener init(Unit ctx) {
				mEntity = ctx;
				return this;
			}
		}.init(this));

		mAttackAnimation.addAnimationListener(new AnimationListener() {
			private Unit mUnit;

			@Override
			public void onFrame(AnimationNode node, int frame) {
				//Reset the attack if the end of the animation was reached
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

	@Override
	public boolean shoot() {
		if(super.shoot()){
			//Decrement the ammunition
			mCurrentDynamiteAmmo--;
			return true;
		}
		return false;
	}

	@Override
	public boolean canShoot() {
		return super.canShoot() && mCurrentDynamiteAmmo > 0;
	}
	
	
}
