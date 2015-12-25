package de.dungeonrunner.util;

import org.jsfml.graphics.RenderWindow;

import de.dungeonrunner.Application;
import de.dungeonrunner.PlayerController;

//TODO refactor
public class Context{
	private RenderWindow mRenderWindow;
	public PlayerController mPlayer;
	
	public Context(RenderWindow window, PlayerController player){
		mRenderWindow = window;
		mPlayer = player;
	}
	
	public RenderWindow getRenderWindow(){
		return Application.getRenderWindow();
	}
}
