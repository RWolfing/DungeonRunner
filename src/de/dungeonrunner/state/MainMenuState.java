package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Button.OnButtonClick;
import de.dungeonrunner.view.Container;

/**
 * The state to represent the main menu of the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class MainMenuState extends State {

	private final int MENU_X_OFFSET = 4; //in % off window width ( 100/MENU_X_OFFSET)
	private Sprite mBackgroundTexture;
	private Container mGUIContainer;

	private Button mPlayButton;
	private Button mOptionsButton;
	private Button mExitButton;

	/**
	 * Default constructor, creates the State from the given StateStack
	 * and Context.
	 * 
	 * @param stack the StateStack
	 * @param context the Context
	 */
	public MainMenuState(StateStack stack, Context context) {
		super(stack, context);

		//Setup of the ui components
		mGUIContainer = new Container();
		mBackgroundTexture = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));

		mPlayButton = Button.createButton("PLAY");
		mPlayButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				//If the play button is clicked, we push the level selection
				requestStackPush(States.LevelSelection);
			}
		});

		mOptionsButton = Button.createButton("HELP");
		mOptionsButton.setOnClickListener(new OnButtonClick() {
			
			@Override
			public void onClick(Button bttn) {
				requestStackPush(States.Help);
			}
		});

		mExitButton = Button.createButton("EXIT");
		mExitButton.setOnClickListener(new OnButtonClick() {
			@Override
			public void onClick(Button bttn) {
				//If exist is clicked we just pop the state
				requestStackPop();
			}
		});

		mGUIContainer.pack(mPlayButton);
		mGUIContainer.pack(mOptionsButton);
		mGUIContainer.pack(mExitButton);
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow window = getContext().getRenderWindow();
		window.draw(mBackgroundTexture);
		window.draw(mGUIContainer);
	}

	@Override
	public boolean update(Time dt) {
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		mGUIContainer.handleEvent(event);
		return super.handleEvent(event);
	}

	@Override
	public void layout() {
		super.layout();
		//We layout the components depending on the window size
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		//layout the components vertically like a menu
		Helper.layoutVertically(windowSize, Constants.MENU_ITEM_SPACING, true, windowSize.x / MENU_X_OFFSET, Constants.MENU_ITEM_HEIGHT / 2, mPlayButton,
				mOptionsButton, mExitButton);
		mBackgroundTexture.setScale(windowSize.x / mBackgroundTexture.getLocalBounds().width,
				windowSize.y / mBackgroundTexture.getLocalBounds().height);
	}
}
