package de.dungeonrunner.commands;

import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.BulletProjectile;
import de.dungeonrunner.entities.GameEntity;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class FireBulletCommand extends SceneCommand{

	private Vector2f mSpawnPosition;
	private GameEntity mShooter;
	
	public FireBulletCommand(GameEntity shooter, NodeType nodeType, Vector2f spawnPosition) {
		super(nodeType);
		mSpawnPosition = spawnPosition;
		mShooter = shooter;
	}

	@Override
	public void execute(SceneNode sceneNode) {
		BulletProjectile projectile = new BulletProjectile(mShooter, TextureID.BULLET);
		projectile.setPosition(mSpawnPosition);
		sceneNode.attachChild(projectile);
	}
}
