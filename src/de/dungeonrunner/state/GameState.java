package de.dungeonrunner.state;

import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import de.dungeonrunner.GameWorld;
import de.dungeonrunner.entities.PlayerEntity;
import de.dungeonrunner.util.Context;

public class GameState extends State{
	
	private GameWorld mWorld;
	private PlayerEntity mPlayer;

	public GameState(StateStack stack, Context context) {
		super(stack, context);
		mPlayer = context.mPlayer;
		mWorld = new GameWorld(context);
	}

	@Override
	public void draw() {
		mWorld.draw();
	}

	@Override
	public boolean update(Time dt) {
		mWorld.update(dt);
		//CommandQueue commands = mWolrd.getCommandQueue();
		//mPlayer.handleRealtimeInput(commands);
		return true;
	}

	@Override
	public boolean handleEvent(Event event) {
		//CommandQueue commands = mWorld.getCommandQueue();
		//mPlayer.handleEvent(event, commands);
		
//		if(event.type == event.asKeyEvent().type.KEY_PRESSED && event.asKeyEvent().key == Key.ESCAPE){
//			//requestStackPush(States.Pause);
//		}
		mWorld.handleEvent(event);
		return true;
	}

}
