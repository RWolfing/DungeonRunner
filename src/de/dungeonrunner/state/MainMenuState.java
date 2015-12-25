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

public class MainMenuState extends State {

	private final int MENU_X_OFFSET = 4; //in % off window width ( 100/MENU_X_OFFSET)
	private Sprite mBackgroundTexture;
	private Container mGUIContainer;

	private Button mPlayButton;
	private Button mCreditsButton;
	private Button mExitButton;

	public MainMenuState(StateStack stack, Context context) {
		super(stack, context);

		mGUIContainer = new Container();
		mBackgroundTexture = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));

		mPlayButton = new Button("PLAY", TextureID.BUTTON_DEFAULT);
		mPlayButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mPlayButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mPlayButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mPlayButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mPlayButton.setCentered(true);
		mPlayButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mPlayButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPop();
				requestStackPush(States.Game);
			}
		});

		mCreditsButton = new Button("CREDITS", TextureID.BUTTON_DEFAULT);
		mCreditsButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mCreditsButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mCreditsButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mCreditsButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mCreditsButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mCreditsButton.setCentered(true);

		mExitButton = new Button("EXIT", TextureID.BUTTON_DEFAULT);
		mExitButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mExitButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mExitButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mExitButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mExitButton.setCentered(true);
		mExitButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);

		mExitButton.setOnClickListener(new OnButtonClick() {
			@Override
			public void onClick(Button bttn) {
				requestStackPop();
			}
		});

		mGUIContainer.pack(mPlayButton);
		mGUIContainer.pack(mCreditsButton);
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
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		Helper.layoutVertically(windowSize, Constants.MENU_ITEM_SPACING, true, windowSize.x / MENU_X_OFFSET, Constants.MENU_ITEM_HEIGHT / 2, mPlayButton,
				mCreditsButton, mExitButton);
		mBackgroundTexture.setScale(windowSize.x / mBackgroundTexture.getLocalBounds().width,
				windowSize.y / mBackgroundTexture.getLocalBounds().height);
	}
}
