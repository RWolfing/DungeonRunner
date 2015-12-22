package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Button.OnButtonClick;
import de.dungeonrunner.view.Container;

public class MainMenuState extends State {

	private Sprite mBackgroundTexture;
	private Container mGUIContainer;

	private Button mPlayButton;
	private Button mExitButton;

	public MainMenuState(StateStack stack, Context context) {
		super(stack, context);

		mGUIContainer = new Container();
		mBackgroundTexture = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));

		mPlayButton = new Button("PLAY", TextureID.BUTTON_DEFAULT);
		mPlayButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mPlayButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mPlayButton.setPosition(200, 200);
		mPlayButton.setWidth(200);
		mPlayButton.setHeight(100);
		mPlayButton.setCentered(true);
		mPlayButton.offsetText(0, -10);
		mPlayButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPop();
				requestStackPush(States.Game);
			}
		});

		mExitButton = new Button("EXIT", TextureID.BUTTON_DEFAULT);
		mExitButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mExitButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mExitButton.setPosition(200, 325);
		mExitButton.setWidth(200);
		mExitButton.setHeight(100);
		mExitButton.setCentered(true);
		mExitButton.offsetText(0, -10);

		mExitButton.setOnClickListener(new OnButtonClick() {
			@Override
			public void onClick(Button bttn) {
				requestStackPop();
			}
		});

		mGUIContainer.pack(mPlayButton);
		mGUIContainer.pack(mExitButton);
	}

	@Override
	public void draw() {
		RenderWindow window = getContext().mRenderWindow;
		window.setView(window.getDefaultView());
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
		return true;
	}

}
