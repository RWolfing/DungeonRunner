package de.dungeonrunner;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.entities.CrystalItem;
import de.dungeonrunner.entities.DynamiteItem;
import de.dungeonrunner.entities.Item;
import de.dungeonrunner.entities.LeashedUnit;
import de.dungeonrunner.entities.LevelExit;
import de.dungeonrunner.entities.PlayerUnit;
import de.dungeonrunner.entities.Spikes;
import de.dungeonrunner.entities.StoneThrower;
import de.dungeonrunner.nodes.NodeType;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.nodes.SpriteNode;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.state.LevelSelectionState;
import de.dungeonrunner.util.QuadTree;
import de.dungeonrunner.util.TmxKeys;
import de.dungeonrunner.util.TmxMapLoader;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;

/**
 * This class represents the world or level of the game. It loads and read a
 * .tmx map and creates all necessary entities, nodes etc.
 * 
 * @author Robert Wolfinger
 */
public class GameWorld {

	/**
	 * Enum for the available render layers.
	 */
	private enum RenderLayers {
		Background, Levelbackground, Levelmiddleground, Levelforeground
	}

	private SceneNode mSceneGraph;
	private QuadTree mCollisionTree;
	private CommandStack mCommandStack;

	private View mCamera;
	private HashMap<RenderLayers, SceneNode> mRenderLayers;

	private Map mMap;
	private boolean mIsPausing = false;

	private PlayerUnit mPlayerEntity;
	private LevelExit mLevelExit;
	private boolean mLevelExitUsed;

	/**
	 * Default constructor, creates a new world.
	 */
	public GameWorld() {
		mCommandStack = new CommandStack();
		mSceneGraph = new SceneNode(null);
		mRenderLayers = new HashMap<>();
		mCollisionTree = new QuadTree(0, FloatRect.EMPTY);
		
		// First we load the map
		if (loadMap()) {
			// Then we can setup and build the scene
			// The quadtree has to match the level size
			mCollisionTree = new QuadTree(0, new FloatRect(-5, -5, mMap.getWidth() * mMap.getTileWidth(),
					(mMap.getHeight() * mMap.getTileHeight()) + 50));

			// Create the scene of the level (all necessary tiles, objects etc)
			createLevelScene();
			// Create the entities of the level (units, pickups etc)
			createLevelEntities();
		}
		// Finally we resize the world to match the window size
		resizeWorld(new Vector2f(Application.getRenderWindow().getSize()));
	}
	
	/**
	 * Draws the world.
	 */
	public void draw() {
		RenderWindow window = Application.getRenderWindow();
		window.setView(mCamera);
		window.draw(mSceneGraph);
		// Debug only
		window.draw(mCollisionTree);
	}

	/**
	 * Updates the world.
	 * 
	 * @param dt
	 *            delta time
	 */
	public void update(Time dt) {
		if (!mIsPausing) {
			// Not pausing, update everything
			// First we execute all commands
			executeCommands();
			// Then we update every node in the scene graph
			mSceneGraph.update(dt);
			// After moving the nodes, we check for collisions
			checkCollision();
			// Remove all destroyed nodes
			mSceneGraph.cleanDestroyedNodes();
			// Adapt the camera position the the player position
			adaptCameraPosition();

			// And check if the player has finished the level
			if (checkDiamondsCollected()) {
				mLevelExit.open();
				if (levelSuccess()) {
					mLevelExitUsed = true;
				}
			}
		}
	}

	/**
	 * Returns the command stack of the world.
	 * 
	 * @return the command stack
	 */
	public CommandStack getCommandStack() {
		return mCommandStack;
	}

	/**
	 * Returns the camera of the world.
	 * 
	 * @return the world camera
	 */
	public View getWorldCamera() {
		return mCamera;
	}

	/**
	 * Returns the QuadTree to check for collisions.
	 * 
	 * @return the collision graph
	 */
	public QuadTree getCollisionGraph() {
		return mCollisionTree;
	}

	/**
	 * Executes all pending commands of the world.
	 */
	private void executeCommands() {
		// First handle input commands
		while (!mCommandStack.isEmpty()) {
			mSceneGraph.onCommand(mCommandStack.pop());
		}
		// Collect entity commands
		mSceneGraph.collectCommands(mCommandStack);
		// Through executing a command, maybe a new command
		// has been created so check again until there are no commands
		recursiveSceneOnCommad();
	}

	/**
	 * Executes all commands in a loop and collects resulting commands until no
	 * commands are left
	 */
	private void recursiveSceneOnCommad() {
		if (mCommandStack.isEmpty())
			// All commands executes we can return
			return;

		while (!mCommandStack.isEmpty()) {
			// Execute every pending command
			mSceneGraph.onCommand(mCommandStack.pop());
		}
		// Collect all resulting commands
		mSceneGraph.collectCommands(mCommandStack);
		// And do it again
		recursiveSceneOnCommad();
	}

	/**
	 * Checks for collisions between nodes in the world.
	 */
	private void checkCollision() {
		// we clear the quadtree
		mCollisionTree.clear();
		for (SceneNode node : mSceneGraph.getSceneGraph()) {
			// And put every node that can collide back into
			// the quadtree
			if (node.getBoundingRect() != null) {
				mCollisionTree.insert(node);
			}
		}
		// we check for collisions
		mSceneGraph.checkCollisions(mCollisionTree);
	}

	private boolean loadMap() {
		mMap = TmxMapLoader.loadMap(LevelSelectionState.getChosenMapPath());
		if (mMap != null) {
			// If we could load the map, load the necessary textures for the
			// tiles
			TextureHolder.getInstance().loadTiledTextures(mMap);
			return true;
		}
		System.err.println("GameWorld: Map setup failed!");
		return false;
	}
	
	/**
	 * Creates the scene of the level. The level scene contains the different
	 * tiles and other static objects of the game.
	 */
	private void createLevelScene() {
		// Setup of the different render layers
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
					// We iterate over every TileLayer of the map
					TileLayer tileLayer = (TileLayer) mMap.getLayer(i);
					final int tileWidth = mMap.getTileWidth();
					final int tileHeight = mMap.getTileHeight();
					final Rectangle boundsInTiles = tileLayer.getBounds();

					TextureHolder textureHolder = TextureHolder.getInstance();

					for (int x = 0; x < boundsInTiles.getWidth(); x++) {
						// Through the bounds of the layer we can iterate over
						// all containing tiles
						for (int y = 0; y < boundsInTiles.getHeight(); y++) {
							Tile tile = tileLayer.getTileAt(x, y);
							if (tile == null) {
								continue;
							} else {
								// For each tile we create a new sprite and
								// sprite node
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

								// Depending on the position of the layer we
								// attach the created node to the correct
								// render layer
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

	/**
	 * Creates the entities of the level. Level entities are units, pickups etc.
	 */
	private void createLevelEntities() {
		if (mMap != null) {
			for (MapLayer layer : mMap.getLayers()) {
				// We iterate over every defined ObjectGroup
				if (layer instanceof ObjectGroup) {
					ObjectGroup objGroup = (ObjectGroup) layer;

					for (Iterator<MapObject> i = objGroup.getObjects(); i.hasNext();) {
						MapObject object = i.next();
						// For each object we check what kind it is and create
						// the appropriate
						// game entity.

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
							LeashedUnit eunit = new StoneThrower(TextureID.STONE_THROWER, object.getProperties());
							eunit.setLeashBounds((float) object.getBounds().x, (float) object.getBounds().y,
									(float) object.getBounds().width, (float) object.getBounds().height);
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(eunit);
						}

						// Crystal Items
						if (object.getType().equals(TmxKeys.OBJECT_TAG_CRYSTAL)) {
							Item item = CrystalItem.getCrystalItem(object);
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(item);
						}
						// Dynamite Pickup
						if(object.getType().equals(TmxKeys.OBJECT_TAG_DYNAMITE)) {
							Item item = new DynamiteItem(TextureID.DYNAMITE_SINGLE, object.getProperties());
							item.setPosition((float) object.getX(), (float) object.getY());
							mRenderLayers.get(RenderLayers.Levelforeground).attachChild(item);
						}

						// Level Exit
						if (object.getType().equals(TmxKeys.OBJECT_NAME_LEVELEXIT)) {
							mLevelExit = new LevelExit(TextureID.LEVEL_EXIT_OPEN, TextureID.LEVEL_EXIT_CLOSED,
									object.getProperties());
							mLevelExit.setPosition((float) object.getBounds().x, (float) object.getBounds().y);
							mRenderLayers.get(RenderLayers.Levelbackground).attachChild(mLevelExit);
						}
					}
				}
			}
		}
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

	/**
	 * Resizes the world to match the given size.
	 * 
	 * @param size
	 *            the size of the world
	 */
	public void resizeWorld(Vector2f size) {
		mCamera = new View(new Vector2f(size.x / 2, size.y / 2), size);
	}

	/**
	 * Checks if the game has ended.
	 * 
	 * @return player is dead or level exit was used
	 */
	public boolean checkGameFinished() {
		return mPlayerEntity.getHitpoints() <= 0 || mLevelExitUsed;
	}

	/**
	 * Checks if the level was successfully finished.
	 * 
	 * @return if the player used the level exit
	 */
	public boolean levelSuccess() {
		return mLevelExit.didPlayerEnter();
	}

	/**
	 * Check if all diamonds were collected by the player.
	 * 
	 * @return if all diamonds were collected
	 */
	private boolean checkDiamondsCollected() {
		return GameState.getGameUI().getDiamondsComponent().collectedAll();
	}

	public boolean validWorld() {
		return mMap != null && mPlayerEntity != null;
	}
}
