package de.dungeonrunner.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;

/**
 * A helper class to load a .tmx Map into a map object.
 * 
 * @author Robert Wolfinger
 */
public class TmxMapLoader {

	/**
	 * Loads the map from the given filepath.
	 * 
	 * @param filepath the filepath of the map
	 * @return the loaded map
	 */
	public static Map loadMap(String filepath) {
		String fileToOpen = filepath;

		if (fileToOpen == null) {
			return null;
		}

		Map map;
		try {
			TMXMapReader mapReader = new TMXMapReader();
			map = mapReader.readMap(filepath);
		} catch (Exception e) {
			System.out.println("Error while reading the map " + filepath + ":\n" + e.getMessage());
			return null;
		}
		return map;
	}

	/**
	 * Retrieves all tiles with the property "BlockVolume" of the given map.
	 * 
	 * @param map a .tmx map
	 * @return all blocking tiles
	 */
	public static List<Tile> getBlockingTiles(Map map) {
		List<Tile> blockingTiles = new ArrayList<>();
		if (map != null) {
			for (int i = 0; i < map.getTileSets().size(); i++) {
				TileSet tileSet = map.getTileSets().get(i);
				final int numberTiles = tileSet.size();
				for (int o = 0; o < numberTiles; o++) {
					Tile tile = tileSet.getTile(o);
					boolean isBlockVolume = Boolean
							.parseBoolean(tile.getProperties().getProperty("BlockVolume", "false"));
					if (isBlockVolume) {
						blockingTiles.add(tile);
					}
				}
			}
		} else {
			System.err.println("No map loaded, no blocking volumes");
		}
		return blockingTiles;
	}

	/**
	 * Retrieves the properties of the tile at the given x- and y-coordinates
	 * for the given map.
	 * 
	 * @param map the .tmx map
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * 
	 * @return the properties of the tile
	 */
	public static Properties getTileInstancePropertiesAt(Map map, int x, int y) {
		if (map != null) {
			for (MapLayer layer : map) {
				if (layer instanceof TileLayer) {
					TileLayer tilelayer = (TileLayer) layer;
					return tilelayer.getTileInstancePropertiesAt(x, y);
				}
			}
		}
		return null;
	}
}
