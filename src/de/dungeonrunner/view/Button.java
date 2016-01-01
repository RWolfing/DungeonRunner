package de.dungeonrunner.view;

import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * A component to represent a button. 
 * 
 * @author Robert Wolfinger
 *
 */
public class Button extends Label {

	private TextureID mSelectedTexture;
	private TextureID mDefaultTexture;
	private TextureID mActivatedTexture;

	private boolean mIsToggle;

	private OnButtonClick mOnClickListener;
	
	private Object mTag;

	/**
	 * Default constructor. Creates a button with the given text and the given TextureID
	 * as the default texture.
	 * 
	 * @param text the text of the button
	 * @param inactiveTex the default texture
	 */
	public Button(String text, TextureID inactiveTex) {
		super(text);
		mIsToggle = false;
		mDefaultTexture = inactiveTex;
		setBackground(mDefaultTexture);
	}

	@Override
	public void handleEvent(Event event) {
		// Unused
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	@Override
	public void select() {
		super.select();
		setBackground(mSelectedTexture);
	}

	@Override
	public void deselect() {
		super.deselect();
		setBackground(mDefaultTexture);
	}

	@Override
	public void activate() {
		super.activate();
		if (mIsToggle) {
			setBackground(mActivatedTexture);
		} else {
			deactivate();
		}
		if (mOnClickListener != null) {
			mOnClickListener.onClick(this);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();

		if (mIsToggle) {
			if (isSelected()) {
				setBackground(mSelectedTexture);
			} else {
				setBackground(mDefaultTexture);
			}
		}
	}

	/**
	 * Sets the texture for a selected state.
	 * 
	 * @param texID the id of the texture
	 */
	public void setSelectedTexture(TextureID texID) {
		mSelectedTexture = texID;
	}

	/**
	 * Sets the texture for the activated state.
	 * 
	 * @param texID the id of the texture
	 */
	public void setActiveTexture(TextureID texID) {
		mActivatedTexture = texID;
	}

	/**
	 * Sets if the button is toggled.
	 * 
	 * @param toggle is toggled
	 */
	public void setToggle(boolean toggle) {
		mIsToggle = toggle;
	}

	/**
	 * Sets the listener for clicks.
	 * @param listener click listener
	 */
	public void setOnClickListener(OnButtonClick listener) {
		mOnClickListener = listener;
	}
	
	/**
	 * Adds a tag to the button.
	 * 
	 * @param tag the tag
	 */
	public void setTag(Object tag){
		mTag = tag;
	}
	
	/**
	 * Returns the tag of the button.
	 * 
	 * @return the tag
	 */
	public Object getTag(){
		return mTag;
	}

	/**
	 * Interface to check for clicks of the button component.
	 * 
	 * @author Robert Wolfinger
	 *
	 */
	public interface OnButtonClick {
		void onClick(Button bttn);
	}
}
