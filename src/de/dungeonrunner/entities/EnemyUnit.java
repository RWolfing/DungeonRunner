package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class EnemyUnit extends Unit {

	private float mJumpStartX = Float.MIN_VALUE;
	
	public EnemyUnit(TextureID textureID){
		super(textureID);
		setVelocity(30f, getVelocity().y);
		setTotalHP(200);
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
		jump();
	}
	
	
}
