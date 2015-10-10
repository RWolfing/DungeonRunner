package de.dungeonrunner;

import java.util.HashMap;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.TextureHolder.TextureID;
import tiled.core.Map;

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
			mMapRenderer = new SFMLTileRenderer(mMap);
			mRenderLayers.get(RenderLayers.Background).attachChild(mMapRenderer);
		}
		
		mPlayer = new PlayerEntity(TextureID.ANIM_IDLE);
		mRenderLayers.get(RenderLayers.Middleground).attachChild(mPlayer);
	}
	
	public void draw() {
		mRenderWindow.setView(new View(mWorldView.getCenter(), mWorldView.getSize()));
		mRenderWindow.draw(mSceneGraph);
	}

	public void update(Time dt) {
		mSceneGraph.update(dt);
	}
}
