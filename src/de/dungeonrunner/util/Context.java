package de.dungeonrunner.util;

import org.jsfml.graphics.RenderWindow;

import de.dungeonrunner.Application;
import de.dungeonrunner.PlayerController;

/**
 * A container to pass the context of the GameWorld around.
 * 
 * @author Robert Wolfinger
 *
 */
public class Context{
	public PlayerController mPlayer;
	
	/**
	 * Default constructor, creates a Context from the given RenderWindow
	 * and PlayerController. The RenderWindow is currently being accessed through
	 * the Application class, so the parameter will be unused.
	 * 
	 * @param window the RenderWindow
	 * @param player the PlayerController
	 */
	public Context(RenderWindow window, PlayerController player){
		mPlayer = player;
	}
	
	public RenderWindow getRenderWindow(){
		return Application.getRenderWindow();
	}
}
