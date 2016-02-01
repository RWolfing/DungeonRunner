package de.dungeonrunner.commands;

import de.dungeonrunner.nodes.NodeType;
import de.dungeonrunner.nodes.SceneNode;

/**
 * This command is the base for every command that can be used
 * in the game.
 * 
 * @author Robert Wolfinger
 *
 */
public abstract class SceneCommand{

	//The type of the SceneNode that should execute this command
	public NodeType mNodeType;
	
	public SceneCommand(NodeType nodeType){
		mNodeType = nodeType;
	}
	
	public abstract void execute(SceneNode sceneNode);
}
