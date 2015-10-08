package de.dungeonrunner;

import java.util.ArrayList;
import java.util.List;

import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;

public class TmxMapLoader {

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
		return map;
	}
	
	public static List<Tile> getBlockingTiles(Map map){
		List<Tile> blockingTiles = new ArrayList<>();
		
		for (int i = 0; i < map.getTileSets().size(); i++) {
			TileSet tileSet = map.getTileSets().get(i);
			final int numberTiles = tileSet.size();
			for (int o = 0; o < numberTiles; o++) {
				Tile tile = tileSet.getTile(o);
				boolean isBlockVolume = Boolean.parseBoolean(tile.getProperties().getProperty("BlockVolume", "false"));
				if(isBlockVolume){
					blockingTiles.add(tile);
				}
			}
		}
		return blockingTiles;
	}
}
