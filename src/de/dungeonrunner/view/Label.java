package de.dungeonrunner.view;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;

public class Label extends Component {

	private Font mFont;
	protected Text mTextDrawable;
	private int mCharSize;
	private boolean mIsCentered;

	public Label(String text) {
		mCharSize = 16;
		mFont = FontHolder.getInstance().getFont(FontID.DUNGEON_FONT);
		mTextDrawable = new Text(text, mFont, mCharSize);
		mIsCentered = false;
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
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
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

	public void setCentered(boolean isCentered){
		mIsCentered = isCentered;
		computeOrigin();
	}
	
	private void computeOrigin() {
		if (mIsCentered) {
			setOrigin(mTextDrawable.getLocalBounds().width / 2, mTextDrawable.getLocalBounds().height / 2);
		} else {
			setOrigin(0, 0);
		}
	}
}
