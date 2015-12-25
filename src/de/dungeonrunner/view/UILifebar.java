package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class UILifebar extends Component{

	private final Vector2f mLifeBarPositionOffset = new Vector2f(93, 77);
	private Sprite mLifeBarBackground;
	private Sprite mLifeBar;
	private float mCurrLifeBar;
	
	public UILifebar(TextureID background, TextureID lifebar) {
		mLifeBarBackground = new Sprite(TextureHolder.getInstance().getTexture(background));
		setWidth(mLifeBarBackground.getLocalBounds().width);
		setHeight(mLifeBarBackground.getLocalBounds().height);
		mLifeBar = new Sprite(TextureHolder.getInstance().getTexture(lifebar));
		mCurrLifeBar = 1.0f;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		super.draw(target, states);
		mLifeBarBackground.draw(target, states);
		mLifeBar.setScale(mCurrLifeBar, 1);
		mLifeBar.draw(target, states);
	}

	@Override
	public boolean isSelectable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
	}

	public void setPosition(int x, int y){
		mLifeBarBackground.setPosition(x, y);
		mLifeBar.setPosition(x + mLifeBarPositionOffset.x, y + mLifeBarPositionOffset.y);
	}
	
	public void setHealthBar(float percentage){
		mCurrLifeBar = percentage / 100;
	}
}
