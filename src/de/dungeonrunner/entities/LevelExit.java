package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;

public class LevelExit extends GameEntity {

	private boolean mIsOpen;
	private TextureID mTexExitClosed;
	private TextureID mTexExitOpen;

	private boolean mPlayerEntered;

	public LevelExit(TextureID texIDOpen, TextureID texIDClosed, Properties props) {
		super(props);
		mTexExitClosed = texIDClosed;
		mTexExitOpen = texIDOpen;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(mTexExitClosed)), null));
		setVelocity(0, -Constants.GRAVITY_DOWN);
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		if (mIsOpen && node.getBoundingRect().intersection(getBoundingRect()) != null && node instanceof PlayerUnit) {
			mPlayerEntered = true;
		}
		return super.processCollision(node);
	}

	public void open() {
		mIsOpen = true;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(mTexExitOpen)), null));
	}

	public boolean didPlayerEnter() {
		return mPlayerEntered;
	}

	@Override
	public FloatRect getBoundingRect() {
		return getSprite().getBoundingRect();
	}
}
