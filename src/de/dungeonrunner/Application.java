package de.dungeonrunner;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

public class Application {

	private final Time FPS = Time.getSeconds(1.0f / 60.0f);
	private RenderWindow mRenderWindow;
	private Clock mClock;
	private GameWorld mGameWorld;

	public Application() {
		mRenderWindow = new RenderWindow();
		mRenderWindow.create(new VideoMode(800, 480, 32), "DungeonRunner");
		mRenderWindow.setFramerateLimit(30);

		mGameWorld = new GameWorld(mRenderWindow);

		mClock = new Clock();

	}

	public void run() {
		Time timeSinceLastUpdate = Time.ZERO;
		while (mRenderWindow.isOpen()) {
			timeSinceLastUpdate = Time.add(timeSinceLastUpdate, mClock.restart());
			while (timeSinceLastUpdate.asMicroseconds() > FPS.asMilliseconds()) {
				timeSinceLastUpdate = Time.sub(timeSinceLastUpdate, FPS);
				processEvents();
				update(FPS);
			}
			render();
		}
	}

	private void processEvents() {
		for (Event event : mRenderWindow.pollEvents())
		{
			if (event.type == Event.Type.CLOSED) {
				mRenderWindow.close();
			}
			if(event.type == Event.Type.RESIZED){
				View view = new View(new Vector2f(mRenderWindow.getSize().x / 2, mRenderWindow.getSize().y), new Vector2f(mRenderWindow.getSize()));
				mRenderWindow.setView(view);
			}
		}
	}

	private void update(Time fPS2) {
		mGameWorld.update(fPS2);
	}

	private void render() {
		mRenderWindow.clear();
		mGameWorld.draw(mRenderWindow);
		mRenderWindow.display();

	}

	public static void main(String[] args) {

		Application application = new Application();
		application.run();
	}
}
