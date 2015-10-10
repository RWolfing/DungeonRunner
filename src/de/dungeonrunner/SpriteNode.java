package de.dungeonrunner;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

import de.dungeonrunner.TextureHolder.TextureID;

public class SpriteNode extends SceneNode {

	private Sprite mSprite;
	
	public SpriteNode(Sprite sprite){
		mSprite = sprite;
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mSprite);
	}
}
