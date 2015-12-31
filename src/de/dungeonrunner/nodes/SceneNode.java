package de.dungeonrunner.nodes;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.Collidable;
import de.dungeonrunner.NodeType;
import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.commands.SceneCommand;
import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.QuadTree;

/**
 * A SceneNode is used to manage all entities, sprites etc. of the game.
 * It provides most of the needed functionality to draw, update, check for collisions etc.
 * that are needed by the game. All entities of the game should extend from a SceneNode.
 * 
 * @author Robert Wolfinger
 *
 */
public class SceneNode extends BasicTransformable implements Drawable, Collidable {

	/*
	 * Enum for the type of a collision.
	 */
	public enum CollisionType {
		TOP, BOTTOM, LEFT, RIGHT, NONE
	}
	
	// Debugging purposes only
	public String mDebugText = "";
	public Color mColor = Color.GREEN;
	
	//The parent node that his node was attached to
	public SceneNode mParentNode;
	//The children nodes that were attached to this node
	public Vector<SceneNode> mChildren;
	protected NodeType mNodeType;

	private List<SceneNode> mCollisionObjects;

	//the properties of the node
	public HashMap<String, String> mPropertySet;

	/*
	 * Default constructor, creates a SceneNode
	 * with the given properties.
	 */
	public SceneNode(Properties props) {
		mChildren = new Vector<>();
		mCollisionObjects = new ArrayList<>();
		mPropertySet = new HashMap<>();
		mergeProperties(props);
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		//we have to draw the node and all attached children
		drawCurrent(target, states);
		drawChildren(target, states);
	}

	protected void drawCurrent(RenderTarget target, RenderStates states) {
		if (Constants.IS_DEBUGGING) {
			//Debugging only
			drawDebugging(target, states);
		}
	}

	/**
	 * A method for debugging.
	 * Draws the bounding rectangle of the node.
	 * 
	 * @param target the RenderTarget
	 * @param states the RenderStates
	 */
	protected void drawDebugging(RenderTarget target, RenderStates states) {
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

	/**
	 * Draws all children of the node.
	 * 
	 * @param target the RenderTarget
	 * @param states the RenderStates
	 */
	private void drawChildren(RenderTarget target, RenderStates states) {
		for (SceneNode node : mChildren) {
			//we iterate over all children and draw them
			node.draw(target, states);
		}
	}

	/*
	 * Method to update the node and all children.
	 */
	public void update(Time dt) {
		updateCurrent(dt);
		updateChildren(dt);
	}

	/**
	 * Method to update the node.
	 * This method should be overwritten with the update
	 * functionality of the subclass. 
	 * 
	 * @param dt delta time
	 */
	protected void updateCurrent(Time dt) {
		// Unused
	}

	/**
	 * Updates all children of the node.
	 * 
	 * @param dt delta time
	 */
	private void updateChildren(Time dt) {
		for (SceneNode node : mChildren) {
			//We iterate over all children and update them
			node.update(dt);
		}
	}

	/**
	 * Attaches an existing node to this node as a child.
	 * 
	 * @param node the child node
	 */
	public void attachChild(SceneNode node) {
		node.setParentNode(this);
		mChildren.addElement(node);
	}

	/**
	 * Detaches an existing child from this node.
	 * 
	 * @param node the child to detach
	 * @return the detached node or null if the node was not a child
	 */
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

	/**
	 * Creates and draws a rectangle shape as the bounding rectangle.
	 * 
	 * @param target the RenderTarget
	 * @param states the RenderStates
	 * @param rect the bounding rectangle to draw
	 */
	private void drawBoundingRect(RenderTarget target, RenderStates states, FloatRect rect) {
		RectangleShape shape = new RectangleShape();
		shape.setPosition(new Vector2f(rect.left, rect.top));
		shape.setSize(new Vector2f(rect.width, rect.height));
		shape.setFillColor(Color.TRANSPARENT);
		shape.setOutlineColor(mColor);
		shape.setOutlineThickness(1.0f);
		target.draw(shape);
	}

	/**
	 * Executes the given command if the NodeType of the command matches
	 * the type of this node. The command will be passed on to all children.
	 * 
	 * @param command the command to execute
	 */
	public void onCommand(SceneCommand command) {
		if (command.mNodeType == getType()) {
			command.execute(this);
		}

		for (SceneNode child : mChildren) {
			child.onCommand(command);
		}
	}

	/**
	 * Checks for collision with this node and all attached children.
	 * 
	 * @param collisionTree the QuadTree to check
	 */
	public void checkCollisions(QuadTree collisionTree) {
		this.checkCollision(collisionTree);
		for (SceneNode child : mChildren) {
			//Pass the collision tree to all children nodes
			child.checkCollisions(collisionTree);
		}
	}

	/**
	 * Checks if a collision occurred between this node and 
	 * any other node in the given QuadTree.
	 */
	public void checkCollision(QuadTree collisionTree) {
		mCollisionObjects.clear();
		//We retrieve all nodes that collide with the bounding rectangle of
		//this node
		collisionTree.retrieve(mCollisionObjects, getBoundingRect());

		//We remove all children from this node as possible collisions
		for (SceneNode node : this.getSceneGraph()) {
			mCollisionObjects.remove(node);
		}

		//We need to remove this node as a possible collision
		mCollisionObjects.remove(this);
		for (SceneNode node : mCollisionObjects) {
			//We need to process every remaining collision
			processCollision(node);
		}
	}

	/**
	 * Method to process the collision with the given SceneNode.
	 * 
	 * @param node the node that collides with this node
	 * @return the CollisionType that occured
	 */
	protected CollisionType processCollision(SceneNode node) {
		return CollisionType.NONE;
	}

	/**
	 * Collects pending commands from this node and all attached children
	 * and adds them to the given CommandStack.
	 * 
	 * @param commandStack the command stack to add commands to
	 */
	public void collectCommands(CommandStack commandStack) {
		collectCommand(commandStack);
		for (SceneNode node : mChildren) {
			node.collectCommands(commandStack);
		}
	}

	/**
	 * Adds all pending commands of this node to the given
	 * CommandStack.
	 * 
	 * @param commandStack the command stack to add pending commands to
	 */
	protected void collectCommand(CommandStack commandStack) {
		// Unused
	}

	/**
	 * Removes all nodes from this node that were destroyed and should 
	 * not be updated, drawed and managed anymore.
	 */
	public void cleanDestroyedNodes() {
		List<SceneNode> destroyedNodes = new ArrayList<>();
		if (isDestroyed()) {
			//If this node is destroyed we remove it from its parent
			if (mParentNode != null) {
				mParentNode.detachChild(this);
			}
		} else {
			//Otherwise we iterate over all children and check if
			//they were destroyed
			for (SceneNode node : mChildren) {
				if (node.isDestroyed())
					destroyedNodes.add(node);
			}
			mChildren.removeAll(destroyedNodes);
			for (SceneNode node : mChildren) {
				node.cleanDestroyedNodes();
			}
		}
	}

	/**
	 * Computes and returns the world transform of this node.
	 * 
	 * @return the world transform of this node
	 */
	public Transform getWorldTransform() {
		Transform transform = Transform.IDENTITY;
		for (SceneNode node = this; node != null; node = node.mParentNode) {
			transform = Transform.combine(node.getTransform(), transform);
		}
		return transform;
	}

	/**
	 * Returns the world position of this node.
	 * 
	 * @return the world position of this node
	 */
	public Vector2f getWorldPosition() {
		return getWorldTransform().transformPoint(Vector2f.ZERO);
	}

	/**
	 * Returns all children of the node.
	 * 
	 * @return the attached children
	 */
	public Vector<SceneNode> getChildren() {
		return mChildren;
	}

	/**
	 * Returns the bounding rectangle of this node.
	 * 
	 * @return the bounding rectangle
	 */
	public FloatRect getBoundingRect() {
		return null;
	}

	/**
	 * Sets the given node as the parent of this node.
	 * 
	 * @param node the parent node
	 */
	public void setParentNode(SceneNode node) {
		mParentNode = node;
	}

	/**
	 * This method merges the given properties into 
	 * the existing properties of this node.
	 * 
	 * @param props the properties that should be merged
	 */
	public void mergeProperties(Properties props) {
		if (props == null) {
			return;
		}

		for (Object keyObj : props.keySet()) {
			String key = (String) keyObj;
			addProperty(key, props.getProperty(key));
		}
	}

	/**
	 * Adds the given property with the given key and value
	 * to the properties of this node.
	 * 
	 * @param key the key of the property
	 * @param value the value of the property
	 */
	public void addProperty(String key, String value) {
		mPropertySet.put(key, value);
	}

	/**
	 * Retrieves the property with the given key.
	 * 
	 * @param key the key of the property
	 * @return the property
	 */
	public String getProperty(String key) {
		return mPropertySet.get(key);
	}

	/**
	 * Retrieves the property with the given key.
	 * If no property with this key exists the defvalue
	 * will be returned.
	 * 
	 * @param key the key of the property
	 * @return the property or a default value
	 */
	public String getProperty(String key, String defvalue) {
		String value = mPropertySet.get(key);
		if (value != null) {
			return value;
		} else {
			return defvalue;
		}
	}

	/**
	 * Removes the property with the given key.
	 * 
	 * @param key the key of the property to remove
	 * @return the removed property
	 */
	public String removeProperty(String key) {
		return mPropertySet.remove(key);
	}

	/**
	 * Returns the parent node of this node.
	 * 
	 * @return the parent node
	 */
	public SceneNode getParentNode() {
		return mParentNode;
	}

	/**
	 * Retrieves the scene graph beginning with this node.
	 * The scene graph starts with this node and contains all children,
	 * as well as all their children and so on.
	 * 
	 * @return a vector containing all nodes
	 */
	public Vector<SceneNode> getSceneGraph() {
		Vector<SceneNode> sceneGraph = new Vector<SceneNode>();
		sceneGraph.add(this);
		for (SceneNode node : mChildren) {
			sceneGraph.addAll(node.getSceneGraph());
		}
		return sceneGraph;
	}

	/**
	 * Sets the type of the node to the given NodeType.
	 * 
	 * @param nodeType the type of the node
	 */
	public void setNodeType(NodeType nodeType) {
		mNodeType = nodeType;
	}

	/**
	 * Retrieves the type of this node.
	 * 
	 * @return the NodeType of this node
	 */
	public NodeType getType() {
		return mNodeType;
	}

	/**
	 * Retrieves if this node is destroyed.
	 * 
	 * @return is this node destroyed
	 */
	protected boolean isDestroyed() {
		return false;
	}

	/**
	 * This method is executed when the node
	 * is being destroyed and should be overwritten
	 * accordingly by all subclasses.
	 */
	public void destroy() {
		// Unused
	}
}
