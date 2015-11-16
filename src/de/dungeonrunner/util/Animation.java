package de.dungeonrunner.util;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Transformable;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class Animation implements Drawable, Transformable {

	private Sprite mSprite;
	private Vector2i mFrameSize;
	private int mNumFrames;
	private int mCurrentFrame;
	private Time mDuration;
	private Time mElapsedTime;
	private boolean mRepeat;

	public Animation(TextureID textureID) {
		mSprite = new Sprite(TextureHolder.getInstance().getTexture(textureID));
		mElapsedTime = Time.ZERO;
	}

	public void update(Time dt) {
		Time timePerFrame = Time.div(mDuration, (float) mNumFrames);
		mElapsedTime = Time.add(mElapsedTime, dt);

		Vector2i textureBounds = mSprite.getTexture().getSize();
		IntRect textureRect = mSprite.getTextureRect();
		if (mCurrentFrame == 0) {
			textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
		}
		while (mElapsedTime.asMilliseconds() >= timePerFrame.asMilliseconds()
				&& (mCurrentFrame <= mNumFrames || mRepeat)) {
			int textureLeft = textureRect.left + textureRect.width;

			if (textureLeft + textureRect.width > textureBounds.x) {
				textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
			} else {
				textureRect = new IntRect(textureLeft, textureRect.top, textureRect.width, textureRect.height);
			}

			mElapsedTime = Time.sub(mElapsedTime, timePerFrame);
			if (mRepeat) {
				mCurrentFrame++;
				if (mCurrentFrame > mNumFrames) {
					mCurrentFrame = 0;
					textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
				}
			}
		}

		mSprite.setTextureRect(textureRect);
	}

	public FloatRect getGlobalBounds() {
		return mSprite.getGlobalBounds();
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		mSprite.draw(target, states);
	}

	public Vector2i getFrameSize() {
		return mFrameSize;
	}

	public void setFrameSize(Vector2i frameSize) {
		mFrameSize = frameSize;
	}

	public int getNumFrames() {
		return mNumFrames;
	}

	public void setNumFrames(int numFrames) {
		mNumFrames = numFrames;
	}

	public Time getDuration() {
		return mDuration;
	}

	public void setDuration(Time duration) {
		mDuration = duration;
	}

	public boolean isRepeat() {
		return mRepeat;
	}

	public void setRepeat(boolean repeat) {
		mRepeat = repeat;
	}

	@Override
	public Transform getInverseTransform() {
		return mSprite.getInverseTransform();
	}

	@Override
	public Vector2f getOrigin() {
		return mSprite.getOrigin();
	}

	@Override
	public Vector2f getPosition() {
		return mSprite.getPosition();
	}

	@Override
	public float getRotation() {
		return mSprite.getRotation();
	}

	@Override
	public Vector2f getScale() {
		return mSprite.getScale();
	}

	@Override
	public Transform getTransform() {
		return mSprite.getTransform();
	}

	@Override
	public void move(Vector2f v) {
		mSprite.move(v);

	}

	@Override
	public void move(float x, float y) {
		mSprite.move(x, y);

	}

	@Override
	public void rotate(float angle) {
		mSprite.rotate(angle);

	}

	@Override
	public void scale(Vector2f factors) {
		mSprite.scale(factors);

	}

	@Override
	public void scale(float x, float y) {
		mSprite.scale(x, y);

	}

	@Override
	public void setOrigin(Vector2f v) {
		mSprite.setOrigin(v);
	}

	@Override
	public void setOrigin(float x, float y) {
		mSprite.setOrigin(x, y);

	}

	@Override
	public void setPosition(Vector2f v) {
		mSprite.setPosition(v);

	}

	@Override
	public void setPosition(float x, float y) {
		mSprite.setPosition(x, y);

	}

	@Override
	public void setRotation(float angle) {
		mSprite.setRotation(angle);

	}

	@Override
	public void setScale(Vector2f factors) {
		mSprite.setScale(factors);

	}

	@Override
	public void setScale(float x, float y) {
		mSprite.setScale(x, y);

	}

}
