package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class PlayerUnit extends Unit {

	private RectangleShape mCollisionShape = new RectangleShape();
	
	public PlayerUnit(TextureID textureID) {
		super(textureID);
		mNodeType = NodeType.PLAYER;
		AnimationNode mIdleAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_IDLE, 1000, true, 4, new Vector2i(135, 135));
		AnimationNode mWalkAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_PLAYER_RUN, 1000, true, 12, new Vector2i(135, 135));
		AnimationNode mJumpAnimation = AnimationNode.createAnimationNode(TextureID.ANIM_PLAYER_JUMP, 1000, true, 5, new Vector2i(135, 135));
		setAnimation(mIdleAnimation, ANIM_ID.IDLE);
		setAnimation(mWalkAnimation, ANIM_ID.WALK);
		setAnimation(mJumpAnimation, ANIM_ID.JUMP);
		setCollisionRect(new FloatRect(27, 11, 62, 118));
		setProjectileSpawn(new Vector2f(0, 0));
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
		target.draw(mCollisionShape);
	}
}
