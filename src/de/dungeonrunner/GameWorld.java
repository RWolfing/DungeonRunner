package de.dungeonrunner;

import java.awt.Rectangle;
import java.util.HashMap;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.entities.PlayerEntity;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.QuadTree;
import de.dungeonrunner.util.TmxMapLoader;
import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;

public class GameWorld {

	private enum RenderLayers {
		Background, Middleground, Foreground
	}

	private RenderWindow mRenderWindow;
	private SceneNode mSceneGraph;
	private QuadTree mCollisionTree;

	private HashMap<RenderLayers, SceneNode> mRenderLayers;

	private Map mMap;
	private Vector2f mSpawnPosition;
	private boolean mIsPausing = false;

	private PlayerEntity mPlayerEntity;
	private View mCamera;

	private CommandStack mCommandStack;

	public GameWorld(RenderWindow window) {
		mRenderWindow = window;
		mCamera = new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2),
				new Vector2f(mRenderWindow.getSize()));
		mRenderWindow.setView(mCamera);
		mSpawnPosition = new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2);
		mCommandStack = new CommandStack();
		loadMap();
		loadTextures();
		buildScene();
	}

	private void loadTextures() {
		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
		}
	}

	private void loadMap() {
		mMap = TmxMapLoader.loadMap(Constants.MAP_DIR + "CollisionTest.tmx");

		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
		}
	}

	private void buildScene() {
		mSceneGraph = new SceneNode();
		mRenderLayers = new HashMap<>();
		mCollisionTree = new QuadTree(0, new FloatRect(-5, -5, mMap.getWidth() * mMap.getTileWidth(),
				(mMap.getHeight() * mMap.getTileHeight()) + 50));

		for (RenderLayers layer : RenderLayers.values()) {
			SceneNode node = new SceneNode();
			mRenderLayers.put(layer, node);
			mSceneGraph.attachChild(node);
		}

		if (mMap != null) {
			for (int i = 0; i < mMap.getLayerCount(); i++) {
				if (mMap.getLayer(i) instanceof TileLayer) {
					TileLayer tileLayer = (TileLayer) mMap.getLayer(i);
					final int tileWidth = mMap.getTileWidth();
					final int tileHeight = mMap.getTileHeight();
					final Rectangle boundsInTiles = tileLayer.getBounds();

					TextureHolder textureHolder = TextureHolder.getInstance();

					for (int x = 0; x < boundsInTiles.getWidth(); x++) {
						for (int y = 0; y < boundsInTiles.getHeight(); y++) {
							Tile tile = tileLayer.getTileAt(x, y);
							if (tile == null) {
								continue;
							} else {
								Sprite cachedTile = new Sprite();
								cachedTile.setTexture(textureHolder.getTileTexture(tile.getId()));

								SpriteNode node = new SpriteNode(cachedTile);
								node.setPosition(x * tileWidth, y * tileHeight);
								node.setProperties(tile.getProperties());

								switch (i) {
								case 0:
									mRenderLayers.get(RenderLayers.Background).attachChild(node);
									break;
								case 1:
									mRenderLayers.get(RenderLayers.Middleground).attachChild(node);
									break;
								default:
									mRenderLayers.get(RenderLayers.Foreground).attachChild(node);
								}
							}
						}
					}
				}
			}
		}

		mPlayerEntity = new PlayerEntity(TextureID.ANIM_IDLE);
		mPlayerEntity.setPosition(mSpawnPosition);
		mRenderLayers.get(RenderLayers.Middleground).attachChild(mPlayerEntity);
	}

	public void handleEvent(Event event) {

	}

	public void draw() {
		mRenderWindow.setView(mCamera);
		mRenderWindow.draw(mSceneGraph);
		mRenderWindow.draw(mCollisionTree);
	}

	public void update(Time dt) {
		if (!mIsPausing) {
			while (!mCommandStack.isEmpty()) {
				mSceneGraph.onCommand(mCommandStack.pop());
			}
			mSceneGraph.update(dt);
			checkCollision();
			adaptCameraPosition();
		}
	}

	public CommandStack getCommandStack() {
		return mCommandStack;
	}

	private void checkCollision() {
		mCollisionTree.clear();
		for (SceneNode node : mSceneGraph.getSceneGraph()) {
			if (node.getBoundingRect() != null) {
				mCollisionTree.insert(node);
			}
		}
		mSceneGraph.checkCollisions(mCollisionTree);
	}

	private void adaptCameraPosition() {
		mCamera = new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2),
				new Vector2f(mRenderWindow.getSize()));
		mCamera.setCenter(mPlayerEntity.getPosition());
	}
}
