package de.dungeonrunner.commands;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.Unit;
import de.dungeonrunner.nodes.SceneNode;

public class AttackCommand extends SceneCommand{

	public AttackCommand(NodeType nodeType) {
		super(nodeType);
	}

	@Override
	public void execute(SceneNode sceneNode) {
		((Unit) sceneNode).attack();
	}

}
