package de.dungeonrunner.commands;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.PlayerUnit;
import de.dungeonrunner.nodes.SceneNode;

/**
 * This command lets the unit with the given
 * nodeType execute a shoot.
 * 
 * @author Robert Wolfinger
 *
 */
public class ShootCommand extends SceneCommand{

	public ShootCommand(NodeType nodeType) {
		super(nodeType);
	}

	@Override
	public void execute(SceneNode sceneNode) {
		((PlayerUnit) sceneNode).shoot();	
	}
}
