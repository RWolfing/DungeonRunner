package de.dungeonrunner.state;

import java.util.Stack;
import java.util.Vector;

import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.StateHolder;

import static de.dungeonrunner.util.ReverseList.reversed;

/**
 * This class represents a simple stack that holds all States 
 * of the application.
 * 
 * @author Robert Wolfinger
 *
 */
public class StateStack {

	private Stack<State> mStack;
	private Vector<PendingChange> mPendingList;

	public enum Action {
		Push, Pop, Clear
	}

	/**
	 * Default Constructor, creates the stack and the vector.
	 */
	public StateStack() {
		mPendingList = new Vector<>();
		mStack = new Stack<>();
	}

	/**
	 * Updates all containing states of the stack.
	 * 
	 * @param dt delta time
	 */
	public void update(Time dt) {
		for (State state : reversed(mStack)) {
			if (state != null) {
				if (!state.update(dt)){
					break;
				}
			}
		}
	}

	/**
	 * Draws all containing states.
	 */
	public void draw() {
		for (State state : mStack) {
			state.draw();
		}
	}

	/**
	 * Handles jsfml events and passes them to the containing 
	 * states.
	 * 
	 * @param event jsfml events
	 */
	public void handleEvent(Event event) {
		for (State state : reversed(mStack)) {
			if (!state.handleEvent(event))
				break;
		}
		//After we handled all events, we have to apply the changes
		//to the stack
		applyPendingChanges();
	}

	/**
	 * Pushes a new State to the top of the stack.
	 * 
	 * @param stateID id of the State to push
	 */
	public void pushState(States stateID) {
		mPendingList.add(new PendingChange(Action.Push, stateID));
	}

	/**
	 * Pops the top State of the stack.
	 */
	public void popState() {
		mPendingList.add(new PendingChange(Action.Pop, States.None));
	}

	/**
	 * Removes all States from the stack.
	 */
	public void clearState() {
		mPendingList.add(new PendingChange(Action.Clear, States.None));
	}

	/**
	 * This method checks if the stack is empty.
	 * 
	 * @return if the stack is empty
	 */
	public boolean isEmpty() {
		return mStack.isEmpty() && mPendingList.isEmpty();
	}

	/**
	 * Retrieves the State with the given stateID from the StateHolder.
	 * 
	 * @param stateID the id of the State
	 * @return the State with the given id
	 */
	private State createState(States stateID) {
		return StateHolder.getInstance().getState(stateID);
	}

	/**
	 * This method applies all made changes to the stack.
	 */
	private void applyPendingChanges() {
		for (PendingChange change : mPendingList) {
			switch (change.mAction) {
			case Push:
				State state = createState(change.mStateID);
				state.onStatePushed();
				mStack.push(state);
				break;
			case Pop:
				mStack.pop();
				if (!mStack.isEmpty())
					mStack.peek().resumeState();
				break;
			case Clear:
				mStack.clear();
				break;
			default:
				break;
			}
		}
		mPendingList.clear();
	}

	/**
	 * Model of a pending change. This model contains the action
	 * that should be executed and the id of the State.
	 * 
	 * @author Robert Wolfinger
	 *
	 */
	public static class PendingChange {
		Action mAction;
		States mStateID = States.None;

		public PendingChange(Action action, States stateID) {
			mAction = action;
			if (mStateID != null)
				mStateID = stateID;
		}
	}

}
