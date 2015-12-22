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

public class Container extends BasicTransformable implements Drawable {

	private Vector<Component> mChildren;
	private int mSelectedChild;

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

	public void handleEvent(Event event) {
		if (hasSelection() && mChildren.get(mSelectedChild).isActive()) {
			mChildren.get(mSelectedChild).handleEvent(event);
		} else if (event.type == Type.KEY_RELEASED) {
			if (event.asKeyEvent().key == Keyboard.Key.W) {
				selectPrevious();
			} else if (event.asKeyEvent().key == Key.S) {
				selectNext();
			} else if (event.asKeyEvent().key == Key.RETURN && hasSelection()) {
				mChildren.get(mSelectedChild).activate();
			}
		}
	}

	public void pack(Component component) {
		mChildren.add(component);
		if (!hasSelection() && component.isSelectable()) {
			select(mChildren.size() - 1);
		}
	}

	public boolean isSelectable() {
		return false;
	}
	
	private void select(int index) {
		if (mChildren.get(index).isSelectable()) {
			if (hasSelection()) {
				mChildren.get(mSelectedChild).deselect();
			}
			mChildren.get(index).select();
			mSelectedChild = index;
		}
	}

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

	private boolean hasSelection() {
		if (mSelectedChild > 0 && mSelectedChild < mChildren.size()) {
			return true;
		}
		return false;
	}
}
