package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class DynamitProjectile extends GameEntity {

	private final Vector2f mVelocity = new Vector2f(200f, 0f);
	private GameEntity mShootingEntity;

	public DynamitProjectile(GameEntity shooter, TextureID textureID) {
		super();
		mShootingEntity = shooter;
		mProperties.setProperty(Constants.PROJECTILE, "true");
		SpriteNode sprite = new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(TextureID.DYNAMITE_SINGLE)));
		sprite.setOrigin(33, 38);
		setSprite(sprite);
		setVelocity(mVelocity);
		setCollisionRect(new FloatRect(0, 0, 66, 76));
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		getSprite().rotate(360 * dt.asSeconds());
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (Boolean.valueOf(node.getProperty(Constants.BLOCK_VOLUME)) || Boolean.valueOf(node.getProperty(Constants.UNIT_VOLUME))
				&& !mShootingEntity.getSceneGraph().contains(node)) {
			if (node.getBoundingRect().intersection(getBoundingRect()) != null){
				destroy();
				if(node instanceof Unit){
					((Unit) node).damage(50);
				}
			}
		}
	}
}
