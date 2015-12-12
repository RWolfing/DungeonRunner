package de.dungeonrunner.entities;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class BulletProjectile extends GameEntity {

	private final Vector2f mVelocity = new Vector2f(200f, 0f);
	private GameEntity mShootingEntity;

	public BulletProjectile(GameEntity shooter, TextureID textureID) {
		super();
		mShootingEntity = shooter;
		mProperties.setProperty(Constants.PROJECTILE, "true");
		SpriteNode mSpriteNode = new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		setSprite(mSpriteNode);
		AnimationNode mAnimation = new AnimationNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		mAnimation.setDuration(Time.getMilliseconds(300));
		mAnimation.setRepeat(false);
		mAnimation.setNumFrames(3);
		mAnimation.setFrameSize(null);
		// setAnimation(mAnimation);
		setVelocity(mVelocity);
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (Boolean.valueOf(node.getProperty(Constants.BLOCK_VOLUME))
				&& !mShootingEntity.getSceneGraph().contains(node)) {
			if (node.getBoundingRect().intersection(getBoundingRect()) != null)
				destroy();
		}
	}
}
