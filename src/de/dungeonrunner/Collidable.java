package de.dungeonrunner;

import de.dungeonrunner.util.QuadTree;

public interface Collidable {
	
	void checkCollision(QuadTree collisionTree);
}
