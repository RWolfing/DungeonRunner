package de.dungeonrunner;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

public class PlayerController {

	public void handleEvent(Event event, CommandStack commands) {
		// do nothing yet
	}

	public void handleRealtimeInput(CommandStack commands) {
		boolean isLeftPressed = Keyboard.isKeyPressed(Key.LEFT);
		boolean isRightKeyPressed = Keyboard.isKeyPressed(Key.RIGHT);

		if (isLeftPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, -5, 0);
			commands.push(moveCommand);
		}
		
		if(isRightKeyPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, 5, 0);
			commands.push(moveCommand);
		}
	}
}
