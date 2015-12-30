package de.dungeonrunner;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.StateHolder;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameOverState;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.state.LevelCompletedState;
import de.dungeonrunner.state.MainMenuState;
import de.dungeonrunner.state.PauseMenuState;
import de.dungeonrunner.state.StateStack;
import de.dungeonrunner.state.States;
import de.dungeonrunner.state.TitleState;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;

public class Application {

	private final Time FPS = Time.getSeconds(1.0f / 60.0f);
	private static RenderWindow mRenderWindow;
	private Clock mClock;
	private StateStack mStateStack;
	private PlayerController mPlayer;
	private boolean mIsPausing = false;

	public Application() {
		mRenderWindow = new RenderWindow();
//		 mRenderWindow.create(VideoMode.getFullscreenModes()[0],
//		 "A Miners Day", RenderWindow.FULLSCREEN);
		mRenderWindow.create(new VideoMode(800, 600, 16), "A Miners Day");
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
		texHolder.loadTexture(TextureID.ANIM_EXPLOSION, Constants.ANIM_DIR + "anim_explosion.png");
		texHolder.loadTexture(TextureID.STONE, Constants.TEX_DIR + "stone.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_THROW, Constants.ANIM_DIR + "hero_miner_throw.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_ATTACK, Constants.ANIM_DIR + "hero_miner_attack.png");
		texHolder.loadTexture(TextureID.ANIM_MINER_DEATH, Constants.ANIM_DIR + "hero_miner_die.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_IDLE, Constants.ANIM_DIR + "stone_thrower_idle.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_ATTACK, Constants.ANIM_DIR + "stone_thrower_attack.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_WALK, Constants.ANIM_DIR + "stone_thrower_walk.png");
		texHolder.loadTexture(TextureID.ANIM_STONE_THROWER_DEATH, Constants.ANIM_DIR + "stone_thrower_death.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL_BIG, Constants.TEX_DIR + "crystal_big.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL_NORMAL, Constants.TEX_DIR + "crystal_normal.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL_SMALL, Constants.TEX_DIR + "crystal_small.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL_MINED_SMALL, Constants.TEX_DIR + "crystal_mined.png");
		texHolder.loadTexture(TextureID.ITEM_CRYSTAL_MINED, Constants.TEX_DIR + "crystal_mined_standard.png");
		texHolder.loadTexture(TextureID.DIAMOND, Constants.IMG_DIR + "diamond.png");
		texHolder.loadTexture(TextureID.LEVEL_EXIT_OPEN, Constants.IMG_DIR + "door_open.png");
		texHolder.loadTexture(TextureID.LEVEL_EXIT_CLOSED, Constants.IMG_DIR + "door_closed.png");
		
		// Menu
		texHolder.loadTexture(TextureID.MAIN_MENU_SCREEN, Constants.IMG_DIR + "miners_day_background.png");
		texHolder.loadTexture(TextureID.BUTTON_DEFAULT, Constants.IMG_DIR + "button_normal.png");
		texHolder.loadTexture(TextureID.BUTTON_SELECTED, Constants.IMG_DIR + "button_selected.png");
		texHolder.loadTexture(TextureID.BUTTON_ACTIVATED, Constants.IMG_DIR + "button_activated.png");
		texHolder.loadTexture(TextureID.ICON_FAILURE, Constants.IMG_DIR + "failure_icon.png");
		texHolder.loadTexture(TextureID.ICON_SUCCESS, Constants.IMG_DIR + "success_icon.png");
		//texHolder.loadTexture(Texture, Constants.IMG_DIR + "button_activated.png");
		
		//UI
		texHolder.loadTexture(TextureID.UI_LIFEBAR_BG, Constants.IMG_DIR + "ui_lifebar.png");
		texHolder.loadTexture(TextureID.UI_LIFEBAR, Constants.IMG_DIR + "lifebar.png");
		texHolder.loadTexture(TextureID.UI_AMMO, Constants.IMG_DIR + "ui_ammo.png");
		texHolder.loadTexture(TextureID.UI_AMMO_INDICATOR, Constants.IMG_DIR + "ui_ammo_indicator.png");
		texHolder.loadTexture(TextureID.UI_DIAMOND, Constants.IMG_DIR + "ui_diamond.png");

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
				processEvents();
				update(FPS);
			}
			render();
		}
	}

	public static RenderWindow getRenderWindow() {
		return mRenderWindow;
	}

	private void processEvents() {
		for (Event event : mRenderWindow.pollEvents()) {
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
			case RESIZED:
				mRenderWindow
						.setView(new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y / 2),
								new Vector2f(mRenderWindow.getSize())));
			default:
				break;
			}
			mStateStack.handleEvent(event);
		}
	}

	private void update(Time fPS2) {
		if (!mIsPausing) {
			mStateStack.update(fPS2);
			if (mStateStack.isEmpty()) {
				mRenderWindow.close();
			}
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
		holder.registerState(States.Menu, new MainMenuState(mStateStack, ctx));
		holder.registerState(States.Pause, new PauseMenuState(mStateStack, ctx));
		holder.registerState(States.GameOver, new GameOverState(mStateStack, ctx));
		holder.registerState(States.LevelSuccess, new LevelCompletedState(mStateStack, ctx));
	}

	public static void main(String[] args) {
		Application application = new Application();
		application.run();
	}
}
