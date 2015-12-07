package de.dungeonrunner.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.util.QuadTree;

public class SceneNode extends BasicTransformable implements Drawable {

	public Color mColor = Color.TRANSPARENT;
	public SceneNode mParentNode;
	public Vector<SceneNode> mChildren;
	public Vector<SceneNode> mStaticChildren;
	public Vector<SceneNode> mDynamicChildren;

	public Properties mProperties;

	public SceneNode() {
		mChildren = new Vector<>();
		mProperties = new Properties();
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		drawCurrent(target, states);
		drawChildren(target, states);
	}

	protected void drawCurrent(RenderTarget target, RenderStates states) {
		drawBoundingRect(target, states);
		mColor = Color.TRANSPARENT;
	}

	private void drawChildren(RenderTarget target, RenderStates states) {
		for (SceneNode node : mChildren) {
			node.draw(target, states);
		}
	}

	public void update(Time dt) {
		updateCurrent(dt);
		updateChildren(dt);
	}

	protected void updateCurrent(Time dt) {

	}

	private void updateChildren(Time dt) {
		for (SceneNode node : mChildren) {
			node.update(dt);
		}
	}

	public void attachChild(SceneNode node) {
		node.setParentNode(this);
		mChildren.addElement(node);
	}

	public SceneNode detachChild(SceneNode node) {
		int indexNode = mChildren.indexOf(node);
		if (indexNode >= 0) {
			SceneNode result = mChildren.remove(indexNode);
			result.mParentNode = null;
			return result;
		} else {
			return null;
		}
	}

	public void checkSceneCollision(SceneNode sceneGraph, List<CollisionPair> collisionPairs) {
		checkNodeCollision(sceneGraph, collisionPairs);
		for (SceneNode childNode : sceneGraph.mChildren) {
			if (childNode != null) {
				checkSceneCollision(childNode, collisionPairs);
			}
		}
	}

	void checkNodeCollision(SceneNode node, List<CollisionPair> collisionPairs) {
		if (this != node) {
			FloatRect collisionRect = collides(this, node);
			if (collisionRect != null) {
				CollisionPair pair = new CollisionPair(this, node);
				if (!collisionPairs.contains(pair)) {
					collisionPairs.add(pair);
					onCollision(node);
				}
			}
		}
		for (SceneNode childNode : mChildren) {
			childNode.checkNodeCollision(node, collisionPairs);
		}
	}

	private FloatRect collides(SceneNode node1, SceneNode node2) {
		Boolean isNode1Blocking = Boolean.valueOf(node1.getProperty("BlockVolume"));
		Boolean isNode2Blocking = Boolean.valueOf(node2.getProperty("BlockVolume"));
		if (isNode1Blocking && isNode2Blocking) {
			return node1.getBoundingRect().intersection(node2.getBoundingRect());
		} else {
			return null;
		}
	}

	private void drawBoundingRect(RenderTarget target, RenderStates states) {
		FloatRect rect = getBoundingRect();
		if (rect != null) {
			RectangleShape shape = new RectangleShape();
			shape.setPosition(new Vector2f(rect.left, rect.top));
			shape.setSize(new Vector2f(rect.width, rect.height));
			shape.setFillColor(Color.TRANSPARENT);
			shape.setOutlineColor(mColor);
			shape.setOutlineThickness(1.0f);
			target.draw(shape);
		}
	}

	public Transform getWorldTransform() {
		Transform transform = Transform.IDENTITY;

		for (SceneNode node = this; node != null; node = node.mParentNode) {
			transform = Transform.combine(node.getTransform(), transform);
		}
		return transform;
	}

	public Vector2f getWorldPosition() {
		return getWorldTransform().transformPoint(Vector2f.ZERO);
	}

	public Vector<SceneNode> getChildren() {
		return mChildren;
	}

	public FloatRect getBoundingRect() {
		return null;
	}

	public void setParentNode(SceneNode node) {
		mParentNode = node;
	}

	public void setProperties(Properties props) {
		mProperties = props;
	}

	public void addProperty(String key, String value) {
		mProperties.setProperty(key, value);
	}

	public String getProperty(String key) {
		return mProperties.getProperty(key);
	}

	public String removeProperty(String key) {
		return (String) mProperties.remove(key);
	}

	public SceneNode getParentNode() {
		return mParentNode;
	}

	public List<SceneNode> getCollisionGraph() {
		List<SceneNode> collisionNodes = new ArrayList();
		if (Boolean.valueOf(getProperty("BlockVolume"))) {
			collisionNodes.add(this);
		}
		for (SceneNode node : mChildren) {
			collisionNodes.addAll(getCollisionGraph());
		}
		return collisionNodes;
	}

	public void onCollision(SceneNode node) {

	}

	public void checkCollision(QuadTree collisionTree) {

	}

	public void checkCollisions(QuadTree collisionTree) {
		this.checkCollision(collisionTree);
		for (SceneNode child : mChildren) {
			child.checkCollisions(collisionTree);
		}
	}

	public Vector<SceneNode> getSceneGraph() {
		Vector<SceneNode> sceneGraph = new Vector<SceneNode>();
		sceneGraph.add(this);
		for (SceneNode node : mChildren) {
			sceneGraph.addAll(node.getSceneGraph());
		}
		return sceneGraph;
	}
}
