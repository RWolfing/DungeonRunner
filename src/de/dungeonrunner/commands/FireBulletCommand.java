package de.dungeonrunner.commands;

import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.DynamitProjectile;
import de.dungeonrunner.entities.GameEntity;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class FireBulletCommand extends SceneCommand{

	private Vector2f mSpawnPosition;
	private GameEntity mShooter;
	private int mXOrientation;
	
	public FireBulletCommand(GameEntity shooter, NodeType nodeType, Vector2f spawnPosition, int orientation) {
		super(nodeType);
		mSpawnPosition = spawnPosition;
		mShooter = shooter;
		mXOrientation = orientation;
	}

	@Override
	public void execute(SceneNode sceneNode) {
		DynamitProjectile projectile = new DynamitProjectile(mShooter, TextureID.BULLET);
		projectile.setPosition(mSpawnPosition);
		projectile.setVelocity(Vector2f.mul(projectile.getVelocity(), mXOrientation));
		sceneNode.attachChild(projectile);
	}
}
