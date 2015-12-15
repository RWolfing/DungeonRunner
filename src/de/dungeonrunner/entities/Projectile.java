package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class Projectile extends GameEntity {

	public enum ProjectileType{
		Dynamite, Stone
	}
	
	private Unit mShootingUnit;
	private int mDamage;

	public Projectile(Unit shooter, TextureID textureID) {
		super();
		mShootingUnit = shooter;
		mProperties.setProperty(Constants.PROJECTILE, "true");
		SpriteNode sprite = new SpriteNode(
				new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		sprite.setOrigin(sprite.getBoundingRect().width / 2, sprite.getBoundingRect().height / 2);	
		setSprite(sprite);
		setVelocity(Vector2f.ZERO);
		setCollisionRect(FloatRect.EMPTY);
	}


	public Unit getShootingUnit() {
		return mShootingUnit;
	}

	public void setDamage(int damage) {
		mDamage = damage;
	}

	public int getDamage() {
		return mDamage;
	}

	public boolean checkIsBlocking(SceneNode node) {
		return (Boolean.valueOf(node.getProperty(Constants.BLOCK_VOLUME))
				|| Boolean.valueOf(node.getProperty(Constants.UNIT_VOLUME)))
						&& !getShootingUnit().getSceneGraph().contains(node) && node.getBoundingRect().intersection(getBoundingRect()) != null;
	}
}
