package de.dungeonrunner;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import tiled.core.Map;

public class Application {

	public static void main(String[] args) {
		Map map = null;
		SFMLTileRenderer renderer = null;

		for (String arg : args) {
			map = TmxMapLoader.loadMap(arg);
		}

		if (map != null) {
			TextureHolder.getInstance().loadTiledTextures(map);
			renderer = new SFMLTileRenderer(map);
		}

		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(800, 480, 32), "Test");
		window.setFramerateLimit(30);
		
		View view = new View(new Vector2f(window.getSize().x / 2, window.getSize().y / 2), new Vector2f(window.getSize()));
		window.setView(view);

		while (window.isOpen()) {
			for (Event event : window.pollEvents()) {
				
				switch(event.type){
				case  CLOSED:
					window.close();
					break;
				case RESIZED:
					view= new View(new Vector2f(window.getSize().x / 2, window.getSize().y / 2), new Vector2f(window.getSize()));
					window.setView(view);
					break;
				case KEY_PRESSED:
					view.move(new Vector2f(10, 0));
					window.setView(view);
				default:
					break;
				}
			}

			window.clear();
			if (renderer != null) {
				window.draw(renderer);
			}
			window.display();
		}

	}
}
