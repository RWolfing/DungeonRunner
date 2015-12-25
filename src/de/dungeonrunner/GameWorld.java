package de.dungeonrunner;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.entities.CrystalItem;
import de.dungeonrunner.entities.Item;
import de.dungeonrunner.entities.LeashedUnit;
import de.dungeonrunner.entities.PlayerUnit;
import de.dungeonrunner.entities.Spikes;
import de.dungeonrunner.entities.StoneThrower;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.QuadTree;
import de.dungeonrunner.util.TmxKeys;
import de.dungeonrunner.util.TmxMapLoader;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;

public class GameWorld {

	private enum RenderLayers {
		Background, Levelbackground, Levelmiddleground, Levelforeground
	}

	private SceneNode mSceneGraph;
	private QuadTree mCollisionTree;

	private HashMap<RenderLayers, SceneNode> mRenderLayers;

	private Map mMap;
	private boolean mIsPausing = false;

	private PlayerUnit mPlayerEntity;
	private View mCamera;

	private CommandStack mCommandStack;
	private static GameWorld mWorldInstance;

	public GameWorld() {
		mCommandStack = new CommandStack();
		mWorldInstance = this;
		loadMap();
		loadTextures();
		buildScene();
		resizeWorld(new Vector2f(Application.getRenderWindow().getSize()));
	}

	private void loadTextures() {
		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
		}
	}

	private void loadMap() {
		mMap = TmxMapLoader.loadMap(Constants.MAP_DIR + "test" + File.separator + "minetest.tmx");

		if (mMap != null) {
			TextureHolder.getInstance().loadTiledTextures(mMap);
		}
	}

	private void buildScene() {
		mSceneGraph = new SceneNode(null);
		mRenderLayers = new HashMap<>();
		mCollisionTree = new QuadTree(0, new FloatRect(-5, -5, mMap.getWidth() * mMap.getTileWidth(),
				(mMap.getHeight() * mMap.getTileHeight()) + 50));

		createLevelScene();
		createLevelEntities();
	}

	public void handleEvent(Event event) {

	}

	public void draw() {
		RenderWindow window = Application.getRenderWindow();
		window.setView(mCamera);
		window.draw(mSceneGraph);
		window.draw(mCollisionTree);
	}

	public void update(Time dt) {
		if (!mIsPausing) {
			executeCommands();
			mSceneGraph.update(dt);
			checkCollision();
			mSceneGraph.cleanDestroyedNodes();
			adaptCameraPosition();
		}
	}

	public CommandStack getCommandStack() {
		return mCommandStack;
	}

	public QuadTree getCollisionGraph() {
		return mCollisionTree;
	}

	public static GameWorld getGame() {
		return mWorldInstance;
	}

	private void executeCommands() {
		// First handle input commands
		while (!mCommandStack.isEmpty()) {
			mSceneGraph.onCommand(mCommandStack.pop());
		}
		// Collect entity commands
		mSceneGraph.collectCommands(mCommandStack);
		recursiveSceneOnCommad();
	}

	private void recursiveSceneOnCommad() {
		if (mCommandStack.isEmpty())
			return;

		while (!mCommandStack.isEmpty()) {
			mSceneGraph.onCommand(mCommandStack.pop());
		}
		mSceneGraph.collectCommands(mCommandStack);
		recursiveSceneOnCommad();
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

	/**
	 * Adapt the camera position, to only see the level with the player centered
	 * if possible
	 */
	private void adaptCameraPosition() {
		// player must not be null
		if (mPlayerEntity != null) {
			// Precalculate the position of the camera if we move with the
			// player
			Vector2f preCalculatedPosition = mPlayerEntity.getPosition();
			// Bounds of the level in the world
			float leftBorder = 0;
			float rightBorder = mMap.getBounds().width * mMap.getTileWidth();
			float topBorder = 0;
			float bottomBorder = mMap.getBounds().height * mMap.getTileHeight();

			boolean isXPossible = true;
			boolean isYPossible = true;
			// Check if we can move the camera in x/y direction and still stay
			// in the level bounds
			if ((preCalculatedPosition.x - mCamera.getSize().x / 2 < leftBorder)
					|| (preCalculatedPosition.x + mCamera.getSize().x / 2 > rightBorder)) {
				isXPossible = false;
			}

			if ((preCalculatedPosition.y - mCamera.getSize().y / 2 < topBorder)
					|| (preCalculatedPosition.y + mCamera.getSize().y / 2) > bottomBorder) {
				isYPossible = false;
			}

			// Only move the camera as long as it stays in the level bounds
			if (isXPossible && isYPossible) {
				mCamera.setCenter(preCalculatedPosition);
			} else if (isXPossible) {
				mCamera.setCenter(preCalculatedPosition.x, mCamera.getCenter().y);
			} else if (isYPossible) {
				mCamera.setCenter(mCamera.getCenter().x, preCalculatedPosition.y);
			}
		}
	}

	public void resizeWorld(Vector2f size) {
		mCamera = new View(new Vector2f(size.x / 2, size.y / 2), size);
	}

	private void createLevelScene() {
		for (RenderLayers layer : RenderLayers.values()) {
			SceneNode node = new SceneNode(null);
			switch (layer) {
			case Levelforeground:
				node.setNodeType(NodeType.WORLD);
				break;
			default:
				break;
			}
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

								SpriteNode node;
								switch (tile.getId()) {
								case 4:
									node = new Spikes(cachedTile, tile.getProperties());
									break;
								default:
									node = new SpriteNode(cachedTile, tile.getProperties());

								}
								node.setPosition(x * tileWidth, y * tileHeight);

								if (tileLayer.getName().equals(TmxKeys.TILE_LAYER_BG)) {
									mRenderLayers.get(RenderLayers.Background).attachChild(node);
								} else if (tileLayer.getName().equals(TmxKeys.TITLE_LAYER_LEVEL_BG)) {
									mRenderLayers.get(RenderLayers.Levelbackground).attachChild(node);
								} else if (tileLayer.getName().equals(TmxKeys.TITLE_LAYER_LEVEL_MIDDLE)) {
									mRenderLayers.get(RenderLayers.Levelmiddleground).attachChild(node);
								} else if (tileLayer.getName().equals(TmxKeys.TITLE_LAYER_LEVEL_FRONT)) {
									mRenderLayers.get(RenderLayers.Levelforeground).attachChild(node);
								} else {
									System.err
											.println("GameWorld, Could not find the tilelayer " + tileLayer.getName());
								}
							}
						}
					}
				}
			}
		}
	}

	private void createLevelEntities() {
		if (mMap != null) {
			for (MapLayer layer : mMap.getLayers()) {
				if (layer instanceof ObjectGroup) {
					ObjectGroup objGroup = (ObjectGroup) layer;

					for (Iterator<MapObject> i = objGroup.getObjects(); i.hasNext();) {
						MapObject object = i.next();

						// Player
						if (object.getType().equals(TmxKeys.OBJECT_TAG_PLAYER)) {
							mPlayerEntity = new PlayerUnit(TextureID.ANIM_IDLE, object.getProperties());
							float spawnX = Float.valueOf(mPlayerEntity.getProperty(TmxKeys.OBJECT_SPAWN_X, "0"));
							float spawnY = Float.valueOf(mPlayerEntity.getProperty(TmxKeys.OBJECT_SPAWN_Y, "0"));
							mPlayerEntity.setPosition(spawnX, spawnY);
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(mPlayerEntity);
							continue;
						}

						// Enemy
						if (object.getType().equals(TmxKeys.OBJECT_TAG_ENEMY)) {
							LeashedUnit eunit = new StoneThrower(TextureID.ENEMY, object.getProperties());
							eunit.setLeashBounds((float) object.getBounds().x, (float) object.getBounds().y,
									(float) object.getBounds().width, (float) object.getBounds().height);
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(eunit);
						}

						// Item
						if (object.getType().equals(TmxKeys.OBJECT_TAG_CRYSTAL)) {
							Item item = new CrystalItem(TextureID.ITEM_CRYSTAL, object.getProperties());
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(item);
							item.setPosition((float) object.getX(), (float) object.getY());
						}
					}
				}
			}
		}
	}
}
