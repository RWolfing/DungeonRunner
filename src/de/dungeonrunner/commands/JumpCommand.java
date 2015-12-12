package de.dungeonrunner.commands;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.PlayerUnit;
import de.dungeonrunner.nodes.SceneNode;

public class JumpCommand extends SceneCommand{

	public JumpCommand(NodeType nodeType) {
		super(nodeType);
	}

	@Override
	public void execute(SceneNode sceneNode) {
		((PlayerUnit) sceneNode).jump();
	}

}
