package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class StoneProjectile extends Projectile {

	private final Vector2f mVelocity = new Vector2f(180f, Constants.GRAVITY_DOWN);

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
		if (checkIsBlocking(node)) {
			destroy();
			if (node instanceof Unit) {
				((Unit) node).damage(getDamage());
			}
		}
		return CollisionType.NONE;
	}

}
