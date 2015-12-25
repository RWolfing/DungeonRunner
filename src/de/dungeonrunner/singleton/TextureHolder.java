package de.dungeonrunner.singleton;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;

import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileSet;

public class TextureHolder {

	public enum TextureID {
		ANIM_IDLE, PLAYER_TEXTURE, TITLE_BG_SCREEN, BULLET, ENEMY, ANIM_PLAYER_RUN, ANIM_PLAYER_JUMP,
		PROJECTILE_DNYAMITE, DYNAMITE_SINGLE, ANIM_MINER_THROW, ANIM_MINER_ATTACK, ANIM_MINER_DEATH,
		ANIM_STONE_THROWER_IDLE, ANIM_STONE_THROWER_ATTACK, ANIM_STONE_THROWER_WALK, ANIM_STONE_THROWER_DEATH, STONE,
		ITEM_CRYSTAL, 
		MAIN_MENU_SCREEN, BUTTON_INACTIVE, BUTTON_DEFAULT, BUTTON_SELECTED, BUTTON_ACTIVATED, ANIM_EXPLOSION,
		UI_LIFEBAR_BG, UI_LIFEBAR
	}

	private static TextureHolder mInstance;
	private HashMap<TextureID, Texture> mTextures;
	private HashMap<Integer, Texture> mTileTextures;

	public static TextureHolder getInstance() {
		if (mInstance == null) {
			mInstance = new TextureHolder();
		}
		return mInstance;
	}

	private TextureHolder() {
		mTextures = new HashMap<>();
		mTileTextures = new HashMap<>();
	}

	public boolean loadTexture(TextureID id, String filePath) {
		Texture texture = null;
		try {
			texture = new Texture();
			texture.loadFromFile(Paths.get(filePath));
		} catch (IOException e) {
			System.err.println("Could not load texture from " + filePath + ", IOException");
			return false;
		}
		mTextures.put(id, texture);
		return true;
	}

	private boolean loadImageTexture(int textureId, java.awt.Image awtImage) {
		try {
			Image image = new Image();
			image.create((BufferedImage) awtImage);
			Texture texture = new Texture();
			texture.loadFromImage(image);
			mTileTextures.put(textureId, texture);
		} catch (TextureCreationException e) {
			System.err.println("Could not create texture from image");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void loadTiledTextures(Map tiledMap) {
		final int numberTileSets = tiledMap.getTileSets().size();

		for (int i = 0; i < numberTileSets; i++) {
			TileSet tileSet = tiledMap.getTileSets().get(i);
			final int numberTiles = tileSet.size();
			for (int o = 0; o < numberTiles; o++) {
				Tile tile = tileSet.getTile(o);
				loadImageTexture(tile.getId(), tile.getImage());
			}
		}
	}

	public Texture getTexture(TextureID id) {
		return mTextures.get(id);
	}

	public Texture getTileTexture(int id) {
		return mTileTextures.get(id);
	}
}
