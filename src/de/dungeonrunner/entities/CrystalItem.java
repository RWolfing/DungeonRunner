package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.SpawnEntityCommand;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.util.TmxKeys;
import de.dungeonrunner.view.LifeBar;
import tiled.core.MapObject;

public class CrystalItem extends Item {

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

	private int mTotalDiamonds;
	private int mContainingDiamonds;
	private LifeBar mLifeBar;
	private TextureID mMinedTexture;
	private boolean mWasMined;

	public CrystalItem(TextureID texID, Properties props) {
		super(texID, props);
		mLifeBar = new LifeBar();
		mLifeBar.setDimension(new Vector2f(getBoundingRect().width, 5f));
		mLifeBar.setColor(Color.GREEN);
		mLifeBar.setPosition(0, -10);
		attachChild(mLifeBar);
	}

	public void mine() {
		if (!mWasMined) {
			mContainingDiamonds--;
			if (mContainingDiamonds < 1) {
				destroy();
			}
			mLifeBar.setCurrent(mContainingDiamonds);
		}
	}

	public void setContainingDiamonds(int diamonds) {
		mTotalDiamonds = mContainingDiamonds = diamonds;
		mLifeBar.setCurrent(mTotalDiamonds);
		mLifeBar.setMaximum(mTotalDiamonds);
	}

	@Override
	public void destroy() {
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

	public void setMinedTexture(TextureID texID) {
		mMinedTexture = texID;
	}

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
