package de.dungeonrunner.entities;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class PlayerUnit extends Unit {

	private RectangleShape mCollisionShape = new RectangleShape();
	
	public PlayerUnit(TextureID textureID) {
		super(textureID);
		mNodeType = NodeType.PLAYER;
		AnimationNode mIdleAnimation = AnimationNode.createAnimationNode(textureID, 900, true, 6, new Vector2i(128, 128));
		setSprite(mIdleAnimation);
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mCollisionShape);
	}
}
