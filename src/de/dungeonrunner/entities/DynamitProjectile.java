package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

/**
 * A custom dynamite projectile.
 * 
 * @author Robert Wolfinger
 *
 */
public class DynamitProjectile extends Projectile {

	//The velocity of the projectile (add the gravity to it to make in fly in a straight line)
	private final Vector2f mVelocity = new Vector2f(200f, -Constants.GRAVITY_DOWN);
	
	private boolean mIsExploding;

	/**
	 * Default constructor, creates the projectile from the given parameters
	 * 
	 * @param shooter the unit that is shooting the projectile
	 * @param textureID the id of the texture to use
	 */
	public DynamitProjectile(Unit shooter, TextureID textureID) {
		super(shooter, textureID);
		setVelocity(mVelocity);
		setCollisionRect(new FloatRect(0, 0, 66, 76));
		setDamage(50);
		mIsExploding = false;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if (!mIsExploding) {
			//As long as the projectile is flying we rotate it
			getSprite().rotate(360 * dt.asSeconds());
		}
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		//Check if the projectile collides with a unit that this
		//projectile can damage
		if (checkIsBlocking(node) && !explode()) {
			if (node instanceof Unit) {
				((Unit) node).damage(getDamage());
			}
		}
		return CollisionType.NONE;
	}

	/**
	 * This method lets the projectile explode.
	 * 
	 * @return success
	 */
	public boolean explode() {
		if (!mIsExploding) {
			//Set the velocity to zero
			setVelocity(0, - Constants.GRAVITY_DOWN);
			//Create the explosion
			AnimationNode mExplosion = AnimationNode.createAnimationNode(TextureID.ANIM_EXPLOSION, 1000, false, 11,
					new Vector2i(266, 210));
			mExplosion.addAnimationListener(new AnimationListener() {

				@Override
				public void onFrame(AnimationNode node, int frame) {
					if (node.getNumFrames() - 1 == frame) {
						//Explosion finished destroy the node
						destroy();
					}

				}
			});
			setSprite(mExplosion);
			mExplosion.start();
			mIsExploding = true;
			return false;
		}
		return true;
	}
}
