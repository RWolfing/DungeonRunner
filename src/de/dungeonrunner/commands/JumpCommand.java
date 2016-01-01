package de.dungeonrunner.commands;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.Unit;
import de.dungeonrunner.nodes.SceneNode;

/**
 * This command makes the Unit with the given nodeType execute
 * a jump.
 * 
 * @author Robert Wolfinger
 *
 */
public class JumpCommand extends SceneCommand{

	public JumpCommand(NodeType nodeType) {
		super(nodeType);
	}

	@Override
	public void execute(SceneNode sceneNode) {
		((Unit) sceneNode).jump();
	}
}
