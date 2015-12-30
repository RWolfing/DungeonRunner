package de.dungeonrunner.util;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;

/**
 * Quadtree implementation to check for collisions.
 * 
 * @author Robert Wolfinger
 */
public class QuadTree extends SceneNode {

	private int MAX_OBJECTS = 100;
	private int MAX_LEVELS = 5;

	private int mLevel;
	private List<SceneNode> mCollisionObjects;
	private FloatRect mQuadTreeBounds;
	private QuadTree[] mQuadTreeNodes;

	/**
	 * Default constructor, creates a new QuadTree with the given
	 * parameters.
	 * 
	 * @param pLevel the maximum levels of the quadtree
	 * @param pBounds the bounds of the quadtree
	 */
	public QuadTree(int pLevel, FloatRect pBounds) {
		super(null);
		mLevel = pLevel;
		mCollisionObjects = new ArrayList<>();
		mQuadTreeBounds = pBounds;
		mQuadTreeNodes = new QuadTree[4];
	}

	/**
	 * Clears the quadtree.
	 */
	public void clear() {
		mCollisionObjects.clear();
		for (int i = 0; i < mQuadTreeNodes.length; i++) {
			if (mQuadTreeNodes[i] != null) {
				mQuadTreeNodes[i].clear();
				mQuadTreeNodes[i] = null;
			}
		}
	}

	/**
	 * Splits the quadtree.
	 */
	private void split() {
		int subWidth = (int) (mQuadTreeBounds.width / 2);
		int subHeight = (int) (mQuadTreeBounds.height / 2);
		int x = (int) mQuadTreeBounds.left;
		int y = (int) mQuadTreeBounds.top;

		mQuadTreeNodes[0] = new QuadTree(mLevel + 1, new FloatRect(x + subWidth, y, subWidth, subHeight));
		mQuadTreeNodes[1] = new QuadTree(mLevel + 1, new FloatRect(x, y, subWidth, subHeight));
		mQuadTreeNodes[2] = new QuadTree(mLevel + 1, new FloatRect(x, y + subHeight, subWidth, subHeight));
		mQuadTreeNodes[3] = new QuadTree(mLevel + 1, new FloatRect(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	/**
	 * Returns the index of the quadrant for the given rectangle.
	 * 
	 * @param rect rectangle to check
	 * @return the index of the quadrant
	 */
	private int getIndex(FloatRect rect) {
		int index = -1;
		double verticalMidpoint = mQuadTreeBounds.left + (mQuadTreeBounds.width / 2);
		double horizontalMidpoint = mQuadTreeBounds.top + (mQuadTreeBounds.height / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = ((rect.top + rect.height) < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (rect.top > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if ((rect.left + rect.width) < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		} else if (rect.left > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}
		return index;
	}

	/**
	 * Inserts the given SceneNode into the QuadTree.
	 * 
	 * @param sceneNode the SceneNode to insert
	 */
	public void insert(SceneNode sceneNode) {
		if (sceneNode.getBoundingRect() == null) {
			return;
		}

		// Check if we have to put the node in a quadrant
		int index = getIndex(sceneNode.getBoundingRect());
		if (index != -1) {
			if (mQuadTreeNodes[index] != null) {
				mQuadTreeNodes[index].insert(sceneNode);
				return;
			}
		}

		// Did not put node in a quadrant
		mCollisionObjects.add(sceneNode);

		if (mCollisionObjects.size() > MAX_OBJECTS && mLevel < MAX_LEVELS) {
			if (mQuadTreeNodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < mCollisionObjects.size()) {
				index = getIndex(mCollisionObjects.get(i).getBoundingRect());
				if (index != -1) {
					SceneNode node = mCollisionObjects.remove(i);
					node.mColor = Color.YELLOW;
					mQuadTreeNodes[index].insert(node);

				} else {
					mCollisionObjects.get(i).mColor = Color.WHITE;
					i++;
				}
			}
		}
	}

	/**
	 * Retrieve a list of SceneNodes that intersect with the given Rectangle.
	 * 
	 * @param returnObjects a list of SceneNodes
	 * @param rect the collision Rectangle
	 * @return a list of SceneNodes that intersect with the given Rectangle
	 */
	public List<SceneNode> retrieve(List<SceneNode> returnObjects, FloatRect rect) {
		if (rect != null) {
			if ((rect.left + rect.width) < mQuadTreeBounds.left
					|| rect.left > (mQuadTreeBounds.left + mQuadTreeBounds.width)
					|| (rect.top + rect.height) < mQuadTreeBounds.top
					|| rect.top > (mQuadTreeBounds.top + mQuadTreeBounds.height)) {
				return returnObjects;
			}

			int index = getIndex(rect);
			if (index != -1 && mQuadTreeNodes[0] != null) {
				mQuadTreeNodes[index].retrieve(returnObjects, rect);
				returnObjects.addAll(mCollisionObjects);
			} else {
				retrieveAllLeaves(returnObjects);
			}
		}
		return returnObjects;
	}

	/**
	 * Retrieves all leaves of the QuadTree and adds them to the given
	 * List.
	 * 
	 * @param nodes a list of SceneNodes
	 */
	public void retrieveAllLeaves(List<SceneNode> nodes) {
		nodes.addAll(mCollisionObjects);
		for (int i = 0; i < mQuadTreeNodes.length; i++) {
			QuadTree node = mQuadTreeNodes[i];
			if (node != null) {
				node.retrieveAllLeaves(nodes);
			}
		}

	}

	@Override
	public FloatRect getBoundingRect() {
		return new FloatRect(mQuadTreeBounds.left, mQuadTreeBounds.top, mQuadTreeBounds.width, mQuadTreeBounds.height);
	}

	@Override
	protected void drawDebugging(RenderTarget target, RenderStates states) {
		super.drawDebugging(target, states);
		RectangleShape shape = new RectangleShape();
		shape.setPosition(new Vector2f(mQuadTreeBounds.left, mQuadTreeBounds.top));
		shape.setSize(new Vector2f(mQuadTreeBounds.width, mQuadTreeBounds.height));
		shape.setFillColor(Color.TRANSPARENT);
		shape.setOutlineColor(Color.RED);
		shape.setOutlineThickness(1.0f);
		target.draw(shape);

		for (int i = 0; i < mQuadTreeNodes.length; i++) {
			if (mQuadTreeNodes[i] != null) {
				mQuadTreeNodes[i].draw(target, states);
			}
		}
	}
}
