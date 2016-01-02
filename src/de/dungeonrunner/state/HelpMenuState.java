package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Container;
import de.dungeonrunner.view.Button.OnButtonClick;

/**
 * The state to represent the help menu of the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class HelpMenuState extends State {

	private Button mDebugButton;
	private Button mTutorialButton;
	private Button mBackButton;
	private Container mGUIContainer;
	private Sprite mBackgroundTexture;

	public HelpMenuState(StateStack stack, Context context) {
		super(stack, context);
		mGUIContainer = new Container();
		mBackgroundTexture = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));

		mTutorialButton = new Button("TUTORIAL", TextureID.BUTTON_DEFAULT);
		mTutorialButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mTutorialButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mTutorialButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mTutorialButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mTutorialButton.setCentered(true);
		mTutorialButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mTutorialButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPush(States.Tutorial);
			}
		});

		mDebugButton = new Button("", TextureID.BUTTON_DEFAULT);
		mDebugButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mDebugButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mDebugButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mDebugButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mDebugButton.setCentered(true);
		mDebugButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mDebugButton.setToggle(true);
		mDebugButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				if (bttn.isToggled()) {
					bttn.setText("DEBUG is ON");
				} else {
					bttn.setText("DEBUG is OFF");
				}
				Constants.IS_DEBUGGING = bttn.isToggled();
			}
		});

		if (Constants.IS_DEBUGGING) {
			mDebugButton.activate();
		} else {
			mDebugButton.setText("DEBUG is OFF");
			mDebugButton.deactivate();
		}

		mBackButton = new Button("BACK", TextureID.BUTTON_DEFAULT);
		mBackButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mBackButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mBackButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mBackButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mBackButton.setCentered(true);
		mBackButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mBackButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPop();
			}
		});

		mGUIContainer.pack(mTutorialButton);
		mGUIContainer.pack(mDebugButton);
		mGUIContainer.pack(mBackButton);
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow window = getContext().getRenderWindow();
		window.draw(mBackgroundTexture);
		window.draw(mGUIContainer);
	}

	@Override
	public boolean handleEvent(Event event) {
		if (event.asKeyEvent() != null && event.asKeyEvent().key == Key.ESCAPE) {
			requestStackPop();
			return true;
		}

		mGUIContainer.handleEvent(event);
		return super.handleEvent(event);
	}

	@Override
	public void layout() {
		super.layout();
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		Helper.layoutVertically(windowSize, Constants.MENU_ITEM_SPACING, true, windowSize.x / 4,
				Constants.MENU_ITEM_HEIGHT / 2, mTutorialButton, mDebugButton, mBackButton);
		mBackgroundTexture.setScale(windowSize.x / mBackgroundTexture.getLocalBounds().width,
				windowSize.y / mBackgroundTexture.getLocalBounds().height);
	}

}
