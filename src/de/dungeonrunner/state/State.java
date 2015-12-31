package de.dungeonrunner.state;

import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import de.dungeonrunner.util.Context;

/**
 * This class represents a window state. Different states must be implementations
 * of this abstract state. States are managed and changed through the StateStack.
 * 
 * @author Robert Wolfinger
 *
 */
public abstract class State {

	private StateStack mStack;
	private Context mContext;
	public View mCamera;

	/**
	 * Default constructor, creates a new State from the given StateStack
	 * and Context.
	 * 
	 * @param stack the StateStack
	 * @param context the Context for this State
	 */
	public State(StateStack stack, Context context) {
		mStack = stack;
		mContext = context;
	}

	/**
	 * Draws the state.
	 */
	public void draw() {
		//Before we want to draw stuff we set the view  of the RenderWindow to our camera
		mContext.getRenderWindow().setView(mCamera);
	}

	/**
	 * Updates the state with the given delta time.
	 * 
	 * @param dt delta time
	 * @return if the update was consumed and following States in the StateStack should
	 * not be updated
	 */
	public boolean update(Time dt) {
		return true;
	}

	/**
	 * Handles click and key events for the state.
	 * 
	 * @param event a event
	 * @return if the event was consumed and following States in the StateStack should
	 * not handle this event
	 */
	public boolean handleEvent(Event event) {
		switch (event.type) {
		case RESIZED:
			//We listen for resized events if the window size changed
			if (event.asSizeEvent() != null) {
				//We need to layout the component of the state to the new size
				onWindowResized(event.asSizeEvent().size);
				return true;
			}
		default:
			break;
		}
		return false;
	}

	/**
	 * Requests that a new State with the given id should be pushed on top of the
	 * StateStack.
	 * 
	 * @param id the id of the new State
	 */
	protected void requestStackPush(States id) {
		mStack.pushState(id);
	}

	/**
	 * Requests that the current State is popped from the top of the
	 * StateStack
	 */
	protected void requestStackPop() {
		mStack.popState();
	}

	/**
	 * Pops all States and clears the StateStack.
	 */
	protected void requestStateClear() {
		mStack.clearState();
	}

	/**
	 * Method that will be called if the window size changed.
	 * 
	 * @param newSize the new size of the window
	 */
	protected void onWindowResized(Vector2i newSize) {
		//As the window size changed we need another layout pass
		layout();
	}

	/**
	 * This method should be called if the State was pushed on top of the StateStack.
	 * This notifies the current State that it is now visible and layouts the components
	 * of the State.
	 */
	public final void onStatePushed() {
		onStateSetup();
		layout();
	}

	/**
	 * This method should be called if the current State was resumed. 
	 * This notifies the current State that it is now visible again and layouts the 
	 * components of the State.
	 */
	public final void resumeState() {
		onStateResumed();
		layout();
	}

	/**
	 * This method will be called if a State was pushed to the top of the StateStack.
	 * Any subclasses should overwrite this method for an initial setup of the State.
	 */
	protected void onStateSetup() {
		// Unused
	}

	/**
	 * This method will be called after the window sized changed and the components 
	 * of the State should be laid out. 
	 */
	public void layout() {
		//We also adapt the camera to the new window size
		Vector2i newSize = mContext.getRenderWindow().getSize();
		mCamera = new View(new Vector2f(newSize.x / 2f, newSize.y / 2f), new Vector2f(newSize.x, newSize.y));
	}

	/**
	 * This method should be called if the State was popped from the StateStack.
	 */
	public void onStatePopped() {
		// Unused
	}

	/**
	 * This method should be called if a State was popped from the StateStack and the
	 * current State is visible again.
	 */
	public void onStateResumed() {
		// Unused
	}

	/**
	 * Returns the context of the state.
	 * 
	 * @return the context
	 */
	protected Context getContext() {
		return mContext;
	}
	
	/**
	 * Returns the camera of the state.
	 * 
	 * @return the camera
	 */
	protected View getCamera(){
		return mCamera;
	}
}
