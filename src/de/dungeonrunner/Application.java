package de.dungeonrunner;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.StateHolder;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.state.StateStack;
import de.dungeonrunner.state.States;
import de.dungeonrunner.state.TitleState;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;

public class Application {

	private final Time FPS = Time.getSeconds(1.0f / 60.0f);
	private RenderWindow mRenderWindow;
	private Clock mClock;
	private StateStack mStateStack;
	private PlayerController mPlayer;
	private boolean mIsPausing = false;

	public Application() {
		mRenderWindow = new RenderWindow();
		mRenderWindow.create(new VideoMode(800, 480, 32), "DungeonRunner");
		mRenderWindow.setFramerateLimit(30);

		mClock = new Clock();

		TextureHolder texHolder = TextureHolder.getInstance();
		texHolder.loadTexture(TextureID.ANIM_IDLE, Constants.ANIM_DIR + "hero_miner_idle.png");
		texHolder.loadTexture(TextureID.PLAYER_TEXTURE, Constants.ANIM_DIR + "player_stand.png");
		texHolder.loadTexture(TextureID.BULLET, Constants.TEX_DIR + "projectile.png");
		texHolder.loadTexture(TextureID.TITLE_BG_SCREEN, Constants.IMG_DIR + "title_screen_background.jpg");
		texHolder.loadTexture(TextureID.ENEMY, Constants.TEX_DIR + "enemy.png");
		texHolder.loadTexture(TextureID.ANIM_PLAYER_RUN, Constants.ANIM_DIR + "hero_miner_run.png");
		texHolder.loadTexture(TextureID.ANIM_PLAYER_JUMP, Constants.ANIM_DIR + "hero_miner_jump.png");
		texHolder.loadTexture(TextureID.DYNAMITE_SINGLE, Constants.TEX_DIR + "dynamite.png");
		texHolder.loadTexture(TextureID.STONE, Constants.TEX_DIR + "stone.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_THROW, Constants.ANIM_DIR + "hero_miner_throw.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_ATTACK, Constants.ANIM_DIR + "hero_miner_attack.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_DEATH, Constants.ANIM_DIR + "hero_miner_die.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_IDLE, Constants.ANIM_DIR + "stone_thrower_idle.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_ATTACK, Constants.ANIM_DIR + "stone_thrower_attack.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_WALK, Constants.ANIM_DIR + "stone_thrower_walk.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_DEATH, Constants.ANIM_DIR + "stone_thrower_death.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL, Constants.TEX_DIR + "crystal_blue.png");
		

		FontHolder.getInstance().loadFont(FontID.DUNGEON_FONT, Constants.RES_DIR + "dungeon_font.ttf");

		mStateStack = new StateStack();
		mPlayer = new PlayerController();

		registerStates();
		mStateStack.pushState(States.Title);
	}

	public void run() {
		Time timeSinceLastUpdate = Time.ZERO;
		while (mRenderWindow.isOpen()) {
			timeSinceLastUpdate = Time.add(timeSinceLastUpdate, mClock.restart());
			while (timeSinceLastUpdate.asMicroseconds() > FPS.asMilliseconds()) {
				timeSinceLastUpdate = Time.sub(timeSinceLastUpdate, FPS);
				long time = System.currentTimeMillis();
				processEvents();
				time = System.currentTimeMillis() - time;
				// TODO remove
				// System.out.println("Update: " + time);
				update(FPS);
			}
			long time = System.currentTimeMillis();
			render();
			time = System.currentTimeMillis() - time;
			// TODO remove
			// System.out.println("Draw: " + time);
		}
	}

	private void processEvents() {
		for (Event event : mRenderWindow.pollEvents()) {
			mStateStack.handleEvent(event);
			switch (event.type) {
			case CLOSED:
				mRenderWindow.close();
				break;
			case GAINED_FOCUS:
				mIsPausing = false;
				break;
			case LOST_FOCUS:
				mIsPausing = true;
				break;
			default:
				break;
			}
		}
	}

	private void update(Time fPS2) {
		if (!mIsPausing) {
			mStateStack.update(fPS2);
		}
	}

	private void render() {
		mRenderWindow.clear();
		mRenderWindow.setView(mRenderWindow.getDefaultView());
		mStateStack.draw();
		mRenderWindow.display();

	}

	private void registerStates() {
		Context ctx = new Context(mRenderWindow, mPlayer);
		StateHolder holder = StateHolder.getInstance();
		holder.registerState(States.Title, new TitleState(mStateStack, ctx));
		holder.registerState(States.Game, new GameState(mStateStack, ctx));
	}

	public static void main(String[] args) {
		Application application = new Application();
		application.run();
	}
}
