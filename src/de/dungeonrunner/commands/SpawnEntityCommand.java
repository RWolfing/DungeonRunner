package de.dungeonrunner.commands;

import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.Diamond;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class SpawnEntityCommand extends SceneCommand {

	private Vector2f mPosition;
	private Vector2f mVelocity;

	public SpawnEntityCommand(Vector2f position, Vector2f veloctiy, NodeType nodeType) {
		super(nodeType);
		mPosition = position;
		mVelocity = veloctiy;
		if (mVelocity == null) {
			mVelocity = Vector2f.ZERO;
		}
	}

	@Override
	public void execute(SceneNode sceneNode) {
		Diamond diamond = new Diamond(TextureID.DIAMOND, null);
		diamond.setPosition(mPosition);
		diamond.setVelocity(mVelocity);
		sceneNode.attachChild(diamond);
	}
}
