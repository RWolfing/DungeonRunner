package de.dungeonrunner.singleton;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;

import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileSet;

/**
 * A Singleton to hold all textures needed for the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class TextureHolder {

	/**
	 * Enum to hold all texture ids.
	 * 
	 * @author Robert Wolfinger
	 *
	 */
	public enum TextureID {
		ANIM_IDLE, PLAYER_TEXTURE, TITLE_BG_SCREEN, BULLET, STONE_THROWER, ANIM_PLAYER_RUN, ANIM_PLAYER_JUMP, PROJECTILE_DNYAMITE, DYNAMITE_SINGLE, ANIM_MINER_THROW, ANIM_MINER_ATTACK, ANIM_MINER_DEATH, ANIM_STONE_THROWER_IDLE, ANIM_STONE_THROWER_ATTACK, ANIM_STONE_THROWER_WALK, ANIM_STONE_THROWER_DEATH, STONE, ITEM_CRYSTAL_BIG, MAIN_MENU_SCREEN, BUTTON_INACTIVE, BUTTON_DEFAULT, BUTTON_SELECTED, BUTTON_ACTIVATED, ANIM_EXPLOSION, UI_LIFEBAR_BG, UI_LIFEBAR, UI_AMMO, UI_AMMO_INDICATOR, UI_DIAMOND, DIAMOND, ITEM_CRYSTAL_NORMAL, ITEM_CRYSTAL_SMALL, ITEM_CRYSTAL_MINED, ITEM_CRYSTAL_MINED_SMALL, LEVEL_EXIT_OPEN, LEVEL_EXIT_CLOSED, ICON_FAILURE, ICON_SUCCESS, LEVEL_SELECTION_BACKGROUND
	}

	private static TextureHolder mInstance;
	private HashMap<TextureID, Texture> mTextures;
	private HashMap<Integer, Texture> mTileTextures;

	/**
	 * Returns the instance of the TextureHolder.
	 * 
	 * @return a TextureHolder instance
	 */
	public static TextureHolder getInstance() {
		if (mInstance == null) {
			mInstance = new TextureHolder();
		}
		return mInstance;
	}

	/**
	 * Default contructor.
	 */
	private TextureHolder() {
		mTextures = new HashMap<>();
		mTileTextures = new HashMap<>();
	}

	/**
	 * Loads the Texture from the given filePath and holds it under the given
	 * id.
	 * 
	 * @param id
	 *            id of the loaded texture
	 * @param filePath
	 *            file path of the texture
	 * @return if the texture could be loaded
	 */
	public boolean loadTexture(TextureID id, String filePath) {
		Texture texture = null;
		try {
			texture = new Texture();
			InputStream textStream = TextureHolder.class.getResourceAsStream(filePath);
			if (textStream != null) {
				texture.loadFromStream(textStream);
			} else {
				System.out.println("Stream from " + filePath);
			}
		} catch (IOException e) {
			System.err.println("Could not load texture from " + filePath + ", IOException");
			return false;
		}
		mTextures.put(id, texture);
		return true;
	}

	/**
	 * Loads the Texture form the given awtImage and holds it under the given
	 * id.
	 * 
	 * @param textureId
	 *            id of the loaded texture
	 * @param awtImage
	 *            a java.awt.Image object
	 * @return if the texture could be loaded
	 */
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

	/**
	 * Loads a set of textures from the given Map.
	 * 
	 * @param tiledMap
	 *            a tmx Map object
	 */
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

	/**
	 * Returns the texture with the given id.
	 * 
	 * @param id
	 *            the id of the texture
	 * @return a texture
	 */
	public Texture getTexture(TextureID id) {
		return mTextures.get(id);
	}

	/**
	 * Returns a tiled texture with the given id.
	 * 
	 * @param id
	 *            the id of the tiled texture
	 * @return a texture
	 */
	public Texture getTileTexture(int id) {
		return mTileTextures.get(id);
	}
}
