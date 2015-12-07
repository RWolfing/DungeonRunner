package de.dungeonrunner.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;

public class QuadTree extends SceneNode{

	private int MAX_OBJECTS = 20;
	private int MAX_LEVELS = 5;
	
	private int mLevel;
	private List<SceneNode> mObjects;
	private FloatRect mBounds;
	private QuadTree[] mNodes;
	
	public QuadTree(int pLevel, FloatRect pBounds){
		mLevel = pLevel;
		mObjects = new ArrayList<>();
		mBounds = pBounds;
		mNodes = new QuadTree[4];
	}
	
	public void clear(){
		mObjects.clear();
		for(int i = 0; i < mNodes.length; i++){
			if(mNodes[i] != null) {
				mNodes[i].clear();
				mNodes[i] = null;
			}
		}
	}
	
	private void split(){
		int subWidth = (int)(mBounds.width / 2);
		int subHeight = (int)(mBounds.height / 2);
		int x = (int) mBounds.left;
		int y = (int) mBounds.top;
		
		mNodes[0] = new QuadTree(mLevel + 1, new FloatRect(x + subWidth, y, subWidth, subHeight));
		mNodes[1] = new QuadTree(mLevel + 1, new FloatRect(x, y, subWidth, subHeight));
		mNodes[2] = new QuadTree(mLevel + 1, new FloatRect(x, y + subHeight, subWidth, subHeight));
		mNodes[3] = new QuadTree(mLevel + 1, new FloatRect(x + subWidth, y + subHeight, subWidth, subHeight));
	}
	
	private int getIndex(FloatRect rect){
		int index = -1;
		double verticalMidpoint = mBounds.left + (mBounds.width / 2);
		double horizontalMidpoint = mBounds.top + (mBounds.height / 2);
		
		//TODO
		// Object can completely fit within the top quadrants
		boolean topQuadrant = (rect.top < horizontalMidpoint && rect.top + rect.height < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (rect.top > horizontalMidpoint);
		
		// Object can completely fit within the left quadrants
		   if (rect.left < verticalMidpoint && rect.left + rect.width < verticalMidpoint) {
		      if (topQuadrant) {
		        index = 1;
		      }
		      else if (bottomQuadrant) {
		        index = 2;
		      }
		    }
		    else if (rect.left >= verticalMidpoint) {
		     if (topQuadrant) {
		       index = 0;
		     }
		     else if (bottomQuadrant) {
		       index = 3;
		     }
		   }
		   return index;
	}
	
	public void insert(SceneNode sceneNode){
		if(sceneNode.getBoundingRect() == null){
			return;
		}
		sceneNode.mColor = Color.WHITE;
		if(mNodes[0] != null){
			int index = getIndex(sceneNode.getBoundingRect());
			
			if(index != -1){
				mNodes[index].insert(sceneNode);
				sceneNode.mColor = Color.YELLOW;
				return;
			}
		}
		
		mObjects.add(sceneNode);
		
		if(mObjects.size() > MAX_OBJECTS && mLevel < MAX_LEVELS){
			if(mNodes[0] == null){
				split();
			}
			
			int i = 0;
			while (i < mObjects.size()) {
				int index = getIndex(mObjects.get(i).getBoundingRect());
				if(index != -1){
					mNodes[index].insert(mObjects.remove(i));
				} else {
					i++;
				}
			}
		}
	}
	
	public List<SceneNode> retrieve(List<SceneNode> returnObjects, FloatRect rect){
		int index = getIndex(rect);

		if(index != -1 && mNodes[0] != null) {
			mNodes[index].retrieve(returnObjects, rect);
		}
		
		returnObjects.addAll(mObjects);
		return returnObjects;
	}

	@Override
	public FloatRect getBoundingRect() {
		return new FloatRect(mBounds.left, mBounds.top, mBounds.width, mBounds.height);
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RectangleShape shape = new RectangleShape();
		shape.setPosition(new Vector2f(mBounds.left, mBounds.top));
		shape.setSize(new Vector2f(mBounds.width, mBounds.height));
		shape.setFillColor(Color.TRANSPARENT);
		shape.setOutlineColor(Color.RED);
		shape.setOutlineThickness(1.0f);
		target.draw(shape);
		
		for(int i = 0; i < mNodes.length; i++){
			if(mNodes[i] != null){
				mNodes[i].draw(target, states);
			}
		}
	}
	
	
}
