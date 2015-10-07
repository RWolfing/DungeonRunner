package de.dungeonrunner;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
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
		
		if(map != null){
			renderer = new SFMLTileRenderer(map);
		}
		
		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(800, 480, 32), "Test");
		window.setFramerateLimit(30);
		
		
		CircleShape shape = new CircleShape(40.0f);
		shape.setPosition(100.0f, 100.0f);
		shape.setFillColor(Color.BLUE);
		
		while(window.isOpen()){
			for(Event event : window.pollEvents()) {
		        if(event.type == Event.Type.CLOSED) {
		            //The user pressed the close button
		            window.close();
		        }
		    }
			
			window.clear();
			if(renderer != null){
			window.draw(renderer);
			}
			window.display();
		}

	}
}
