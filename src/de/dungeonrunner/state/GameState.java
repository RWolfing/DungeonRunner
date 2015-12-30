package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import de.dungeonrunner.Application;
import de.dungeonrunner.GameWorld;
import de.dungeonrunner.PlayerController;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.view.GameUI;

public class GameState extends State {

	private static GameWorld mWorld;
	private PlayerController mPlayerController;
	private static GameUI mUIContainer;

	private boolean mStatePushed;

	public GameState(StateStack stack, Context context) {
		super(stack, context);
		mPlayerController = context.mPlayer;
	}

	@Override
	public void draw() {
		super.draw();
		mWorld.draw();
		RenderWindow window = getContext().getRenderWindow();
		window.setView(getCamera());
		window.draw(mUIContainer);
	}

	@Override
	public boolean update(Time dt) {
		mWorld.update(dt);
		mPlayerController.handleRealtimeInput(mWorld.getCommandStack());
		if (mWorld.checkGameFinished()) {
			States requestState;
			if (mWorld.levelSuccess()) {
				requestState = States.LevelSuccess;
			} else {
				requestState = States.GameOver;
			}
			if (!mStatePushed) {
				requestStackPush(requestState);
				mStatePushed = true;
			}
		}
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		mPlayerController.handleEvent(event, mWorld.getCommandStack());
		switch (event.type) {
		case KEY_RELEASED:
			if (event.asKeyEvent().key == Key.P || event.asKeyEvent().key == Key.ESCAPE) {
				requestStackPush(States.Pause);
			}
			break;
		default:
			break;
		}
		return super.handleEvent(event);
	}

	@Override
	protected void onStateSetup() {
		mWorld = new GameWorld();
		mStatePushed = false;
		setupUI();
	}

	@Override
	public void onStateResumed() {
		super.onStateResumed();
		mStatePushed = false;
	}

	@Override
	protected void onWindowResized(Vector2i newSize) {
		super.onWindowResized(newSize);
		mWorld.resizeWorld(new Vector2f(newSize));
	}

	public static GameWorld getWorld() {
		return mWorld;
	}

	public static GameUI getGameUI() {
		return mUIContainer;
	}

	private void setupUI() {
		mUIContainer = new GameUI(Application.getRenderWindow().getSize());
	}
}
