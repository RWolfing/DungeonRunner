package de.dungeonrunner.singleton;

import java.util.HashMap;
import java.util.Map;

import de.dungeonrunner.state.State;
import de.dungeonrunner.state.States;


public class StateHolder{
	
	private static StateHolder mInstance;
	private Map<States, State> mStateMap;
	
	public static StateHolder getInstance(){
		if(mInstance == null){
			mInstance = new StateHolder();
		}
		return mInstance;
	}
	
	private StateHolder(){
		mStateMap = new HashMap<>();
	}
	
	public void registerState(States stateID, State state){
		mStateMap.put(stateID, state);
	}
	
	public State getState(States stateID){
		return mStateMap.get(stateID);
	}
}
