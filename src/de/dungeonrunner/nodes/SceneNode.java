package de.dungeonrunner.nodes;

import java.util.Properties;
import java.util.Vector;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.Collidable;
import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.QuadTree;

public class SceneNode extends BasicTransformable implements Drawable, Collidable {

	// Debugging purposes only
	public String mDebugText = "";

	public Color mColor = Color.GREEN;
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
		drawDebugging(target, states);
	}

	protected void drawDebugging(RenderTarget target, RenderStates states) {
		if (Constants.IS_DEBUGGING) {
			if (getBoundingRect() != null) {
				FloatRect rect = getBoundingRect();
				drawBoundingRect(target, states, rect);
				Text text = new Text(mDebugText, FontHolder.getInstance().getFont(FontID.DUNGEON_FONT));
				text.setColor(mColor);
				text.setPosition(new Vector2f(getBoundingRect().left + getBoundingRect().width / 2,
						getBoundingRect().top + getBoundingRect().height / 2));
				target.draw(text);
			}
		}
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

	private void drawBoundingRect(RenderTarget target, RenderStates states, FloatRect rect) {
		RectangleShape shape = new RectangleShape();
		shape.setPosition(new Vector2f(rect.left, rect.top));
		shape.setSize(new Vector2f(rect.width, rect.height));
		shape.setFillColor(Color.TRANSPARENT);
		shape.setOutlineColor(mColor);
		shape.setOutlineThickness(1.0f);
		target.draw(shape);
	}
	
	public void checkCollisions(QuadTree collisionTree) {
		this.checkCollision(collisionTree);
		for (SceneNode child : mChildren) {
			child.checkCollisions(collisionTree);
		}
	}
	
	@Override
	public void checkCollision(QuadTree collisionTree) {
		// TODO unused
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

	public Vector<SceneNode> getSceneGraph() {
		Vector<SceneNode> sceneGraph = new Vector<SceneNode>();
		sceneGraph.add(this);
		for (SceneNode node : mChildren) {
			sceneGraph.addAll(node.getSceneGraph());
		}
		return sceneGraph;
	}
}
