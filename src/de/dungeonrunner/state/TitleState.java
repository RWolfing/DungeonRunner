package de.dungeonrunner.state;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import de.dungeonrunner.singleton.FontHolder;
import de.dungeonrunner.singleton.TextureHolder;
import de.dungeonrunner.singleton.FontHolder.FontID;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.util.Context;

public class TitleState extends State {

	private Sprite mBackgroundSprite;
	private Text mTitleText;

	public TitleState(StateStack stack, Context context) {
		super(stack, context);

		mBackgroundSprite = new Sprite(TextureHolder.getInstance().getTexture(TextureID.TITLE_BG_SCREEN));
		mTitleText = new Text("Press any key to start", FontHolder.getInstance().getFont(FontID.DUNGEON_FONT));
		mTitleText.setPosition(context.mRenderWindow.getView().getCenter());
	}

	@Override
	public void draw() {
		RenderWindow renderWindow = getContext().mRenderWindow;
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
			requestStackPop();
			requestStackPush(States.Menu);
		}
		return false;
	}
}
