package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

/**
 * This class represents the base class for any projectiles in the game.
 * 
 * @author Robert Wolfinger
 *
 */
public class Projectile extends GameEntity {

	/**
	 * Enum that contains the available projectile types
	 * 
	 * @author Robert Wolfinger
	 *
	 */
	public enum ProjectileType {
		Dynamite, Stone
	}

	private Unit mShootingUnit;
	private int mDamage;

	/**
	 * Default constructor, creates a projectile with the given parameters.
	 * 
	 * @param shooter  the unit shooting the projectile
	 * @param textureID the id of the texture to use
	 */
	public Projectile(Unit shooter, TextureID textureID) {
		super(null);
		mShootingUnit = shooter;
		mPropertySet.put(Constants.PROJECTILE, "true");
		SpriteNode sprite = new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)), null);
		sprite.setOrigin(sprite.getBoundingRect().width / 2, sprite.getBoundingRect().height / 2);
		setSprite(sprite);
		setVelocity(Vector2f.ZERO);
		setCollisionRect(FloatRect.EMPTY);
	}

	/**
	 * Returns the unit shooting the projectile.
	 * 
	 * @return the shooting unit
	 */
	public Unit getShootingUnit() {
		return mShootingUnit;
	}

	/**
	 * Sets the damage the projectile should inflict.
	 * 
	 * @param damage the damage
	 */
	public void setDamage(int damage) {
		mDamage = damage;
	}

	/**
	 * Returns the damage of the projectile.
	 * 
	 * @return the damage
	 */
	public int getDamage() {
		return mDamage;
	}

	/**
	 * This method checks if the given node should be computed as a collision with
	 * the projectile.
	 * 
	 * @param node the node to check
	 * @return is the node a collision
	 */
	public boolean checkIsBlocking(SceneNode node) {
		return (Boolean.valueOf(node.getProperty(Constants.BLOCK_VOLUME))
				|| Boolean.valueOf(node.getProperty(Constants.UNIT_VOLUME)))
				&& !getShootingUnit().getSceneGraph().contains(node)
				&& node.getBoundingRect().intersection(getBoundingRect()) != null;
	}
}
