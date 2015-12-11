package de.dungeonrunner;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

public class PlayerController {

	public void handleEvent(Event event, CommandStack commands) {

		//Key Pressed Event
		if(event.type == Type.KEY_PRESSED){
			switch(event.asKeyEvent().key){
			case UP:
				JumpCommand jumpCommand = new JumpCommand(NodeType.PLAYER);
				commands.push(jumpCommand);
				break;
			default:
				break;
			}
		}
	}

	public void handleRealtimeInput(CommandStack commands) {
		boolean isLeftPressed = Keyboard.isKeyPressed(Key.LEFT);
		boolean isRightKeyPressed = Keyboard.isKeyPressed(Key.RIGHT);

		if (isLeftPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, -2, 0);
			commands.push(moveCommand);
		}
		
		if(isRightKeyPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, 2, 0);
			commands.push(moveCommand);
		}
	}
}
