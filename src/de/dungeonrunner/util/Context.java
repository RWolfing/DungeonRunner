package de.dungeonrunner.util;

import org.jsfml.graphics.RenderWindow;

import de.dungeonrunner.entities.PlayerEntity;

public class Context{
	public RenderWindow mRenderWindow;
	public PlayerEntity mPlayer;
	
	public Context(RenderWindow window, PlayerEntity player){
		mRenderWindow = window;
		mPlayer = player;
	}
}
