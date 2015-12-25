package de.dungeonrunner.entities;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.AnimationNode.AnimationListener;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class DynamitProjectile extends Projectile {

	private final Vector2f mVelocity = new Vector2f(200f, 0f);
	private boolean mIsExploding;

	public DynamitProjectile(Unit shooter, TextureID textureID) {
		super(shooter, textureID);
		setVelocity(mVelocity);
		setCollisionRect(new FloatRect(0, 0, 66, 76));
		setDamage(50);
		mIsExploding = false;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if (!mIsExploding) {
			getSprite().rotate(360 * dt.asSeconds());
		}
	}

	@Override
	protected void processCollision(SceneNode node) {
		if (checkIsBlocking(node) && !explode()) {
			if (node instanceof Unit) {
				((Unit) node).damage(getDamage());
			}
		}
	}

	public boolean explode() {
		if (!mIsExploding) {
			setVelocity(0, 0);
			AnimationNode mExplosion = AnimationNode.createAnimationNode(TextureID.ANIM_EXPLOSION, 1000, false, 11,
					new Vector2i(266, 210));
			mExplosion.addAnimationListener(new AnimationListener() {

				@Override
				public void onFrame(AnimationNode node, int frame) {
					if (node.getNumFrames() - 1 == frame) {
						destroy();
					}

				}
			});
			setSprite(mExplosion);
			mExplosion.start();
			mIsExploding = true;
			return false;
		}
		return true;
	}
}
