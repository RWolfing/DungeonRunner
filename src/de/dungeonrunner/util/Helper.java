package de.dungeonrunner.util;

import org.jsfml.system.Vector2i;

import de.dungeonrunner.view.Component;

public class Helper {

	public static void layoutVertically(Vector2i windowSize, float itemSpacing, boolean center, float offSetX,
			float offsetY, Component... components) {
		int totalLayoutHeight = 0;
		float startPointY = 0 + offsetY;
		float startPointX = 0 + offSetX;

		for (Component component : components) {
			totalLayoutHeight += component.getHeight();
		}
		totalLayoutHeight += (components.length - 1) * itemSpacing;
		if (center) {
			startPointX += windowSize.x / 2;
			startPointY += windowSize.y / 2 - totalLayoutHeight / 2;
		}

		float prevItemHeight = 0;
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];
			component.setPosition(startPointX, startPointY + prevItemHeight);
			prevItemHeight += component.getHeight() + itemSpacing;
		}
	}
}
