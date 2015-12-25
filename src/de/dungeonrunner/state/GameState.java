package de.dungeonrunner.state;


import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import de.dungeonrunner.GameWorld;
import de.dungeonrunner.PlayerController;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.view.Container;
import de.dungeonrunner.view.UILifebar;

public class GameState extends State {

	private GameWorld mWorld;
	private PlayerController mPlayerController;
	private Container mUIContainer;

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
		setupUI();
	}

	@Override
	protected void onWindowResized(Vector2i newSize) {
		super.onWindowResized(newSize);
		mWorld.resizeWorld(new Vector2f(newSize));
	}
	
	private void setupUI(){
		mUIContainer = new Container();
		UILifebar mUiLifebar = new UILifebar(TextureID.UI_LIFEBAR_BG, TextureID.UI_LIFEBAR);
		mUiLifebar.setPosition(0, 0);
		
		mUIContainer.pack(mUiLifebar);
	}
}
