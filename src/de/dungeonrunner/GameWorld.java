package de.dungeonrunner;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.entities.PlayerEntity;
import de.dungeonrunner.nodes.CollisionPair;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.TmxMapLoader;
import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;

public class GameWorld {

	private enum RenderLayers {
		Background, Middleground, Foreground
	}

	private RenderWindow mRenderWindow;
	private View mWorldView;
	private SceneNode mSceneGraph;
	private SceneNode mCollisionGraph;

	private HashMap<RenderLayers, SceneNode> mRenderLayers;

	private Map mMap;
	private Vector2f mSpawnPosition;
	private PlayerEntity mPlayer;

	public GameWorld(Context context) {
		mRenderWindow = context.mRenderWindow;
		mWorldView = new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2),
				new Vector2f(mRenderWindow.getSize()));
		mRenderWindow.setView(mWorldView);
		mSpawnPosition = new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2);
		mPlayer = context.mPlayer;
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

		mRenderLayers.get(RenderLayers.Middleground).attachChild(mPlayer);
		mPlayer.setPosition(mSpawnPosition);
	}

	public void handleEvent(Event event) {
		switch (event.type) {
		default:
			break;
		}
	}

	public void draw() {
		View view = new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2),
				new Vector2f(mRenderWindow.getSize()));
		view.setCenter(mPlayer.getPosition());
		mRenderWindow.setView(view);
		mRenderWindow.draw(mSceneGraph);
	}

	public void update(Time dt) {
		mSceneGraph.update(dt);
		Set<CollisionPair> collisionPairs = new HashSet<>();
		mSceneGraph.checkSceneCollision(mRenderLayers.get(RenderLayers.Middleground), collisionPairs);
	}
}
