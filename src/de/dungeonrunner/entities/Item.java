package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * A item entity in the game. It only contains the used sprite, collision recangle
 * and properties.
 * 
 * @author Robert Wolfinger
 *
 */
public class Item extends GameEntity{

	private boolean mIsPickedUp;
	
	public Item(TextureID texID, Properties props) {
		super(props);
		SpriteNode sprite = new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(texID)), null);
		setSprite(sprite);
		setCollisionRect(sprite.getBoundingRect());
		mIsPickedUp = false;
	}
	
	@Override
	protected CollisionType processCollision(SceneNode node) {
		//Check if the diamond collided with the player or with the environment and
		//handle the collision accordingly
		FloatRect intersection = node.getBoundingRect().intersection(getBoundingRect());
		if (intersection != null) {
			if (node instanceof PlayerUnit && pickUpCondition()) {
				mIsPickedUp = true;
			}
		}
		CollisionType type = super.processCollision(node);
		if (type == CollisionType.BOTTOM) {
			setVelocity(0, 0);
		}
		if (type == CollisionType.TOP) {
			setVelocity(getVelocity().x, 0);
		}
		return type;
	}

	/**
	 * Checks if the dimaond was picked up by the player.
	 * 
	 * @return was the diamond picked up
	 */
	public boolean isPickedUp() {
		return mIsPickedUp;
	}
	
	public boolean pickUpCondition(){
		return true;
	}
}
