package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.singleton.TextureHolder.TextureID;

public class GameUI extends Container{

	private Vector2i mWindowSize;
	private UILifebar mHPItem;
	private UILifebar mAmunitionItem;
	private UILifebar mDiamondItem;
	
	public GameUI(Vector2i windowSize){
		mWindowSize = windowSize;
		mHPItem = new UILifebar(TextureID.UI_LIFEBAR_BG, TextureID.UI_LIFEBAR);
		mAmunitionItem = new UILifebar(TextureID.UI_LIFEBAR_BG, TextureID.UI_LIFEBAR);
		mDiamondItem = new UILifebar(TextureID.UI_LIFEBAR_BG, TextureID.UI_LIFEBAR);
		layout(mWindowSize);
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		mHPItem.draw(target, states);
		mAmunitionItem.draw(target, states);
		//mDiamondItem.draw(target, states);
	}
	
	private void layout(Vector2i windowSize){
		int posX = windowSize.x / 10;
		mHPItem.setPosition(posX, 0);
		mAmunitionItem.setPosition(posX + mHPItem.getWidth(), 0);
		mDiamondItem.setPosition(windowSize.x - mDiamondItem.getWidth()-100, 0);
	}
	
	public UILifebar getLifeComponent(){
		return mHPItem;
	}

	
}
