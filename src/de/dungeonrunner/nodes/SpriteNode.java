package de.dungeonrunner.nodes;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

public class SpriteNode extends SceneNode{

	protected Sprite mSprite;
	
	public SpriteNode(Sprite sprite){
		mSprite = sprite;
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
