package de.dungeonrunner;

import de.dungeonrunner.util.QuadTree;

/**
 * A interface for collisions.
 * 
 * @author Robert Wolfinger
 *
 */
public interface Collidable {
	void checkCollision(QuadTree collisionTree);
}
