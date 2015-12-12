package de.dungeonrunner.commands;

import java.util.Stack;

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
