package de.dungeonrunner.state;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Container;
import de.dungeonrunner.view.Label;
import de.dungeonrunner.view.Button.OnButtonClick;

/**
 * The state to represent the level completed screen of the game.
 * 
 * @author Robert Wolfinger
 *
 */
public class LevelCompletedState extends State{

	private Label mSuccessTextLabel;
	private Label mSuccessIconLabel;
	private Button mExitButton;
	
	private RectangleShape mBackgroundOverlay;
	
	private Container mGUIContainer;
	
	/**
	 * Default constructor, creates a new State with the given StateStack
	 * and Context.
	 * 
	 * @param stack the StateStack
	 * @param context the Context
	 */
	public LevelCompletedState(StateStack stack, Context context) {
		super(stack, context);
		
		mSuccessTextLabel = new Label("SUCCESS");
		mSuccessTextLabel.setCentered(true);
		mSuccessTextLabel.setHeight(Constants.MENU_ITEM_HEIGHT);
		mSuccessTextLabel.setCharSize(25);

		mSuccessIconLabel = new Label("");
		mSuccessIconLabel.setBackground(TextureID.ICON_SUCCESS);
		mSuccessIconLabel.setWidth(180);
		mSuccessIconLabel.setHeight(180);
		mSuccessIconLabel.setCentered(true);

		mExitButton = new Button("EXIT", TextureID.BUTTON_DEFAULT);
		mExitButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mExitButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mExitButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mExitButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mExitButton.setCentered(true);
		mExitButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				//If the exit button was clicked we clear the
				//stack and return to the menu
				requestStateClear();
				requestStackPush(States.Menu);
			}
		});

		mBackgroundOverlay = new RectangleShape();
		mBackgroundOverlay.setSize(new Vector2f(context.getRenderWindow().getSize()));
		mBackgroundOverlay.setFillColor(new Color(0, 0, 0, 150));

		mGUIContainer = new Container();
		mGUIContainer.pack(mSuccessTextLabel);
		mGUIContainer.pack(mSuccessIconLabel);
		mGUIContainer.pack(mExitButton);
	}

	//TODO false is not going to be handled by the state stack
	@Override
	public boolean handleEvent(Event event) {
		mGUIContainer.handleEvent(event);
		return false;
		//return super.handleEvent(event);
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow renderWindow = getContext().getRenderWindow();
		renderWindow.draw(mBackgroundOverlay);
		renderWindow.draw(mGUIContainer);
	}

	@Override
	public void layout() {
		super.layout();
		//Depending on the window size we layout the components
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		mBackgroundOverlay.setSize(new Vector2f(windowSize));
		Helper.layoutVertically(windowSize, 35, true, 0, 0, mSuccessTextLabel, mSuccessIconLabel, mExitButton);
	}

}
