package de.dungeonrunner.view;

import java.util.Vector;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

/**
 * A container to hold different ui components.
 * 
 * @author Robert Wolfinger
 *
 */
public class Container extends BasicTransformable implements Drawable {

	private Vector<Component> mChildren;
	private int mSelectedChild;

	/**
	 * Default constructor.
	 */
	public Container() {
		mChildren = new Vector<>();
		mSelectedChild = -1;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states = new RenderStates(states, Transform.combine(states.transform, getTransform()));
		for(Component child : mChildren){
			target.draw(child, states);
		}
	}

	/**
	 * Handles click and key events.
	 * 
	 * @param event a click or key event
	 */
	public void handleEvent(Event event) {
		if (hasSelection() && mChildren.get(mSelectedChild).isActive()) {
			//If the selected child is active, forward the event to the child
			mChildren.get(mSelectedChild).handleEvent(event);
		} else if (event.type == Type.KEY_PRESSED) {
			// Handle up and down to navigate through the containing components
			if (event.asKeyEvent().key == Keyboard.Key.W || event.asKeyEvent().key == Key.UP) {
				selectPrevious();
			} else if (event.asKeyEvent().key == Key.S || event.asKeyEvent().key == Key.DOWN) {
				selectNext();
			} else if (event.asKeyEvent().key == Key.RETURN && hasSelection()) {
				//Return key was pressed, activate the selected child
				mChildren.get(mSelectedChild).activate();
			}
		}
	}

	/**
	 * Pack a new component into the container.
	 * 
	 * @param component a new component
	 */
	public void pack(Component component) {
		mChildren.add(component);
		if (!hasSelection() && component.isSelectable()) {
			select(mChildren.size() - 1);
		}
	}

	/**
	 * Is the container selectable.
	 * 
	 * @return false Per default a container is not selectable.
	 */
	public boolean isSelectable() {
		return false;
	}
	
	/**
	 * Select a child with the given index.
	 * 
	 * @param index the index of the child
	 */
	private void select(int index) {
		if (mChildren.get(index).isSelectable()) {
			//The child is selectable
			if (hasSelection()) {
				//Deselect all other children
				mChildren.get(mSelectedChild).deselect();
			}
			//Select the child with the given index
			mChildren.get(index).select();
			mSelectedChild = index;
		}
	}

	/**
	 * Selects the next child in the container.
	 */
	private void selectNext() {
		if (!hasSelection()) {
			return;
		}
		
		int next = mSelectedChild;
		do {
			next = (next + 1) % mChildren.size();
		} while (!mChildren.get(next).isSelectable());
		select(next);
	}

	/**
	 * Selects the previous child in the container.
	 */
	private void selectPrevious() {
		if (!hasSelection()) {
			return;
		}
		
		int prev = mSelectedChild;
		do {
			prev = (prev + mChildren.size() - 1) % mChildren.size();
		} while (!mChildren.get(prev).isSelectable());
		select(prev);
	}

	/**
	 * Returns if the container has children, that can be selected.
	 * 
	 * @return if the container has a selection
	 */
	private boolean hasSelection() {
		if (mSelectedChild >= 0 && mSelectedChild < mChildren.size()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Return all children.
	 * 
	 * @return all containing children
	 */
	public Vector<Component> getComponents(){
		return mChildren;
	}
	
	/**
	 * Returns the selected child.
	 * 
	 * @return the selected child
	 */
	public Component getSelectedChild(){
		return mChildren.get(mSelectedChild);
	}
}
