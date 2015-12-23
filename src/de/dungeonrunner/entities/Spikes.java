package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;

public class Spikes extends SpriteNode {

	private FloatRect mCollisionRect;
	private final int DAMAGE = 100;

	public Spikes(Sprite texture, Properties props) {
		super(texture, props);
		mCollisionRect = new FloatRect(new Vector2f(0, texture.getLocalBounds().height - 1),
				new Vector2f(texture.getLocalBounds().width, 10));
	}

	@Override
	protected void processCollision(SceneNode node) {
		super.processCollision(node);
		if (node.getBoundingRect().intersection(getBoundingRect()) != null && node instanceof Unit) {
			((Unit) node).damage(DAMAGE);
		}
	}

	@Override
	public FloatRect getBoundingRect() {
		return getWorldTransform().transformRect(mCollisionRect);
	}
}
