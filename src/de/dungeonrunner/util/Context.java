package de.dungeonrunner.util;

import org.jsfml.graphics.RenderWindow;

import de.dungeonrunner.PlayerController;

public class Context{
	public RenderWindow mRenderWindow;
	public PlayerController mPlayer;
	
	public Context(RenderWindow window, PlayerController player){
		mRenderWindow = window;
		mPlayer = player;
	}
}
