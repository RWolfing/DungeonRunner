package de.dungeonrunner.view;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Transform;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * Component to represent the available ammunition of the player.
 * 
 * @author Robert Wolfinger
 *
 */
public class UIAmmoBar extends Component {

	private final int mMaxAmmo = 5;
	//The starting point of the ammunition indicators
	private final int mAmmoXStart = 75;
	private final int mAmmoYStart = 61;
	private final int mAmmoIndicatorWidth = 21;

	private int mCurrentAmmo;

	private Sprite mBackgroundSprite;
	private List<Sprite> mAmmoSprites;

	/**
	 * Default constructor. 
	 */
	public UIAmmoBar() {
		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(TextureID.UI_AMMO));
		setWidth(mBackgroundSprite.getLocalBounds().width);
		setHeight(mBackgroundSprite.getLocalBounds().height);
		mAmmoSprites = new ArrayList<>();
		mCurrentAmmo = 0;
		createAmmo();
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		super.draw(target, states);
		states = new RenderStates(states, Transform.combine(getTransform(), states.transform));
		mBackgroundSprite.draw(target, states);
		for (int i = 0; i < mCurrentAmmo && i < mMaxAmmo; i++) {
			mAmmoSprites.get(i).draw(target, states);
		}
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		// Unused
	}

	/*
	 * Creates and positions the ammunition indicators in a row.
	 */
	private void createAmmo() {
		mAmmoSprites.clear();
		Sprite sprite;
		for (int i = 0; i < mMaxAmmo; i++) {
			sprite = new Sprite(TextureHolder.getInstance().getTexture(TextureID.UI_AMMO_INDICATOR));
			sprite.setPosition(mAmmoXStart + i * mAmmoIndicatorWidth, mAmmoYStart);
			mAmmoSprites.add(sprite);
		}
	}

	/**
	 * Sets the current ammunition.
	 * 
	 * @param ammo the ammunition
	 */
	public void setCurrentAmmo(int ammo) {
		mCurrentAmmo = ammo;
	}
	
	/**
	 * Retrieves the currently available ammunition.
	 * 
	 * @return the ammunition left
	 */
	public int getCurrentAmmo(){
		return mCurrentAmmo;
	}
	
	/**
	 * Checks if the maximum possible amount of ammunition
	 * is reached.
	 * 
	 * @return if the current ammo is the max ammo
	 */
	public boolean isMaxReached(){
		return mCurrentAmmo >= mMaxAmmo;
	}

	/**
	 * Increments the current available ammunition.
	 * 
	 * @return if the ammunition could be incremented or if the maximum
	 * ammount was reached
	 */
	public boolean incrementAmmo() {
		mCurrentAmmo++;
		if (mCurrentAmmo > mMaxAmmo) {
			mCurrentAmmo = mMaxAmmo;
			return false;
		}
		return true;
	}

	/**
	 * Decrements the current ammunition.
	 * 
	 * @return if the ammunition could be decremented or 0 is reached
	 */
	public boolean decrementAmmo() {
		mCurrentAmmo--;
		if (mCurrentAmmo < 0) {
			mCurrentAmmo = 0;
			return false;
		}
		return true;
	}
}
