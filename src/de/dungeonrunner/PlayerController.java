package de.dungeonrunner;

import org.jsfml.system.Clock;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.commands.JumpCommand;
import de.dungeonrunner.commands.MoveCommand;
import de.dungeonrunner.commands.ShootCommand;

public class PlayerController {

	private Clock mClock;

	public PlayerController() {
		mClock = new Clock();
	}

	public void handleEvent(Event event, CommandStack commands) {

		// Key Pressed Event
		if (event.type == Type.KEY_PRESSED) {
			switch (event.asKeyEvent().key) {
			case UP:
				JumpCommand jumpCommand = new JumpCommand(NodeType.PLAYER);
				commands.push(jumpCommand);
				break;
			default:
				break;
			}
		}

		// Mouse Events
		if (event.type == Type.MOUSE_BUTTON_PRESSED) {
			if (checkReloadTime()) {
				ShootCommand command = new ShootCommand(NodeType.PLAYER);
				commands.push(command);
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

		if (isRightKeyPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, 2, 0);
			commands.push(moveCommand);
		}
	}

	private boolean checkReloadTime() {
		if (mClock.getElapsedTime().asMilliseconds() > 500) {
			mClock.restart();
			return true;
		}
		return false;
	}
}
