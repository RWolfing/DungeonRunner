package de.dungeonrunner.view;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;

/**
 * Component to represent the life of the player.
 * 
 * @author Robert Wolfinger
 *
 */
public class UILifebar extends Component {

	private final Vector2f mLifeBarPositionOffset = new Vector2f(76, 61);
	private Sprite mLifeBarBackground;
	private Sprite mLifeBar;
	private float mCurrLifeBar;

	/**
	 * Default constructor. 
	 * @param background
	 * @param lifebar
	 */
	public UILifebar(TextureID background, TextureID lifebar) {
		mLifeBarBackground = new Sprite(TextureHolder.getInstance().getTexture(background));
		setWidth(mLifeBarBackground.getLocalBounds().width);
		setHeight(mLifeBarBackground.getLocalBounds().height);
		mLifeBar = new Sprite(TextureHolder.getInstance().getTexture(lifebar));
		mLifeBar.setPosition(mLifeBarPositionOffset);
		mCurrLifeBar = 1.0f;
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		super.draw(target, states);
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		mLifeBarBackground.draw(target, states);
		mLifeBar.setScale(mCurrLifeBar, 1);
		mLifeBar.draw(target, states);
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		// Unused
	}

	/**
	 * Sets the health of the health bar in percent.
	 * 
	 * @param percentage the percentage of the health bar
	 */
	public void setHealthBar(float percentage) {
		if (percentage <= 0)
			percentage = 0.01f;
		mCurrLifeBar = percentage;
	}
}
