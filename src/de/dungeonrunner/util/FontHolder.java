package de.dungeonrunner.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.jsfml.graphics.Font;

public class FontHolder {

	public enum FontID {

	}

	private static FontHolder mInstance;
	private HashMap<FontID, Font> mFonts;

	public static FontHolder getInstance() {
		if (mInstance == null) {
			mInstance = new FontHolder();
		}
		return mInstance;
	}

	private FontHolder() {
		mFonts = new HashMap<>();
	}

	public boolean loadFont(FontID id, String filePath) {
		Font font = null;
		try {
			font = new Font();
			font.loadFromFile(Paths.get(filePath));
		} catch (IOException e) {
			System.err.println("Could not load font from " + filePath + ", IOException");
			return false;
		}
		mFonts.put(id, font);
		return true;

	}

	public Font getFont(FontID id) {
		return mFonts.get(id);
	}
}
