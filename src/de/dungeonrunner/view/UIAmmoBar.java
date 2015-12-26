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

public class UIAmmoBar extends Component {

	private final int mMaxAmmo = 5;
	private final int mAmmoXStart = 75;
	private final int mAmmoYStart = 61;
	private final int mAmmoIndicatorWidth = 21;

	private int mCurrentAmmo;

	private Sprite mBackgroundSprite;
	private List<Sprite> mAmmoSprites;

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

	private void createAmmo() {
		mAmmoSprites.clear();
		Sprite sprite;
		for (int i = 0; i < mMaxAmmo; i++) {
			sprite = new Sprite(TextureHolder.getInstance().getTexture(TextureID.UI_AMMO_INDICATOR));
			sprite.setPosition(mAmmoXStart + i * mAmmoIndicatorWidth, mAmmoYStart);
			mAmmoSprites.add(sprite);
		}
	}

	public void setCurrentAmmo(int ammo) {
		mCurrentAmmo = ammo;
	}

	public boolean incrementAmmo() {
		mCurrentAmmo++;
		if (mCurrentAmmo > mMaxAmmo) {
			mCurrentAmmo = mMaxAmmo;
			return false;
		}
		return true;
	}

	public boolean decrementAmmo() {
		mCurrentAmmo--;
		if (mCurrentAmmo < 0) {
			mCurrentAmmo = 0;
			return false;
		}
		return true;
	}

}
