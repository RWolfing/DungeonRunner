package de.dungeonrunner.util;

import java.io.File;

import org.jsfml.system.Vector2f;

/**
 * Constants class
 * 
 * @author Robert Wolfinger
 *
 */
public class Constants {

	public static final String ROOT_DIR = System.getProperty("user.dir") + File.separator;
	public static final String RES_DIR = ROOT_DIR + "resources" + File.separator;
	public static final String MAP_DIR = RES_DIR + "maps" + File.separator;
	public static final String IMG_DIR = RES_DIR + "images" + File.separator;
	public static final String TEX_DIR = RES_DIR + "textures" + File.separator;
	public static final String ANIM_DIR = TEX_DIR + File.separator + "animations" + File.separator;
	public static boolean IS_DEBUGGING = false;

	// Physics
	public static final Vector2f GRAVITY = new Vector2f(0, 170);
	public static final float GRAVITY_DOWN = 170;

	// Properties
	public static final String BLOCK_VOLUME = "BlockVolume";
	public static final String PROJECTILE = "Projectile";
	public static final String UNIT_VOLUME = "UnitVolume";

	public static final float MENU_ITEM_HEIGHT = 100;
	public static final float MENU_ITEM_WIDTH = 200;
	public static final Vector2f MENU_ITEM_TEXT_OFFSET = new Vector2f(0, -10);
	public static final float MENU_ITEM_SPACING = 25;
}
