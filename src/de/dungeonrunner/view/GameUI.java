package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * This container hold the ui of the game world. It contains and handles the different ui items.
 * 
 * @author Robert Wolfinger
 *
 */
public class GameUI extends Container{

	private Vector2i mWindowSize;
	private UILifebar mHPItem;
	private UIAmmoBar mAmunitionItem;
	private UIDiamonds mDiamondItem;
	
	public GameUI(Vector2i windowSize){
		mWindowSize = windowSize;
		mHPItem = new UILifebar(TextureID.UI_LIFEBAR_BG, TextureID.UI_LIFEBAR);
		mAmunitionItem = new UIAmmoBar();
		mDiamondItem = new UIDiamonds(TextureID.UI_DIAMOND, FontID.DUNGEON_FONT, 10);
		layout(mWindowSize);
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		mHPItem.draw(target, states);
		mAmunitionItem.draw(target, states);
		mDiamondItem.draw(target, states);
	}
	
	/**
	 * This method layouts the ui components depending on the window size.
	 * 
	 * @param windowSize the size of the window
	 */
	private void layout(Vector2i windowSize){
		int posX = windowSize.x / 10;
		mHPItem.setPosition(posX, 0);
		mAmunitionItem.setPosition(posX + mHPItem.getWidth(), 0);
		mDiamondItem.setPosition(posX + mHPItem.getWidth() + mAmunitionItem.getWidth(), 0);
	}
	
	/**
	 * Returns the component holding the lifebar of the player.
	 * 
	 * @return the UILifebar
	 */
	public UILifebar getLifeComponent(){
		return mHPItem;
	}
	
	/**
	 * Returns the component holding the ammunition of the player.
	 * 
	 * @return the UIAmmoBar
	 */
	public UIAmmoBar getAmmoComponent(){
		return mAmunitionItem;
	}
	
	/**
	 * Returns the component holding the collected diamonds of the player.
	 * 
	 * @return the UIDiamonds
	 */
	public UIDiamonds getDiamondsComponent(){
		return mDiamondItem;
	}

	
}
