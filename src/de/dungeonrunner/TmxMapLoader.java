package de.dungeonrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;

public class TmxMapLoader {

	private static Map mMap;
	private static List<Tile> mBlockingTiles;

	public static Map loadMap(String filepath) {
		String fileToOpen = filepath;

		if (fileToOpen == null) {
			return null;
		}

		Map map;
		try {
			TMXMapReader mapReader = new TMXMapReader();
			map = mapReader.readMap(fileToOpen);
		} catch (Exception e) {
			System.out.println("Error while reading the map:\n" + e.getMessage());
			return null;
		}

		System.out.println(map.toString() + " loaded");
		mMap = map;
		return map;
	}

	public static List<Tile> getBlockingTiles() {
		List<Tile> blockingTiles = new ArrayList<>();
		Map map = mMap;
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

	public static Properties getTileInstancePropertiesAt(int x, int y) {
		if (mMap != null) {
			for (MapLayer layer : mMap) {
				if (layer instanceof TileLayer) {
					TileLayer tilelayer = (TileLayer) layer;
					return tilelayer.getTileInstancePropertiesAt(x, y);
				}
			}
		}
		return null;
	}

	public static Map getMap() {
		return mMap;
	}
}
