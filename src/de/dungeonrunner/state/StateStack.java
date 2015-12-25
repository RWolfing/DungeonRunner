package de.dungeonrunner.state;

import java.util.Stack;
import java.util.Vector;

import org.jsfml.system.Time;
import org.jsfml.window.event.Event;

import de.dungeonrunner.singleton.StateHolder;

import static de.dungeonrunner.util.ReverseList.reversed;

public class StateStack {

	private Stack<State> mStack;
	private Vector<PendingChange> mPendingList;

	public enum Action {
		Push, Pop, Clear
	}

	public StateStack() {
		mPendingList = new Vector<>();
		mStack = new Stack<>();
	}

	public void update(Time dt) {
		for (State state : reversed(mStack)) {
			if (state != null) {
				if (!state.update(dt))
					break;
			}
		}
	}

	public void draw() {
		for (State state : mStack) {
			state.draw();
		}
	}

	public void handleEvent(Event event) {
		for (State state : reversed(mStack)) {
			if (!state.handleEvent(event))
				break;
		}
		applyPendingChanges();
	}

	public void pushState(States stateID) {
		mPendingList.add(new PendingChange(Action.Push, stateID));
	}

	public void popState() {
		mPendingList.add(new PendingChange(Action.Pop, States.None));
	}

	public void clearState() {
		mPendingList.add(new PendingChange(Action.Clear, States.None));
	}

	public boolean isEmpty() {
		return mStack.isEmpty() && mPendingList.isEmpty();
	}

	private State createState(States stateID) {
		return StateHolder.getInstance().getState(stateID);
	}

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
