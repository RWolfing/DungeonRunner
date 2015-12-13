package de.dungeonrunner.commands;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.GameEntity;
import de.dungeonrunner.nodes.SceneNode;

public class MoveCommand extends SceneCommand{

	private int mDeltaX = 0;
	private int mDeltaY = 0;
	
	public MoveCommand(NodeType nodeType, int x, int y){
		super(nodeType);
		mDeltaX = x;
		mDeltaY = y;
	}
	
	@Override
	public void execute(SceneNode node) {
		if(node instanceof GameEntity){
			GameEntity entity = (GameEntity) node;
			entity.setVelocity(entity.getVelocity().x + mDeltaX, entity.getVelocity().y + mDeltaY);
		}
	}

}
