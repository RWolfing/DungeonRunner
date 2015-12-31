package de.dungeonrunner.state;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Container;
import de.dungeonrunner.view.Button.OnButtonClick;
import de.dungeonrunner.view.Label;

/**
 * State to represent the game over screen of the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class GameOverState extends State {

	private Label mGameOverTextLabel;
	private Label mGameOverIconLabel;
	private Button mExitButton;

	private RectangleShape mBackgroundOverlay;

	private Container mGUIContainer;

	/**
	 * Default constructor, creates the State with the given
	 * parameters.
	 * 
	 * @param stack the StateStack
	 * @param context the Context
	 */
	public GameOverState(StateStack stack, Context context) {
		super(stack, context);

		//Setup of the ui elements
		mGameOverTextLabel = new Label("FAILURE");
		mGameOverTextLabel.setCentered(true);
		mGameOverTextLabel.setHeight(Constants.MENU_ITEM_HEIGHT);
		mGameOverTextLabel.setCharSize(25);

		mGameOverIconLabel = new Label("");
		mGameOverIconLabel.setBackground(TextureID.ICON_FAILURE);
		mGameOverIconLabel.setWidth(180);
		mGameOverIconLabel.setHeight(180);
		mGameOverIconLabel.setCentered(true);

		mExitButton = new Button("EXIT", TextureID.BUTTON_DEFAULT);
		mExitButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mExitButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mExitButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mExitButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mExitButton.setCentered(true);
		mExitButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				//If the exit button was clicked we clear the stack
				//and push the main menu
				requestStateClear();
				requestStackPush(States.Menu);
			}
		});

		mBackgroundOverlay = new RectangleShape();
		mBackgroundOverlay.setSize(new Vector2f(context.getRenderWindow().getSize()));
		mBackgroundOverlay.setFillColor(new Color(0, 0, 0, 150));

		mGUIContainer = new Container();
		mGUIContainer.pack(mGameOverTextLabel);
		mGUIContainer.pack(mGameOverIconLabel);
		mGUIContainer.pack(mExitButton);
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow renderWindow = getContext().getRenderWindow();
		renderWindow.draw(mBackgroundOverlay);
		renderWindow.draw(mGUIContainer);
	}

	@Override
	public boolean handleEvent(Event event) {
		super.handleEvent(event);
		mGUIContainer.handleEvent(event);
		return false;
	}

	@Override
	public boolean update(Time dt) {
		return false;
	}

	@Override
	public void layout() {
		super.layout();
		//We have to layout the components depending on the window size
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		mBackgroundOverlay.setSize(new Vector2f(windowSize));
		Helper.layoutVertically(windowSize, 35, true, 0, 0, mGameOverTextLabel, mGameOverIconLabel, mExitButton);
	}

}
