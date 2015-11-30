package de.dungeonrunner.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;

import de.dungeonrunner.nodes.SceneNode;

public class QuadTree {

	private int MAX_OBJECTS = 10;
	private int MAX_LEVELS = 5;
	
	private int mLevel;
	private List<SceneNode> mObjects;
	private Rectangle mBounds;
	private QuadTree[] mNodes;
	
	public QuadTree(int pLevel, Rectangle pBounds){
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
		int subWidth = (int)(mBounds.getWidth() / 2);
		int subHeight = (int)(mBounds.getHeight() / 2);
		int x = (int) mBounds.getX();
		int y = (int) mBounds.getY();
		
		mNodes[0] = new QuadTree(mLevel + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		mNodes[1] = new QuadTree(mLevel + 1, new Rectangle(x, y, subWidth, subHeight));
		mNodes[2] = new QuadTree(mLevel + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		mNodes[3] = new QuadTree(mLevel + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}
	
	private int getIndex(FloatRect rect){
		if(rect == null){
			return -1;
		}
		
		int index = -1;
		double verticalMidpoint = mBounds.getX() + (mBounds.getWidth() / 2);
		double horizontalMidpoint = mBounds.getY() + (mBounds.getHeight() / 2);
		
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
		    // Object can completely fit within the right quadrants
		    else if (rect.left > verticalMidpoint) {
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
		if(mNodes[0] != null){
			int index = getIndex(sceneNode.getBoundingRect());
			
			if(index != -1){
				mNodes[index].insert(sceneNode);
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
}
