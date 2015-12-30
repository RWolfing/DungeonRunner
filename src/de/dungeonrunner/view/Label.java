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

/**
 * A simple label component.
 * 
 * @author Robert Wolfinger
 *
 */
public class Label extends Component {

	//The font of the text
	private Font mFont;
	//The text of the label
	protected Text mTextDrawable;
	//The background of the background
	private Sprite mBackgroundSprite;
	//The character size of the text
	private int mCharSize;
	//Sets if the text should be displayed in the center of the label
	private boolean mIsCentered;

	//Offset of the text
	private Vector2f mOffsetText;

	/**
	 * Default constructor, creates a new label with the given text.
	 * 
	 * @param text the text of the label
	 */
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
		// Unused
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

	/**
	 * Sets the text of the label.
	 * 
	 * @param text the text of label
	 */
	public void setText(String text) {
		mTextDrawable.setString(text);
		computeOrigin();
	}

	/**
	 * Sets the character size of the label.
	 * 
	 * @param charSize the character size of the label
	 */
	public void setCharSize(int charSize) {
		mCharSize = charSize;
		mTextDrawable.setCharacterSize(mCharSize);
		computeOrigin();
	}

	/**
	 * Sets the font of the label.
	 * 
	 * @param fontID the id of the font
	 */
	public void setFont(FontID fontID) {
		mFont = FontHolder.getInstance().getFont(fontID);
		mTextDrawable.setFont(mFont);
		computeOrigin();
	}

	/**
	 * Sets the background of the label.
	 * 
	 * @param texID the id of the texture to set as the background
	 */
	public void setBackground(TextureID texID) {
		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(texID));
	}

	/**
	 * Sets if the text should be centered
	 * 
	 * @param isCentered
	 */
	public void setCentered(boolean isCentered) {
		mIsCentered = isCentered;
		computeOrigin();
	}

	/**
	 * Sets the offset of the text.
	 * 
	 * @param x x-offset
	 * @param y y-offset
	 */
	public void offsetText(float x, float y) {
		mOffsetText = new Vector2f(x, y);
	}
	
	/**
	 * Sets the offset of the text.
	 * 
	 * @param offset offset of the text
	 */
	public void offsetText(Vector2f offset){
		mOffsetText = offset;
	}

	/**
	 * Computes the origin of the label. If the text is centered
	 * the origin will be set to the center of the label.
	 */
	private void computeOrigin() {
		if (mIsCentered) {
			setOrigin(mWidth / 2, mHeight / 2);
			mTextDrawable.setOrigin(mTextDrawable.getLocalBounds().width / 2,
					mTextDrawable.getLocalBounds().height / 2);
		} else {
			setOrigin(0, 0);
		}
	}

	/**
	 * Computes the size of the label. If a background exists
	 * the size should be the size of the background texture.
	 */
	private void computeSize() {
		if (mBackgroundSprite != null) {
			float width = mBackgroundSprite.getLocalBounds().width;
			float height = mBackgroundSprite.getLocalBounds().height;
			mBackgroundSprite.setScale(mWidth / width, mHeight / height);
		}
	}
}
