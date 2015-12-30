package de.dungeonrunner.util;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.entities.GameEntity;
import de.dungeonrunner.nodes.SceneNode.CollisionType;
import de.dungeonrunner.view.Component;

/**
 * A Helper class to provide needed functionality.
 * 
 * @author Robert Wolfinger
 *
 */
public class Helper {

	/**
	 * Lays out the given components vertically.
	 * 
	 * @param windowSize the size of the window
	 * @param itemSpacing spacing between the items
	 * @param center if the components should be centered
	 * @param offSetX x-offset of the components
	 * @param offsetY y-offset of the components
	 * @param components components that should be laid out
	 */
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

	/**
	 * Creates the unit vector of the given vector.
	 * 
	 * @param vector the vector where the unit vector should be created from
	 * @return the unit vector
	 */
	public static Vector2f unitVector(Vector2f vector) {
		float length = (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
		return Vector2f.div(vector, length);
	}

	/**
	 * 
	 * @param entity
	 * @param intersection
	 * @return
	 */
	public static CollisionType resetEntityByCollision(GameEntity entity, FloatRect intersection) {
		CollisionType type = CollisionType.NONE;
		if (intersection == null || entity == null) {
			return type;
		}

		// Round the collision TODO why is this necessary
		if (intersection.width > 3 || intersection.height > 3) {
			if (intersection.width > intersection.height) {
				// Player inbound from Top or Bottom
				if (entity.getBoundingRect().top < intersection.top) {
					// Collision from bottom
					entity.setPosition(entity.getWorldPosition().x, entity.getWorldPosition().y - intersection.height);
					type = CollisionType.BOTTOM;
				} else {
					// Collision from top
					entity.setPosition(entity.getWorldPosition().x, entity.getWorldPosition().y + intersection.height);
					type = CollisionType.TOP;
				}
			} else {
				if (entity.getBoundingRect().left < intersection.left) {
					// Collision from the right
					entity.setPosition(entity.getWorldPosition().x - intersection.width, entity.getWorldPosition().y);
					type = CollisionType.RIGHT;
				} else {
					entity.setPosition(entity.getWorldPosition().x + intersection.width, entity.getWorldPosition().y);
					type = CollisionType.LEFT;
				}
			}
		}
		return type;
	}

	/**
	 * Returns a random signed integer either 1 or -1.
	 * 
	 * @return 1 or -1
	 */
	public static int getRandomSigned() {
		if (Math.random() > 0.5) {
			return -1;
		} else {
			return 1;
		}
	}
}
