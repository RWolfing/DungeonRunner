package de.dungeonrunner.view;

import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

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
	private boolean mIsActivated;

	private OnButtonClick mOnClickListener;

	private Object mTag;

	/**
	 * Default constructor. Creates a button with the given text and the given
	 * TextureID as the default texture.
	 * 
	 * @param text
	 *            the text of the button
	 * @param inactiveTex
	 *            the default texture
	 */
	public Button(String text, TextureID inactiveTex) {
		super(text);
		mIsToggle = false;
		mIsActivated = false;
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
		if (!(mIsToggle && mIsActivated))
			setBackground(mSelectedTexture);
	}

	@Override
	public void deselect() {
		super.deselect();
		if (!(mIsToggle && mIsActivated)){
			setBackground(mDefaultTexture);
		} 
	}

	@Override
	public void activate() {
		super.activate();
		if (mIsToggle && !mIsActivated) {
			mIsActivated = true;
			
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
		
		mIsActivated = false;
		if (isSelected()) {
			setBackground(mSelectedTexture);
		} else {
			setBackground(mDefaultTexture);
		}
	}

	/**
	 * Sets the texture for a selected state.
	 * 
	 * @param texID
	 *            the id of the texture
	 */
	public void setSelectedTexture(TextureID texID) {
		mSelectedTexture = texID;
	}

	/**
	 * Sets the texture for the activated state.
	 * 
	 * @param texID
	 *            the id of the texture
	 */
	public void setActiveTexture(TextureID texID) {
		mActivatedTexture = texID;
	}

	/**
	 * Sets if the button is toggled.
	 * 
	 * @param toggle
	 *            is toggled
	 */
	public void setToggle(boolean toggle) {
		mIsToggle = toggle;
	}

	/**
	 * Returns if the button is toogled and active or not.
	 * 
	 * @return is toggled
	 */
	public boolean isToggled() {
		return mIsActivated;
	}

	/**
	 * Sets the listener for clicks.
	 * 
	 * @param listener
	 *            click listener
	 */
	public void setOnClickListener(OnButtonClick listener) {
		mOnClickListener = listener;
	}

	/**
	 * Adds a tag to the button.
	 * 
	 * @param tag
	 *            the tag
	 */
	public void setTag(Object tag) {
		mTag = tag;
	}

	/**
	 * Returns the tag of the button.
	 * 
	 * @return the tag
	 */
	public Object getTag() {
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
	
	/**
	 * Convenience method to create a button with the default settings.
	 * 
	 * @param text text to display
	 * @return a new button
	 */
	public static Button createButton(String text){
		Button button = new Button(text, TextureID.BUTTON_DEFAULT);
		button.setSelectedTexture(TextureID.BUTTON_SELECTED);
		button.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		button.setWidth(Constants.MENU_ITEM_WIDTH);
		button.setHeight(Constants.MENU_ITEM_HEIGHT);
		button.setCentered(true);
		button.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		return button;
	}
}
