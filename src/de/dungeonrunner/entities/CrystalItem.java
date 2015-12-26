package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.SpawnEntityCommand;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.util.TmxKeys;
import de.dungeonrunner.view.LifeBar;

public class CrystalItem extends Item {

	public enum Type {
		BLUE, RED, PINK;

		// TODO Refactor
		public static Type getType(String type) {
			if (type.equals("blue")) {
				return BLUE;
			} else if (type.equals("red")) {
				return RED;
			} else {
				return Type.PINK;
			}
		}
	}

	private Type mType;
	private final int mTotalMinepoints = 3;
	private int mCurrentMinepoints = 3;
	private final int mScore = 10;
	private LifeBar mLifeBar;

	public CrystalItem(TextureID texID, Properties props) {
		super(texID, props);
		mType = Type.getType(mPropertySet.get(TmxKeys.CRYSTAL_TYPE));
		mLifeBar = new LifeBar();
		mLifeBar.setCurrent(mTotalMinepoints);
		mLifeBar.setMaximum(mTotalMinepoints);
		mLifeBar.setDimension(new Vector2f(getBoundingRect().width, 5f));
		mLifeBar.setColor(Color.GREEN);
		mLifeBar.setPosition(0, -10);
		attachChild(mLifeBar);

	}

	public void mine() {
		mCurrentMinepoints--;
		if (mCurrentMinepoints < 1) {
			destroy();
			GameState.getGameUI().getDiamondsComponent().incrementDiamonds();
		} else {
			mLifeBar.setCurrent(mCurrentMinepoints);
		}
	}

	private void setupByType() {
		switch (mType) {
		case BLUE:

			break;
		case RED:
			break;

		case PINK:
			break;

		default:
			System.err.println("Invalid Crystal Type!");
			break;
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		SpawnEntityCommand spawnCommand = new SpawnEntityCommand(getWorldPosition(), NodeType.WORLD);
		addCommand(spawnCommand);
	}
	
	

}
