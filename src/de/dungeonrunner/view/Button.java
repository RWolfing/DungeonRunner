package de.dungeonrunner.view;

import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class Button extends Label {

	private TextureID mSelectedTexture;
	private TextureID mDefaultTexture;
	private TextureID mActivatedTexture;

	private boolean mIsToggle;

	private OnButtonClick mOnClickListener;

	public Button(String text, TextureID inactiveTex) {
		super(text);
		mIsToggle = false;
		mDefaultTexture = inactiveTex;
		setBackground(mDefaultTexture);
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

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

	public void setSelectedTexture(TextureID texID) {
		mSelectedTexture = texID;
	}

	public void setActiveTexture(TextureID texID) {
		mActivatedTexture = texID;
	}

	public void setToggle(boolean toggle) {
		mIsToggle = toggle;
	}

	public void setOnClickListener(OnButtonClick listener) {
		mOnClickListener = listener;
	}

	public interface OnButtonClick {
		void onClick(Button bttn);
	}
}
