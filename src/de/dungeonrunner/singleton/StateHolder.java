package de.dungeonrunner.singleton;

import java.util.HashMap;
import java.util.Map;

import de.dungeonrunner.state.State;
import de.dungeonrunner.state.States;

/**
 * A Singleton to hold and register all states of the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class StateHolder{
	
	private static StateHolder mInstance;
	private Map<States, State> mStateMap;
	
	/**
	 * Returns the instance of the StateHolder.
	 * 
	 * @return a StateHolder instance
	 */
	public static StateHolder getInstance(){
		if(mInstance == null){
			mInstance = new StateHolder();
		}
		return mInstance;
	}
	
	/**
	 * Default constructor.
	 */
	private StateHolder(){
		mStateMap = new HashMap<>();
	}
	
	/**
	 * Registers the given state under the given stateID.
	 * 
	 * @param stateID the id of the state
	 * @param state the state
	 */
	public void registerState(States stateID, State state){
		mStateMap.put(stateID, state);
	}
	
	/**
	 * Retrieves the state with the given stateID.
	 * 
	 * @param stateID the id of the state to retrieve
	 * @return a state
	 */
	public State getState(States stateID){
		return mStateMap.get(stateID);
	}
}
