package de.dungeonrunner.commands;

import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.entities.DynamitProjectile;
import de.dungeonrunner.entities.Projectile;
import de.dungeonrunner.entities.StoneProjectile;
import de.dungeonrunner.entities.Unit;
import de.dungeonrunner.entities.Projectile.ProjectileType;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class FireProjectileCommand extends SceneCommand {

	private Unit mShooter;
	private ProjectileType mType;

	public FireProjectileCommand(Unit shooter, NodeType nodeType, ProjectileType type) {
		super(nodeType);
		mShooter = shooter;
		mType = type;
	}

	@Override
	public void execute(SceneNode sceneNode) {
		Projectile projectile = null;
		switch (mType) {
		case Dynamite:
			projectile = new DynamitProjectile(mShooter, TextureID.DYNAMITE_SINGLE);
			break;
		case Stone:
			projectile = new StoneProjectile(mShooter, TextureID.STONE);
			break;
		}
		if (projectile != null) {
			projectile.setPosition(mShooter.getProjectileSpawn());
			projectile.setVelocity(Vector2f.mul(projectile.getVelocity(), mShooter.getOrientation().getValue()));
			sceneNode.attachChild(projectile);
		}
	}
}
