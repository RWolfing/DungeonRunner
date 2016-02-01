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
import de.dungeonrunner.entities.PlayerUnit;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.view.GameUI;

/**
 * The State to represent the actual game. It contains the UI of the game and
 * the GameWorld.
 * 
 * @author Robert Wolfinger
 *
 */
public class GameState extends State {

	private static GameWorld mWorld;
	private PlayerController mPlayerController;
	private static GameUI mUIContainer;

	// Member to check if this state was pushed
	private boolean mStatePushed;

	/**
	 * Default constructor, creates the State from the given StateStack and
	 * Context.
	 * 
	 * @param stack
	 *            the stack
	 * @param context
	 *            the context
	 */
	public GameState(StateStack stack, Context context) {
		super(stack, context);
		mPlayerController = context.mPlayer;
	}

	@Override
	public void draw() {
		super.draw();
		// First we draw the world
		mWorld.draw();
		RenderWindow window = getContext().getRenderWindow();
		// Reset the camera and draw the UI of the game
		window.setView(getCamera());
		window.draw(mUIContainer);
	}

	@Override
	public boolean update(Time dt) {
		if (mWorld.validWorld()) {
			// First we update the game world
			mWorld.update(dt);
			// Handle the realtime input from the PlayerController and add it to
			// the commands
			mPlayerController.handleRealtimeInput(mWorld.getCommandStack());

			// Check if the game is over
			if (mWorld.checkGameFinished()) {
				// If the game is over, check if the player failed or succeeded
				States requestState;
				if (mWorld.levelSuccess()) {
					requestState = States.LevelSuccess;
				} else {
					requestState = States.GameOver;
				}
				// Finally push the correct state
				if (!mStatePushed) {
					requestStackPush(requestState);
					mStatePushed = true;
				}
			}
		}
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		// Pass all events to the PlayerController, with the command stack of
		// the world
		mPlayerController.handleEvent(event, mWorld.getCommandStack());

		// Check for events that should be handled by the GameState
		switch (event.type) {
		case KEY_RELEASED:
			// For P and ESCAPE we push the PauseMenu
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
		// Initially we create the world and setup the ui
		mWorld = new GameWorld();
		if(!mWorld.validWorld()){
			System.err.println("World is missing map or player!");
		} else {
			System.out.println("Successfully created game world!");
		}
		mStatePushed = false;
		mUIContainer = new GameUI(Application.getRenderWindow().getSize());
		mUIContainer.getAmmoComponent().setCurrentAmmo(PlayerUnit.START_AMMUNITION);
	}

	@Override
	public void onStateResumed() {
		super.onStateResumed();
		mStatePushed = false;
	}

	@Override
	protected void onWindowResized(Vector2i newSize) {
		super.onWindowResized(newSize);
		// if the window was resized we have to resize the world too
		mWorld.resizeWorld(new Vector2f(newSize));
	}

	/**
	 * Method to return the current GameWorld.
	 * 
	 * @return returns the world
	 */
	public static GameWorld getWorld() {
		return mWorld;
	}

	/**
	 * Method to return the UI of the game.
	 * 
	 * @return the GameUI
	 */
	public static GameUI getGameUI() {
		return mUIContainer;
	}
}
