package de.dungeonrunner.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.nodes.AnimationNode;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.QuadTree;

public class PlayerEntity extends GameEntity {

	private final Vector2f mInitialVelocity = new Vector2f(30f, 10f);
	private Vector2f mPrePosition;
	private List<SceneNode> mCollisionObjects;

	public PlayerEntity(TextureID textureID) {
		mProperties.setProperty("BlockVolume", "true");
		mPrePosition = Vector2f.ZERO;
		mCollisionObjects = new ArrayList<>();
		AnimationNode mIdleAnimation = new AnimationNode(new Sprite(TextureHolder.getInstance().getTexture(textureID)));
		mIdleAnimation.setDuration(Time.getMilliseconds(900));
		mIdleAnimation.setRepeat(true);
		mIdleAnimation.setNumFrames(6);
		mIdleAnimation.setFrameSize(new Vector2i(128, 128));
		setAnimation(mIdleAnimation);
		setVelocity(mInitialVelocity);
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		super.drawCurrent(target, states);
	}

	@Override
	protected void updateCurrent(Time dt) {
		super.updateCurrent(dt);
	}

	@Override
	public void checkCollision(QuadTree collisionTree) {
		mCollisionObjects.clear();
		collisionTree.retrieve(mCollisionObjects, getBoundingRect());

		for (SceneNode node : this.getSceneGraph()) {
			mCollisionObjects.remove(node);
		}

		mCollisionObjects.remove(this);
		System.out.println("Retrieved Collision " + mCollisionObjects.size());
		for (SceneNode node : mCollisionObjects) {
			node.mColor = Color.BLUE;
			processCollision(node);
		}
	}

	private void processCollision(SceneNode node) {
		if (Boolean.valueOf(node.getProperty("BlockVolume"))) {
			FloatRect intersection1 = node.getBoundingRect().intersection(getBoundingRect());
			if (intersection1 == null) {
				return;
			}

			// TODO rounded intersection needed?
			FloatRect intersection = new FloatRect(intersection1.left, intersection1.top,
					Math.round(intersection1.width), Math.round(intersection1.height));

			node.mColor = Color.BLACK;

			if (intersection1.width > intersection1.height) {
				//Player inbound from Top or Bottom
				if (getBoundingRect().top < intersection1.top) {
					// Collision from top
					setPosition(getWorldPosition().x, getWorldPosition().y - intersection1.height);
				} else {
					// Collision from bottom
					setPosition(getWorldPosition().x, getWorldPosition().y + intersection1.height);
				}
			} else {
				if (getBoundingRect().left < intersection1.left) {
					// Collision from the right
					setPosition(getWorldPosition().x - intersection1.width, getWorldPosition().y);
				} else {
					setPosition(getWorldPosition().x + intersection1.width, getWorldPosition().y);
				}
			}
		}
	}
}
