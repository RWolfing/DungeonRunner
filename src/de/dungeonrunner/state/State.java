package de.dungeonrunner.state;

import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.util.Context;

public abstract class State {

	private StateStack mStack;
	private Context mContext;
	public View mCamera;

	public State(StateStack stack, Context context) {
		mStack = stack;
		mContext = context;
	}

	public void draw() {
		mContext.getRenderWindow().setView(mCamera);
	}

	public boolean update(Time dt) {
		return true;
	}

	public boolean handleEvent(Event event) {
		switch (event.type) {
		case RESIZED:
			if (event.asSizeEvent() != null) {
				onWindowResized(event.asSizeEvent().size);
				return true;
			}
		default:
			break;
		}
		return false;
	}

	protected void requestStackPush(States id) {
		mStack.pushState(id);
	}

	protected void requestStackPop() {
		mStack.popState();
	}

	protected void requestStateClear() {
		mStack.clearState();
	}

	protected void onWindowResized(Vector2i newSize) {
		layout();
	}

	public final void onStatePushed() {
		onStateSetup();
		layout();
	}

	public final void resumeState() {
		onStateResumed();
		layout();
	}

	protected void onStateSetup() {
		// Unused
	}

	public void layout() {
		Vector2i newSize = mContext.getRenderWindow().getSize();
		mCamera = new View(new Vector2f(newSize.x / 2f, newSize.y / 2f), new Vector2f(newSize.x, newSize.y));
	}

	public void onStatePopped() {
		// Unused
	}

	public void onStateResumed() {
		// Unused
	}

	protected Context getContext() {
		return mContext;
	}
	
	protected View getCamera(){
		return mCamera;
	}
}
