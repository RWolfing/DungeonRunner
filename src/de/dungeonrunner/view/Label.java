package de.dungeonrunner.view;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class Label extends Component {

	private Font mFont;
	protected Text mTextDrawable;
	private Sprite mBackgroundSprite;
	private int mCharSize;
	private boolean mIsCentered;

	private Vector2f mOffsetText;

	public Label(String text) {
		mCharSize = 16;
		mFont = FontHolder.getInstance().getFont(FontID.DUNGEON_FONT);
		mTextDrawable = new Text(text, mFont, mCharSize);
		mIsCentered = false;
		mOffsetText = Vector2f.ZERO;
		computeOrigin();
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		mTextDrawable.setPosition(Vector2f.add(getOrigin(), mOffsetText));
		computeSize();
		
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		if (mBackgroundSprite != null) {
			target.draw(mBackgroundSprite, states);
		}
		target.draw(mTextDrawable, states);
	}

	public void setText(String text) {
		mTextDrawable.setString(text);
		computeOrigin();
	}

	public void setCharSize(int charSize) {
		mCharSize = charSize;
		mTextDrawable.setCharacterSize(mCharSize);
		computeOrigin();
	}

	public void setFont(FontID fontID) {
		mFont = FontHolder.getInstance().getFont(fontID);
		mTextDrawable.setFont(mFont);
		computeOrigin();
	}

	public void setBackground(TextureID texID) {
		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(texID));
	}

	public void setCentered(boolean isCentered) {
		mIsCentered = isCentered;
		computeOrigin();
	}

	public void offsetText(float x, float y) {
		mOffsetText = new Vector2f(x, y);
	}

	private void computeOrigin() {
		if (mIsCentered) {
			setOrigin(mWidth / 2, mHeight / 2);
			mTextDrawable.setOrigin(mTextDrawable.getLocalBounds().width / 2,
					mTextDrawable.getLocalBounds().height / 2);
		} else {
			setOrigin(0, 0);
		}
	}

	private void computeSize() {
		if (mBackgroundSprite != null) {
			float width = mBackgroundSprite.getLocalBounds().width;
			float height = mBackgroundSprite.getLocalBounds().height;
			mBackgroundSprite.setScale(mWidth / width, mHeight / height);
		}
	}
}
