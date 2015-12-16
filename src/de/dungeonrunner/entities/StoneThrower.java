package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.FireProjectileCommand;
import de.dungeonrunner.entities.Projectile.ProjectileType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class StoneThrower extends Unit {

	private static final int mShootFrameStart = 4;
	
	private float mJumpStartX = Float.MIN_VALUE;
	
	public StoneThrower(TextureID textureID, Properties props){
		super(textureID, props);
		setupAnimations();
		setVelocity(30f, getVelocity().y);
		setTotalHP(200);
		setCollisionRect(new FloatRect(9, 18, 96, 100));
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
					
					//AI of the enemy
					if(canJump() && mJumpStartX != getPosition().x){
						mJumpStartX = getPosition().x;
						jump();
					} else if(!isJumping()){
						setVelocity(-getVelocity().x, getVelocity().y);
					}
				}
			}
		}
	}

	@Override
	public void damage(int damage) {
		super.damage(damage);
		shoot();
	}
	
	private void setupAnimations(){
		AnimationNode idleAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_IDLE, 1000, true, 4, new Vector2i(135, 120));
		AnimationNode mShootAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_STONE_THROWER_ATTACK, 1000, false, 7, new Vector2i(135, 120));
		
		mShootAnimation.setAnimationListener(new AnimationListener() {
			private Unit mUnit;
			
			@Override
			public void onFrame(AnimationNode node, int frame) {
				if (frame == mShootFrameStart) {
					FireProjectileCommand command = new FireProjectileCommand(mUnit, NodeType.WORLD, ProjectileType.Stone);
					addCommand(command);
				}
				
				if(node.getNumFrames() - 1 == frame){
					mUnit.resetShoot();
				}
			}
			
			private AnimationListener init(Unit unit){
				mUnit = unit;
				return this;
			}
		}.init(this));
		setAnimation(idleAnimation, ANIM_ID.IDLE);
		setAnimation(mShootAnimation, ANIM_ID.SHOOT);
	}
	
	
}
