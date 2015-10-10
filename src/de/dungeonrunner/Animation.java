package de.dungeonrunner;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.TextureHolder.TextureID;

public class Animation implements Drawable {

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
			
			if(textureLeft + textureRect.width > textureBounds.x){
				textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
			} else {
				textureRect = new IntRect(textureLeft, textureRect.top, textureRect.width, textureRect.height);
			}
			
			mElapsedTime = Time.sub(mElapsedTime, timePerFrame);
			if (mRepeat) {
				mCurrentFrame++;
				if(mCurrentFrame > mNumFrames){
					mCurrentFrame = 0;
					textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
				}
			}		
		}

		mSprite.setTextureRect(textureRect);
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(mSprite);
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

}
