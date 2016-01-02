package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

/**
 * A custom stone projectile.
 * 
 * @author Robert Wolfinger
 *
 */
public class StoneProjectile extends Projectile {

	//The velocity of the projectile (add the gravity to it to make in fly in a straight line)
	private final Vector2f mVelocity = new Vector2f(180f, -Constants.GRAVITY_DOWN);

	/**
	 * Default constructor, creates the projectile from the given parameters.
	 * 
	 * @param shooter the unit that is shooting the projectile
	 * @param textureID the id of the texture to use
	 */
	public StoneProjectile(Unit shooter, TextureID textureID) {
		super(shooter, textureID);
		setVelocity(mVelocity);
		setCollisionRect(new FloatRect(0f, 0f, 48f, 50f));
		setDamage(10);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		getSprite().rotate(360 * dt.asSeconds());
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		//Check if the projectile collides with a unit that this
		//projectile can damage
		if (checkIsBlocking(node)) {
			destroy();
			if (node instanceof PlayerUnit) {
				((PlayerUnit) node).damage(getDamage());
			}
		}
		return CollisionType.NONE;
	}
}
