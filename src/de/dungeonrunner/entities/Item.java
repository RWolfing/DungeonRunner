package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.Sprite;

import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class Item extends GameEntity{

	public Item(TextureID texID, Properties props) {
		super(props);
		
		SpriteNode sprite = new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(texID)), null);
		setSprite(sprite);
		setCollisionRect(sprite.getBoundingRect());
	}
}
