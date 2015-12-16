package de.dungeonrunner.nodes;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.entities.GameEntity.ORIENTATION;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

//TODO Remember AnimationNode Origin is the middle
public class AnimationNode extends SpriteNode {

	private Vector2i mFrameSize;
	private int mNumFrames;
	private int mCurrentFrame;
	private Time mDuration;
	private Time mElapsedTime;
	private boolean mRepeat;
	private boolean mIsRunning;

	private ORIENTATION mOrientation;
	private AnimationListener mAnimationListener;

	public AnimationNode(Sprite sprite) {
		super(sprite, null);
		mElapsedTime = Time.ZERO;
		mIsRunning = false;
		mOrientation = ORIENTATION.RIGHT;
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		if (isRunning()) {
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
				if (mAnimationListener != null) {
					mAnimationListener.onFrame(this, mCurrentFrame);
				}
				mCurrentFrame++;

				if (mRepeat) {
					if (mCurrentFrame > mNumFrames) {
						mCurrentFrame = 0;
						textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
					}
				}
				if (mCurrentFrame == mNumFrames && !mRepeat) {
					stop();
					return;
				}
			}
			mSprite.setTextureRect(
					new IntRect(textureRect.left, textureRect.top, textureRect.width, textureRect.height));

		}
	}

	public void setOrientation(ORIENTATION orientation) {
		if (mOrientation != orientation) {
			mOrientation = orientation;
			mirror();
		}
	}

	public void mirror() {
		mSprite.scale(-1, 1);
	}

	public void start() {
		if (!isRunning()) {
			mIsRunning = true;
			mCurrentFrame = 0;
			mElapsedTime = Time.ZERO;
		}
	}

	public void stop() {
		mIsRunning = false;
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public Vector2i getFrameSize() {
		return mFrameSize;
	}

	public void setFrameSize(Vector2i frameSize) {
		mFrameSize = frameSize;
		if (mFrameSize != null)
			mSprite.setOrigin(frameSize.x / 2, frameSize.y / 2);
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

	public void setAnimationListener(AnimationListener listener) {
		mAnimationListener = listener;
	}

	public static AnimationNode createAnimationNode(TextureID textureID, long duration, boolean repeat, int numFrames,
			Vector2i frameSize) {
		Sprite sprite = new Sprite(TextureHolder.getInstance().getTexture(textureID));
		AnimationNode animationNode = new AnimationNode(sprite);
		animationNode.setDuration(Time.getMilliseconds(duration));
		animationNode.setRepeat(repeat);
		animationNode.setNumFrames(numFrames);
		animationNode.setFrameSize(frameSize);
		return animationNode;
	}

	public interface AnimationListener {
		void onFrame(AnimationNode node, int frame);
	}
}
