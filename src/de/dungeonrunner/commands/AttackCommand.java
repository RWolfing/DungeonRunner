package de.dungeonrunner.commands;

import de.dungeonrunner.entities.Unit;
import de.dungeonrunner.nodes.NodeType;
import de.dungeonrunner.nodes.SceneNode;

/**
 * This command makes the Unit with the given nodeType execute 
 * a attack.
 * 
 * @author Robert Wolfinger
 *
 */
public class AttackCommand extends SceneCommand{

	public AttackCommand(NodeType nodeType) {
		super(nodeType);
	}

	@Override
	public void execute(SceneNode sceneNode) {
		((Unit) sceneNode).attack();
	}
}
