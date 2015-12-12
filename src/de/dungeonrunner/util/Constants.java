package de.dungeonrunner.util;

import java.io.File;

public class Constants {

	public static final String ROOT_DIR = System.getProperty("user.dir") + File.separator;
	public static final String RES_DIR = ROOT_DIR + "resources" + File.separator;
	public static final String MAP_DIR = RES_DIR + "maps" + File.separator;
	public static final String IMG_DIR = RES_DIR + "images" + File.separator;
	public static final String TEX_DIR = RES_DIR + "textures" + File.separator;
	public static final String ANIM_DIR = TEX_DIR + File.separator + "animations" + File.separator;
	public static boolean IS_DEBUGGING = false;
	
	
	//Properties
	public static final String BLOCK_VOLUME = "BlockVolume";
	public static final String PROJECTILE = "Projectile";
}
