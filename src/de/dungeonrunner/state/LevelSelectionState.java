package de.dungeonrunner.state;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
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
import de.dungeonrunner.view.Label;

/**
 * State to present the level selection screen for the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class LevelSelectionState extends State implements OnButtonClick {

	//the path to the map chosen by the user
	private static String mChosenMapFilePath;
	
	private View mSelectionCamera;
	
	private Label mTitle;
	private Label mText;
	private List<Component> mComponents;
	private Container mSelectionContainer;
	
	private RectangleShape mBackgroundOverlay;
	private Sprite mBackgroundImage;
	
	private Button mBackButton;

	/**
	 * Default constructor, creates the State with the given 
	 * parameters
	 * 
	 * @param stack the state stack
	 * @param context the context
	 */
	public LevelSelectionState(StateStack stack, Context context) {
		super(stack, context);
		mComponents = new ArrayList<>();
		mSelectionContainer = new Container();
		mSelectionCamera = new View(Vector2f.ZERO, Vector2f.ZERO);

		//Search through the map directory for .tmx files
		File rootDirectory = new File(Constants.MAP_DIR);
		List<File> tmxFiles = Arrays.asList(rootDirectory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".tmx");
			}
		}));
		
		mTitle = new Label("Choose Your Level!");
		mTitle.setCentered(true);
		mTitle.setHeight(Constants.MENU_ITEM_HEIGHT);
		mTitle.setCharSize(35);
		mComponents.add(mTitle);
		
		mText = new Label("Choose the level you want to play from the available maps...");
		mText.setCentered(true);
		mText.setHeight(Constants.MENU_ITEM_HEIGHT);
		mText.setCharSize(25);
		mComponents.add(mText);
		
		//For each file we create a button to select that map
		for (File file : tmxFiles) {
			Button button = new Button(file.getName().replace(".tmx", ""), TextureID.BUTTON_DEFAULT);
			button.setSelectedTexture(TextureID.BUTTON_SELECTED);
			button.setActiveTexture(TextureID.BUTTON_ACTIVATED);
			button.setWidth(Constants.MENU_ITEM_WIDTH);
			button.setHeight(Constants.MENU_ITEM_HEIGHT);
			button.setCentered(true);
			button.offsetText(Constants.MENU_ITEM_TEXT_OFFSET);
			button.setOnClickListener(this);
			button.setTag(file.getPath());

			mComponents.add(button);
			mSelectionContainer.pack(button);
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
		
		mComponents.add(mBackButton);
		mSelectionContainer.pack(mBackButton);
		
		mBackgroundImage = new Sprite(TextureHolder.getInstance().getTexture(TextureID.LEVEL_SELECTION_BACKGROUND));
		mBackgroundOverlay = new RectangleShape();
		mBackgroundOverlay.setSize(new Vector2f(context.getRenderWindow().getSize()));
		mBackgroundOverlay.setFillColor(new Color(0, 0, 0, 150));
	}

	@Override
	public void draw() {
		super.draw();
		RenderWindow window = getContext().getRenderWindow();
		window.draw(mBackgroundImage);
		window.draw(mBackgroundOverlay);
		window.setView(mSelectionCamera);
		window.draw(mTitle);
		window.draw(mText);
		window.draw(mSelectionContainer);
	}

	@Override
	public boolean handleEvent(Event event) {
		if(event.asKeyEvent() != null && event.asKeyEvent().key == Key.ESCAPE){
			requestStackPop();
			return true;
		}
		
		mSelectionContainer.handleEvent(event);
		mSelectionCamera.setCenter(mSelectionContainer.getSelectedChild().getPosition());
		return super.handleEvent(event);
	}

	@Override
	public void layout() {
		super.layout();
		Vector2i windowSize = getContext().getRenderWindow().getSize();
		mSelectionCamera = new View(new Vector2f(windowSize.x / 2f, windowSize.y / 2f), new Vector2f(windowSize.x, windowSize.y));
		mBackgroundOverlay.setSize(new Vector2f(windowSize));
		mBackgroundImage.setScale(windowSize.x / mBackgroundImage.getLocalBounds().width,
				windowSize.y / mBackgroundImage.getLocalBounds().height);
		Helper.layoutVertically(windowSize, Constants.MENU_ITEM_SPACING, true, 0,
				0, mComponents.toArray(new Component[mComponents.size()]));
	}

	@Override
	public void onClick(Button bttn) {
		mChosenMapFilePath = (String) bttn.getTag();
		requestStateClear();
		requestStackPush(States.Game);
	}
	
	/**
	 * Returns the path to the map chosen by the user.
	 * 
	 * @return a path to a .tmx file
	 */
	public static String getChosenMapPath(){
		return mChosenMapFilePath;
	}
}
