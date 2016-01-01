package de.dungeonrunner.singleton;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.jsfml.graphics.Font;

/**
 * A Singleton to hold and register different fonts used by the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class FontHolder {

	public enum FontID {
		DUNGEON_FONT
	}

	private static FontHolder mInstance;
	private HashMap<FontID, Font> mFonts;

	/**
	 * Returns the instance of the FontHolder.
	 * 
	 * @return a FontHolder instance
	 */
	public static FontHolder getInstance() {
		if (mInstance == null) {
			mInstance = new FontHolder();
		}
		return mInstance;
	}

	/**
	 * Default constructor.
	 */
	private FontHolder() {
		mFonts = new HashMap<>();
	}

	/**
	 * Loads the Font From the given filePaht and holds it 
	 * under the given id.
	 * 
	 * @param id id of the font
	 * @param filePath filePath to the font file
	 * @return success
	 */
	public boolean loadFont(FontID id, String filePath) {
		Font font = null;
		try {
			font = new Font();
			font.loadFromStream(FontHolder.class.getResourceAsStream(filePath));
		} catch (IOException e) {
			System.err.println("Could not load font from " + filePath + ", IOException");
			return false;
		}
		mFonts.put(id, font);
		return true;

	}

	/**
	 * Retrieves the Font for the given id.
	 * 
	 * @param id id of the font
	 * @return a font
	 */
	public Font getFont(FontID id) {
		return mFonts.get(id);
	}
}
