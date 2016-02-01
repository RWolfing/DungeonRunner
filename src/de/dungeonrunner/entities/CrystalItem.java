package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.commands.SpawnEntityCommand;
import de.dungeonrunner.nodes.NodeType;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.util.TmxKeys;
import de.dungeonrunner.view.LifeBar;
import tiled.core.MapObject;

/**
 * This class represents a crystal item in the game.
 * The player has to mine it in order to receive diamonds.
 * 
 * @author Robert Wolfinger
 *
 */
public class CrystalItem extends Item {

	/**
	 * Enum for the different types of crystals available.
	 */
	public enum Type {
		SMALL, NORMAL, BIG;
		public static Type getType(String type) {
			if (type == null) {
				return null;
			}

			if (type.equals("big")) {
				return BIG;
			} else if (type.equals("normal")) {
				return NORMAL;
			} else {
				return Type.SMALL;
			}
		}
	}

	//Diamonds that the crystal contains
	private int mTotalDiamonds;
	private int mContainingDiamonds;
	
	//Lifebar of the crystal
	private LifeBar mLifeBar;
	
	//Texture for the crystal if it was successfully mined
	private TextureID mMinedTexture;
	private boolean mWasMined;

	/**
	 * Default constructor, creates a new crystal with the 
	 * given parameters.
	 * 
	 * @param texID id of the texture to use
	 * @param props properties of the nodes
	 */
	public CrystalItem(TextureID texID, Properties props) {
		super(texID, props);
		mLifeBar = new LifeBar();
		mLifeBar.setDimension(new Vector2f(getBoundingRect().width, 5f));
		mLifeBar.setColor(Color.GREEN);
		mLifeBar.setPosition(0, -10);
		attachChild(mLifeBar);
	}

	/**
	 * Mines the crystal
	 */
	public void mine() {
		if (!mWasMined) {
			//decrement the containing crystals
			mContainingDiamonds--;
			if (mContainingDiamonds < 1) {
				//if no crystals are left we can destroy the node
				destroy();
			}
			//Update the lifebar
			mLifeBar.setCurrent(mContainingDiamonds);
		}
	}

	/**
	 * Sets the amount of diamonds that the crystal contains.
	 * 
	 * @param diamonds the amount of diamonds
	 */
	public void setContainingDiamonds(int diamonds) {
		mTotalDiamonds = mContainingDiamonds = diamonds;
		mLifeBar.setCurrent(mContainingDiamonds);
		mLifeBar.setMaximum(mContainingDiamonds);
	}

	@Override
	public void destroy() {
		//If the node was destroyed we change to sprite to the mined texture
		//and spawn all containing diamonds
		mWasMined = true;
		setSprite(new SpriteNode(new Sprite(TextureHolder.getInstance().getTexture(mMinedTexture)), null));
		Vector2f diamondSpawnVel = new Vector2f(40, -400);
		for (int i = 0; i < mTotalDiamonds; i++) {
			SpawnEntityCommand spawnCommand = new SpawnEntityCommand(getWorldPosition(),
					new Vector2f((float) Math.random() * 50 * Helper.getRandomSigned(), diamondSpawnVel.y),
					NodeType.WORLD);
			addCommand(spawnCommand);
		}
	}

	/**
	 * Sets the id of the texture to use as a the mined texture.
	 * 
	 * @param texID the id of the texture
	 */
	public void setMinedTexture(TextureID texID) {
		mMinedTexture = texID;
	}

	/**
	 * Helper method to create a CrystalItem by its different types
	 * from a MapObject.
	 * 
	 * @param object the map object
	 * @return a crystal item
	 */
	public static CrystalItem getCrystalItem(MapObject object) {
		Properties props = object.getProperties();
		Type type = Type.getType(props.getProperty(TmxKeys.CRYSTAL_TYPE, null));
		CrystalItem item = null;
		switch (type) {
		case BIG:
			item = new CrystalItem(TextureID.ITEM_CRYSTAL_BIG, props);
			item.setContainingDiamonds(5);
			item.setMinedTexture(TextureID.ITEM_CRYSTAL_MINED);
			item.setPosition((float) object.getX(), (float) object.getY());
			break;
		case NORMAL:
			item = new CrystalItem(TextureID.ITEM_CRYSTAL_NORMAL, props);
			item.setContainingDiamonds(3);
			item.setMinedTexture(TextureID.ITEM_CRYSTAL_MINED);
			item.setPosition((float) object.getX(), (float) object.getY());
			break;
		case SMALL:
			item = new CrystalItem(TextureID.ITEM_CRYSTAL_SMALL, props);
			item.setContainingDiamonds(2);
			item.setMinedTexture(TextureID.ITEM_CRYSTAL_MINED_SMALL);
			item.setPosition((float) object.getX(), (float) object.getY());
			break;
		default:
			System.err.println("CrystalItem: Unkown Itemtype - " + type);
		}
		return item;
	}

}
