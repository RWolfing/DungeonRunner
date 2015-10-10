package de.dungeonrunner;

import java.util.Properties;
import java.util.Vector;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

public class SceneNode extends BasicTransformable implements Drawable {

	private SceneNode mParentNode;
	private Vector<SceneNode> mChildren;
	private Properties mProperties;

	public SceneNode() {
		mChildren = new Vector<>();
		mProperties = new Properties();
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states,  Transform.combine(states.transform, getTransform()));
		drawCurrent(target, states);
		drawChildren(target, states);
	}

	protected void drawCurrent(RenderTarget target, RenderStates states){
		
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

	protected void updateCurrent(Time dt){
		
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

	public void setParentNode(SceneNode node) {
		mParentNode = node;
	}
	
	public void setProperties(Properties props){
		mProperties = props;
	}
	
	public void addProperty(String key, String value){
		mProperties.setProperty(key, value);
	}
	
	public String removeProperty(String key){
		return (String) mProperties.remove(key);
	}
	
	public SceneNode getParentNode() {
		return mParentNode;
	}
	
	public Transform getWorldTransform(){
		Transform transform = Transform.IDENTITY;
		for(SceneNode node = this; node != null; node = node.mParentNode){
			transform = Transform.combine(getTransform(), transform);
		}
		return transform;
	}

	public Vector2f getWorldPosition(){
		return getWorldTransform().transformPoint(Vector2f.ZERO);
	}
}
