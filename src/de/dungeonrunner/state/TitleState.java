package de.dungeonrunner.state;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import de.dungeonrunner.Application;
import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Context;

/**
 * The state to represent the title screen of the game.
 * 
 * @author Robert Wolfinger
 *
 */
public class TitleState extends State {

	private Sprite mBackgroundSprite;
	private Text mTitleText;

	/**
	 * Creates the title screen from the given parameters.
	 * 
	 * @param stack the StateStack
	 * @param context the context of the state
	 */
	public TitleState(StateStack stack, Context context) {
		super(stack, context);

		//Creates the needed background and text
		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));
		mTitleText = new Text("Press any key to start", FontHolder.getInstance().getFont(FontID.DUNGEON_FONT));
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow renderWindow = Application.getRenderWindow();
		renderWindow.draw(mBackgroundSprite);
		renderWindow.draw(mTitleText);
	}

	@Override
	public boolean update(Time dt) {
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		if (event.type == Type.KEY_PRESSED) {
			//If any key was pressed, we want to switch to the menu
			requestStackPop();
			requestStackPush(States.Menu);
		}
		return super.handleEvent(event);
	}

	@Override
	public void layout() {
		super.layout();
		//Depending on the screen size we do the layouting of the background and text
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		FloatRect bounds = mBackgroundSprite.getLocalBounds();
		//The background will scale over the whole screen
		mBackgroundSprite.setScale((float) windowSize.x / bounds.width, (float) windowSize.y / bounds.height);

		//The text will be shown in the bottom right corner
		bounds = mTitleText.getLocalBounds();
		mTitleText.setPosition(windowSize.x - bounds.width - windowSize.x / 10,
				windowSize.y - bounds.height - windowSize.y / 5);
	}
}
