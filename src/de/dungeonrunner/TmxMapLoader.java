package de.dungeonrunner;

import tiled.core.Map;
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
}
