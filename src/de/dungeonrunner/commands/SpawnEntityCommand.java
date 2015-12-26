package de.dungeonrunner.commands;

import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.Diamond;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class SpawnEntityCommand extends SceneCommand{

	private Vector2f mPosition;
	
	public SpawnEntityCommand(Vector2f position, NodeType nodeType) {
		super(nodeType);
		mPosition = position;
	}

	@Override
	public void execute(SceneNode sceneNode) {
		Diamond diamond = new Diamond(TextureID.DIAMOND, null);
		diamond.setPosition(mPosition);
		sceneNode.attachChild(diamond);
	}

}
