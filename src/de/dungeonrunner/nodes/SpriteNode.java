package de.dungeonrunner.nodes;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

public class SpriteNode extends SceneNode{

	protected Sprite mSprite;
	
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
	
	public FloatRect getBoundingRect(){
		return getWorldTransform().transformRect(mSprite.getGlobalBounds());
	}
}
