package de.dungeonrunner.state;

import org.jsfml.system.Time;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import de.dungeonrunner.GameWorld;
import de.dungeonrunner.PlayerController;
import de.dungeonrunner.util.Context;

public class GameState extends State {

	private GameWorld mWorld;
	private PlayerController mPlayerController;
	private boolean mIsPausing = false;

	public GameState(StateStack stack, Context context) {
		super(stack, context);
		mPlayerController = context.mPlayer;
		mWorld = new GameWorld(context.mRenderWindow);
	}

	@Override
	public void draw() {
		mWorld.draw();
	}

	@Override
	public boolean update(Time dt) {
		if (!mIsPausing) {
			mWorld.update(dt);
			mPlayerController.handleRealtimeInput(mWorld.getCommandStack());
		}
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		mPlayerController.handleEvent(event, mWorld.getCommandStack());
		
		switch (event.type) {
		case KEY_PRESSED:
			if (event.asKeyEvent().key == Key.P) {
				mIsPausing = !mIsPausing;
			}
			break;
		default:
			break;
		}
		
		return true;
	}

}
