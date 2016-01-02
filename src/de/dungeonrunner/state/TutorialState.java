package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Context;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.Button;
import de.dungeonrunner.view.Button.OnButtonClick;
import de.dungeonrunner.view.Component;
import de.dungeonrunner.view.Container;

public class TutorialState extends State implements OnButtonClick {

	private enum Tutorials {
		Shooting, Mining, Moving, Goal
	}

	private Button mShootingTut;
	private Button mMiningTut;
	private Button mMovingTut;
	private Button mGoalTut;
	private Button mBackButton;

	private Container mGUIContainer;
	private Sprite mBackgroundTexture;

	private Sprite mCurrentImage;

	public TutorialState(StateStack stack, Context context) {
		super(stack, context);

		mGUIContainer = new Container();
		mBackgroundTexture = new Sprite(TextureHolder.getInstance().getTexture(TextureID.MAIN_MENU_SCREEN));

		mMovingTut = Button.createButton("Moving");
		mMovingTut.setTag(Tutorials.Moving);
		mMovingTut.setOnClickListener(this);

		mShootingTut = Button.createButton("Shooting");
		mShootingTut.setTag(Tutorials.Shooting);
		mShootingTut.setOnClickListener(this);

		mMiningTut = Button.createButton("Mining");
		mMiningTut.setTag(Tutorials.Mining);
		mMiningTut.setOnClickListener(this);

		mGoalTut = Button.createButton("Goal");
		mGoalTut.setTag(Tutorials.Goal);
		mGoalTut.setOnClickListener(this);

		mBackButton = Button.createButton("BACK");
		mBackButton.setOnClickListener(new OnButtonClick() {

			@Override
			public void onClick(Button bttn) {
				requestStackPop();
			}
		});

		mGUIContainer.pack(mMovingTut);
		mGUIContainer.pack(mShootingTut);
		mGUIContainer.pack(mMiningTut);
		mGUIContainer.pack(mGoalTut);
		mGUIContainer.pack(mBackButton);

		mCurrentImage = new Sprite(TextureHolder.getInstance().getTexture(TextureID.TUT_MOVING));
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow window = getContext().getRenderWindow();
		window.draw(mBackgroundTexture);
		window.draw(mCurrentImage);
		window.draw(mGUIContainer);
	}

	@Override
	public boolean handleEvent(Event event) {
		mGUIContainer.handleEvent(event);
		return super.handleEvent(event);
	}

	@Override
	public void onClick(Button bttn) {
		Tutorials tutoral = (Tutorials) bttn.getTag();
		switch (tutoral) {
		case Moving:
			mCurrentImage.setTexture(TextureHolder.getInstance().getTexture(TextureID.TUT_MOVING));
			break;
		case Shooting:
			mCurrentImage.setTexture(TextureHolder.getInstance().getTexture(TextureID.TUT_SHOOTING));
			break;
		case Mining:
			mCurrentImage.setTexture(TextureHolder.getInstance().getTexture(TextureID.TUT_MINING));
			break;
		case Goal:
			mCurrentImage.setTexture(TextureHolder.getInstance().getTexture(TextureID.TUT_GOAL));
			break;
		default:
			break;
		}
	}

	@Override
	public void layout() {
		super.layout();
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		Helper.layoutVertically(windowSize, Constants.MENU_ITEM_SPACING, true, windowSize.x / 3,
				Constants.MENU_ITEM_HEIGHT / 2,
				mGUIContainer.getComponents().toArray(new Component[mGUIContainer.getComponents().size()]));
		mBackgroundTexture.setScale(windowSize.x / mBackgroundTexture.getLocalBounds().width,
				windowSize.y / mBackgroundTexture.getLocalBounds().height);

		// Tutorial image
		float yPos = windowSize.y / 2 - mCurrentImage.getGlobalBounds().height / 2;
		mCurrentImage.setPosition(windowSize.x / 12, yPos);
	}
}
