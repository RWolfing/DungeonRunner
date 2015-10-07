package de.dungeonrunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.jsfml.graphics.Texture;

public class TextureHolder {

	public enum TextureID {

	}

	private static TextureHolder mInstance;
	private HashMap<TextureID, Texture> mTextures;
	private HashMap<Integer, Texture> mTileTextures;

	public static TextureHolder getInstance(){
		if(mInstance == null){
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
	
	public boolean loadTileTexture(int textureId, Texture texture){
		if(texture == null){
			System.out.println("Loading null texture into TextureHolder");
			return false;
		}
		mTileTextures.put(textureId, texture);
		return true;
	}

	public Texture getTexture(TextureID id) {
		return mTextures.get(id);
	}
	
	public Texture getTileTexture(int id){
		return mTileTextures.get(id);
	}
}
