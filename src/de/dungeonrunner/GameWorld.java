package de.dungeonrunner;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.SceneNode.CollisionPair;
import de.dungeonrunner.TextureHolder.TextureID;
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

	private HashMap<RenderLayers, SceneNode> mRenderLayers;

	private Map mMap;
	private SFMLTileRenderer mMapRenderer;

	private FloatRect mWorldBounds;
	private Vector2f mSpawnPosition;
	private PlayerEntity mPlayer;

	public GameWorld(RenderWindow window) {
		mRenderWindow = window;
		mWorldView = new View(new Vector2f(window.getSize().x / 2, window.getSize().y / 2),
				new Vector2f(window.getSize()));
		window.setView(mWorldView);
		mSpawnPosition = new Vector2f(window.getSize().x / 2, window.getSize().y / 2);
	
		loadMap();
		loadTextures();
		buildScene();
	}

	private void loadTextures() {
		TextureHolder holder = TextureHolder.getInstance();
		holder.loadTexture(TextureID.ANIM_IDLE, Constants.ANIM_DIR + "hero_idle_anim.png");
		holder.loadTexture(TextureID.PLAYER_TEXTURE, Constants.ANIM_DIR + "player_stand.png");
		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
		}
	}

	private void loadMap() {
		mMapRenderer = null;
		mMap = TmxMapLoader.loadMap(Constants.MAP_DIR + "CollisionTest.tmx");

		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
			mMapRenderer = new SFMLTileRenderer(mMap);
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

		mPlayer = new PlayerEntity(TextureID.ANIM_IDLE);
		mRenderLayers.get(RenderLayers.Middleground).attachChild(mPlayer);
		mPlayer.setPosition(100f, 0f);
	}

	public void draw(RenderWindow renderWinodw) {
		renderWinodw.setView(new View(mWorldView.getCenter(), mWorldView.getSize()));
		renderWinodw.draw(mSceneGraph);
	}

	public void update(Time dt) {
		mSceneGraph.update(dt);
		Set<CollisionPair> collisionPairs = new HashSet<>();
		mSceneGraph.checkSceneCollision(mSceneGraph, collisionPairs);
	}
}
