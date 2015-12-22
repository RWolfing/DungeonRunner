package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Transform;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class Button extends Component {

	private TextureID mSelectedTexture;
	private TextureID mDefaultTexture;
	private TextureID mActivatedTexture;
	private Sprite mBackgroundSprite;
	private Label mTextLabel;

	private boolean mIsToggle;

	private OnButtonClick mOnClickListener;

	public Button(String text, TextureID inactiveTex) {
		mTextLabel = new Label(text);
		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(inactiveTex));
		mTextLabel.setPosition(mBackgroundSprite.getTexture().getSize().x / 2,
				mBackgroundSprite.getTexture().getSize().y / 2);
		mIsToggle = false;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		target.draw(mBackgroundSprite, states);
		target.draw(mTextLabel, states);
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	@Override
	public void select() {
		super.select();
		mBackgroundSprite.setTexture(TextureHolder.getInstance().getTexture(mSelectedTexture));
	}

	@Override
	public void deselect() {
		super.deselect();
		mBackgroundSprite.setTexture(TextureHolder.getInstance().getTexture(mDefaultTexture));
	}

	@Override
	public void activate() {
		super.activate();
		if (mIsToggle) {
			mBackgroundSprite.setTexture(TextureHolder.getInstance().getTexture(mActivatedTexture));
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
				mBackgroundSprite.setTexture(TextureHolder.getInstance().getTexture(mSelectedTexture));
			} else {
				mBackgroundSprite.setTexture(TextureHolder.getInstance().getTexture(mDefaultTexture));
			}
		}
	}

	public void setText(String text) {
		mTextLabel.setText(text);
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
