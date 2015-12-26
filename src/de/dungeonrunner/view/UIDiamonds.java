package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class UIDiamonds extends Component{

	private Sprite mBackground;
	private Text mText;
	private int mTotalDiamonds;
	private int mCurrentDiamonds;
	
	public UIDiamonds(TextureID texID, FontID font, int totalDiamonds) {
		mTotalDiamonds = totalDiamonds;
		mCurrentDiamonds = 0;
		
		mBackground = new Sprite(TextureHolder.getInstance().getTexture(texID));
		setWidth(mBackground.getLocalBounds().width);
		setHeight(mBackground.getLocalBounds().height);
		mText = new Text(mCurrentDiamonds + "/" + mTotalDiamonds, FontHolder.getInstance().getFont(font));
		mText.setPosition(64, 57);
		mText.setCharacterSize(18);
		
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		//Unused
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		super.draw(target, states);
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		mBackground.draw(target, states);
		mText.draw(target, states);
	}
	
	public boolean incrementDiamonds(){
		mCurrentDiamonds++;
		if(mCurrentDiamonds > mTotalDiamonds){
			mCurrentDiamonds = mTotalDiamonds;
			mText.setString(mCurrentDiamonds + "/" + mTotalDiamonds);
			return false;
		}
		mText.setString(mCurrentDiamonds + "/" + mTotalDiamonds);
		return true;
	}
	
	public boolean collectedAll(){
		return mCurrentDiamonds >= mTotalDiamonds;
	}
}
