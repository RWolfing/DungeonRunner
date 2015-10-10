package de.dungeonrunner;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;

public class PlayerEntity extends GameEntity {

	private Animation mIdleAnimation;

	public PlayerEntity(TextureID textureID) {
		mIdleAnimation = new Animation(textureID);
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setVelocity(new Vector2f(1.5f, 0));
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		mIdleAnimation.draw(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		mIdleAnimation.update(dt);
	}

}
