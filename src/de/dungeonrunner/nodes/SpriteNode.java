package de.dungeonrunner.nodes;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

/**
 * A SceneNode which can hold and manage a Sprite.
 * 
 * @author Robert Wolfinger
 *
 */
public class SpriteNode extends SceneNode{

	protected Sprite mSprite;
	
	/**
	 * Default constructor, creates the SpriteNode from
	 * the given parameters.
	 * 
	 * @param sprite the sprite of the node
	 * @param props the node properties
	 */
	public SpriteNode(Sprite sprite, Properties props){
		super(props);
		mSprite = sprite;
		mColor = Color.GREEN;
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mSprite, states);
	}
	
	@Override
	public FloatRect getBoundingRect(){
		//As the bounding rectangle of the node, we want to return the bounds of the sprite
		return getWorldTransform().transformRect(mSprite.getGlobalBounds());
	}
}
