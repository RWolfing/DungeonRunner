package de.dungeonrunner.nodes;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.entities.GameEntity.ORIENTATION;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * A SceneNode which holds and manages an animation from a given Sprite. Note:
 * The origin of this node is by default the middle of the animation.
 * 
 * @author Robert Wolfinger
 *
 */
public class AnimationNode extends SpriteNode {

	// The size of a single frame of the animation
	private Vector2i mFrameSize;
	// The number of frames of the animation
	private int mNumFrames;
	// The current frame of the animation
	private int mCurrentFrame;
	// The duration of the animation
	private Time mDuration;
	// The elapsed time of the animation
	private Time mElapsedTime;
	// Should the animation be repeated in an infinite loop
	private boolean mRepeat;
	// Is the animation running
	private boolean mIsRunning;

	// The orientation of the animation LEFT | RIGHT
	private ORIENTATION mOrientation;

	// Listeners that should get notified about the animation state
	private List<AnimationListener> mAnimationListener;

	/**
	 * Default constructor, creates a new animation from the given Sprite.
	 * 
	 * @param sprite
	 *            the sprite
	 */
	public AnimationNode(Sprite sprite) {
		super(sprite, null);
		// Initial setup
		mElapsedTime = Time.ZERO;
		mIsRunning = false;
		mOrientation = ORIENTATION.RIGHT;
		mNumFrames = 0;
		mCurrentFrame = 0;
		mAnimationListener = new ArrayList<>();
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
		// If the animation is running we have to update the sprite
		if (isRunning()) {
			// Compute how much time has elapsed and how much time we have for
			// every frame
			Time timePerFrame = Time.div(mDuration, (float) mNumFrames);
			mElapsedTime = Time.add(mElapsedTime, dt);

			// Get the size of the sprite and the rectangle that should be
			// showed
			Vector2i textureBounds = mSprite.getTexture().getSize();
			// This is the actual rectangle we want to show
			IntRect textureRect = mSprite.getTextureRect();

			if (mCurrentFrame == 0) {
				// If we are at the first frame, set the textureRect to the
				// start of the sprite
				textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
			}
			// While the elapsed time exceeds the time per frame, the end of the
			// animation was not reached and we dont want to repeat it
			while (mElapsedTime.asMilliseconds() >= timePerFrame.asMilliseconds() && (mCurrentFrame <= mNumFrames)) {
				// Move the textureRect to the next frame
				int textureLeft = textureRect.left + textureRect.width;

				if (textureLeft + textureRect.width > textureBounds.x) {
					if (mRepeat) {
						mCurrentFrame = -1;
						textureRect = new IntRect(0, 0, mFrameSize.x, mFrameSize.y);
					} else {
						stop();
						notifyListener(mCurrentFrame);
						break;
					}
				} else {
					textureRect = new IntRect(textureLeft, textureRect.top, textureRect.width, textureRect.height);
				}
				
				notifyListener(mCurrentFrame);
				// We are showing a new frame, so we have to substract the timer
				// per frame from the elapsed time
				mElapsedTime = Time.sub(mElapsedTime, timePerFrame);
				// Increment the current frame counter
				mCurrentFrame++;
			}
			// Set the rectangle of the frame we want to show to the computes
			// position
			mSprite.setTextureRect(
					new IntRect(textureRect.left, textureRect.top, textureRect.width, textureRect.height));
		}
	}

	/**
	 * Sets the orientation of the animation.
	 * 
	 * @param orientation
	 *            the orientation
	 */
	public void setOrientation(ORIENTATION orientation) {
		if (mOrientation != orientation) {
			// if the orientation changed we mirror the image of the sprite
			mOrientation = orientation;
			mirror();
		}
	}

	/**
	 * Mirrors the image of the sprite by scaling it to -1 in x.
	 */
	public void mirror() {
		mSprite.scale(-1, 1);
	}

	/**
	 * Starts the animation if it is not already running.
	 */
	public void start() {
		if (!isRunning()) {
			// Reset the values
			mIsRunning = true;
			mCurrentFrame = 0;
			mElapsedTime = Time.ZERO;
		}
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		mIsRunning = false;
	}

	/**
	 * Returns if the animation is currently running.
	 * 
	 * @return is the animation running
	 */
	public boolean isRunning() {
		return mIsRunning;
	}

	/**
	 * Returns the size of one frame of the animation.
	 * 
	 * @return the frame size
	 */
	public Vector2i getFrameSize() {
		return mFrameSize;
	}

	/**
	 * Sets the size of one frame of the animation.
	 * 
	 * @param frameSize
	 *            the frame size
	 */
	public void setFrameSize(Vector2i frameSize) {
		mFrameSize = frameSize;
		if (mFrameSize != null)
			mSprite.setOrigin(frameSize.x / 2, frameSize.y / 2);
	}

	/**
	 * Returns the number of frames of the animation.
	 * 
	 * @return the number of frames
	 */
	public int getNumFrames() {
		return mNumFrames;
	}

	/**
	 * Sets the number of frames of the animation.
	 * 
	 * @param numFrames
	 *            the number of frames
	 */
	public void setNumFrames(int numFrames) {
		mNumFrames = numFrames;
	}

	/**
	 * Returns the duration of one cylce of the animation.
	 * 
	 * @return the duration of the animation
	 */
	public Time getDuration() {
		return mDuration;
	}

	/**
	 * Sets the duration of the animation.
	 * 
	 * @param duration
	 *            the duration of the animation
	 */
	public void setDuration(Time duration) {
		mDuration = duration;
	}

	/**
	 * Returns if the animation is currently set to repeat.
	 * 
	 * @return should the animation be repeated
	 */
	public boolean isRepeat() {
		return mRepeat;
	}

	/**
	 * Sets if the animation should be repeated.
	 * 
	 * @param repeat
	 *            should the animation be repeated
	 */
	public void setRepeat(boolean repeat) {
		mRepeat = repeat;
	}

	/**
	 * Adds an AnimationListener to this animation.
	 * 
	 * @param listener
	 *            a animation listener
	 */
	public void addAnimationListener(AnimationListener listener) {
		mAnimationListener.add(listener);
	}

	/**
	 * Notifies all registered listener about the current frame of the
	 * animation.
	 * 
	 * @param frame
	 *            the frame of the animation
	 */
	private void notifyListener(int frame) {
		for (AnimationListener listener : mAnimationListener) {
			listener.onFrame(this, frame);
		}
	}

	/**
	 * Helper method to create a AnimationNode with the given parameters.
	 * 
	 * @param textureID
	 *            the id of the texture to use as a sprite
	 * @param duration
	 *            the duration of the animation
	 * @param repeat
	 *            should the animation be repeated
	 * @param numFrames
	 *            the number of frames of the animation
	 * @param frameSize
	 *            the size of an animation frame
	 * @return the created animation node
	 */
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

	/**
	 * Interface to get notified about the current frame of a AnimationNode.
	 */
	public interface AnimationListener {
		void onFrame(AnimationNode node, int frame);
	}
}
