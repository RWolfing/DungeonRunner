package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;

/**
 * A custom node that represents a spike obstacle in the game
 * that damages units.
 * 
 * @author Robert Wolfinger
 *
 */
public class Spikes extends SpriteNode {

	private final int DAMAGE = 100;
	private FloatRect mCollisionRect;

	/**
	 * Default constructor, creates the spike entity.
	 * 
	 * @param texture the texture to use
	 * @param props the properties of the node
	 */
	public Spikes(Sprite texture, Properties props) {
		super(texture, props);
		mCollisionRect = new FloatRect(new Vector2f(0, texture.getLocalBounds().height - 1),
				new Vector2f(texture.getLocalBounds().width, 10));
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		//If a unit collides with the spikes dmg it
		if (node.getBoundingRect().intersection(getBoundingRect()) != null && node instanceof Unit) {
			((Unit) node).damage(DAMAGE);
		}
		return CollisionType.NONE;
	}

	@Override
	public FloatRect getBoundingRect() {
		return getWorldTransform().transformRect(mCollisionRect);
	}
}
