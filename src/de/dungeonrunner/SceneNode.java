package de.dungeonrunner;

import java.util.Properties;
import java.util.Set;
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

public class SceneNode extends BasicTransformable implements Drawable {

	public SceneNode mParentNode;
	public Vector<SceneNode> mChildren;
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

	void checkNodeCollision(SceneNode node, Set<CollisionPair> collisionPairs) {
		if (this != node) {
			FloatRect collisionRect = collides(this, node);
			if(collisionRect != null){
				collisionPairs.add(new CollisionPair(this, node));
				onCollision(node);
			}
		}
		for (SceneNode childNode : mChildren) {
			childNode.checkNodeCollision(node, collisionPairs);
		}
	}

	public void checkSceneCollision(SceneNode sceneGraph, Set<CollisionPair> collisionPairs) {
		checkNodeCollision(sceneGraph, collisionPairs);
		for (SceneNode childNode : sceneGraph.mChildren) {
			if (childNode != null) {
				checkSceneCollision(childNode, collisionPairs);
			}
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

	public Transform getWorldTransform() {
		Transform transform = Transform.IDENTITY;

		for (SceneNode node = this; node != null; node = node.mParentNode) {
			//System.out.println("Get Transform" + getTransform());
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

	public static class CollisionPair {

		public SceneNode mNode1;
		public SceneNode mNode2;

		public CollisionPair(SceneNode node1, SceneNode node2) {
			mNode1 = node1;
			mNode2 = node2;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mNode1 == null) ? 0 : mNode1.hashCode());
			result = prime * result + ((mNode2 == null) ? 0 : mNode2.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CollisionPair other = (CollisionPair) obj;
			if (mNode1 == null) {
				if (other.mNode1 != null)
					return false;
			} else if (!mNode1.equals(other.mNode1))
				return false;
			if (mNode2 == null) {
				if (other.mNode2 != null)
					return false;
			} else if (!mNode2.equals(other.mNode2))
				return false;
			return true;
		}
	}

	private void drawBoundingRect(RenderTarget target, RenderStates states) {
		FloatRect rect = getBoundingRect();
		Boolean isNode1Blocking = Boolean.valueOf(getProperty("BlockVolume"));
		if (isNode1Blocking) {
			RectangleShape shape = new RectangleShape();
			shape.setPosition(new Vector2f(rect.left, rect.top));
			shape.setSize(new Vector2f(rect.width, rect.height));
			shape.setFillColor(Color.TRANSPARENT);
			shape.setOutlineColor(Color.GREEN);
			shape.setOutlineThickness(1.0f);
			target.draw(shape);
		}
	}
	
	protected void onCollision(SceneNode node){
		
	}
}
