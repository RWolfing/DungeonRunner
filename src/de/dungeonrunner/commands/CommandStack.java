package de.dungeonrunner.commands;

import java.util.Stack;

/**
 * Implementation of a custom stack to handle commands.
 * TODO currently unnecessary, but this class may be expanded.
 * 
 * @author Robert Wolfinger
 *
 */
public class CommandStack {

	private Stack<SceneCommand> mStack;
	
	public CommandStack(){
		mStack = new Stack<>();
	}
	
	public void push(SceneCommand command){
		mStack.push(command);
	}
	
	public SceneCommand pop(){
		return mStack.pop();
	}
	
	public boolean isEmpty(){
		return mStack.isEmpty();
	}
}
