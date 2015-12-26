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
import de.dungeonrunner.view.Button.OnButtonClick;
import de.dungeonrunner.view.Container;
import de.dungeonrunner.view.Label;

public class PauseMenuState extends State {

	private Label mPauseLabel;
	private Button mContinueButton;
	private Button mExitButton;

	private Container mGUIContainer;
	private RectangleShape mBackgroundOverlay;

	public PauseMenuState(StateStack stack, Context context) {
		super(stack, context);
		mPauseLabel = new Label("PAUSE");
		mPauseLabel.setCentered(true);
		mPauseLabel.setHeight(Constants.MENU_ITEM_HEIGHT);

		mContinueButton = new Button("CONTINUE", TextureID.BUTTON_DEFAULT);
		mContinueButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mContinueButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mContinueButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mContinueButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mContinueButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mContinueButton.setCentered(true);
		mContinueButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPop();
			}
		});

		mExitButton = new Button("EXIT", TextureID.BUTTON_DEFAULT);
		mExitButton.setSelectedTexture(TextureID.BUTTON_SELECTED);
		mExitButton.setActiveTexture(TextureID.BUTTON_ACTIVATED);
		mExitButton.setWidth(Constants.MENU_ITEM_WIDTH);
		mExitButton.setHeight(Constants.MENU_ITEM_HEIGHT);
		mExitButton.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
		mExitButton.setCentered(true);
		mExitButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStateClear();
				requestStackPush(States.Menu);
			}
		});

		mBackgroundOverlay = new RectangleShape();
		mBackgroundOverlay.setSize(new Vector2f(context.getRenderWindow().getSize()));
		mBackgroundOverlay.setFillColor(new Color(0, 0, 0, 150));

		mGUIContainer = new Container();
		mGUIContainer.pack(mPauseLabel);
		mGUIContainer.pack(mContinueButton);
		mGUIContainer.pack(mExitButton);
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow mRenderWindow = getContext().getRenderWindow();
		mRenderWindow.draw(mBackgroundOverlay);
		mRenderWindow.draw(mGUIContainer);
	}

	@Override
	public boolean handleEvent(Event event) {
		mGUIContainer.handleEvent(event);
		return super.handleEvent(event);
	}

	@Override
	public boolean update(Time dt) {
		return false;
	}

	@Override
	public void layout() {
		super.layout();
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		mBackgroundOverlay.setSize(new Vector2f(windowSize));
		Helper.layoutVertically(windowSize, 25, true, 0, 0, mPauseLabel, mContinueButton, mExitButton);
	}
}