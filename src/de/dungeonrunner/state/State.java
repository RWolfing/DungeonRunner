package de.dungeonrunner.state;

import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import de.dungeonrunner.util.Context;

public abstract class State {

	private StateStack mStack;
	private Context mContext;
	
	public State(StateStack stack, Context context){
		mStack = stack;
		mContext = context;
	}
	
	public void draw(){
		
	}
	
	public boolean update(Time dt){
		return true;
	}
	
	public boolean handleEvent(Event event){
		return true;
	}
	
	protected void requestStackPush(States id){
		mStack.pushState(id);
	}
	
	protected void requestStackPop(){
		mStack.popState();
	}
	
	protected void requestStateClear(){
		mStack.clearState();
	}
	
	protected Context getContext(){
		return mContext;
	}
}
