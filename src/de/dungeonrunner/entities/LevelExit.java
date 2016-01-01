package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

/**
 * A entity of the game that represents the level exit.
 * 
 * @author Robert Wolfinger
 *
 */
public class LevelExit extends GameEntity {

	//Is the exit door open or closed
	private boolean mIsOpen;
	private TextureID mTexExitClosed;
	private TextureID mTexExitOpen;

	private boolean mPlayerEntered;

	/**
	 * Default constructor, creates the LevelExit from the given parameters.
	 * 
	 * @param texIDOpen id of the texture for the open state
	 * @param texIDClosed id of the texture for the closed state
	 * @param props properties of the node
	 */
	public LevelExit(TextureID texIDOpen, TextureID texIDClosed, Properties props) {
		super(props);
		mTexExitClosed = texIDClosed;
		mTexExitOpen = texIDOpen;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(mTexExitClosed)), null));
		setVelocity(0, -Constants.GRAVITY_DOWN);
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		//Check if the exit is open and the player is in the door
		if (mIsOpen && node.getBoundingRect().intersection(getBoundingRect()) != null && node instanceof PlayerUnit) {
			mPlayerEntered = true;
		}
		return super.processCollision(node);
	}

	/**
	 * Opens the level exit
	 */
	public void open() {
		mIsOpen = true;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(mTexExitOpen)), null));
	}

	/**
	 * Checks if the player has entered the exit.
	 * 
	 * @return did player enter the exit
	 */
	public boolean didPlayerEnter() {
		return mPlayerEntered;
	}

	@Override
	public FloatRect getBoundingRect() {
		return getSprite().getBoundingRect();
	}
}
