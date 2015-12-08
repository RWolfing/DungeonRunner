package de.dungeonrunner;

import de.dungeonrunner.nodes.SceneNode;

public abstract class SceneCommand{

	public NodeType mNodeType;
	
	public SceneCommand(NodeType nodeType){
		mNodeType = nodeType;
	}
	
	public abstract void execute(SceneNode sceneNode);
}
