package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class DynamitProjectile extends Projectile {

	private final Vector2f mVelocity = new Vector2f(200f, 0f);

	public DynamitProjectile(Unit shooter, TextureID textureID) {
		super(shooter, textureID);
		setVelocity(mVelocity);
		setCollisionRect(new FloatRect(0, 0, 66, 76));
		setDamage(50);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		getSprite().rotate(360 * dt.asSeconds());
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (checkIsBlocking(node)) {
			destroy();
			if (node instanceof Unit) {
				((Unit) node).damage(getDamage());
			}
		}
	}
}
