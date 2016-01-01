package de.dungeonrunner;

import org.jsfml.system.Clock;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import de.dungeonrunner.commands.AttackCommand;
import de.dungeonrunner.commands.CommandStack;
import de.dungeonrunner.commands.JumpCommand;
import de.dungeonrunner.commands.MoveCommand;
import de.dungeonrunner.commands.ShootCommand;

/**
 * This class controls the input of key and click events provided by the player.
 * 
 * @author Robert Wolfinger
 *
 */
public class PlayerController {

	private Clock mClock;

	public PlayerController() {
		mClock = new Clock();
	}

	/**
	 * This method handles events that are not realtime, like a click.
	 * 
	 * @param event the event
	 * @param commands the command stack
	 */
	public void handleEvent(Event event, CommandStack commands) {
		// Key Pressed Event
		if (event.type == Type.KEY_PRESSED) {
			switch (event.asKeyEvent().key) {
			case UP:
			case W:
				//Make the player unit jump
				JumpCommand jumpCommand = new JumpCommand(NodeType.PLAYER);
				commands.push(jumpCommand);
				break;
			case SPACE:
				if (checkReloadTime()) {
					//If we reloaded make the player unit shoot
					ShootCommand command = new ShootCommand(NodeType.PLAYER);
					commands.push(command);
				}
				break;
			default:
				break;
			}
		}

		// Mouse Events
		if (event.type == Type.MOUSE_BUTTON_PRESSED) {
			AttackCommand command = new AttackCommand(NodeType.PLAYER);
			commands.push(command);
		}
	}

	/**
	 * This method handles realtime input, like the state of a key or click.
	 * 
	 * @param commands command stack
	 */
	public void handleRealtimeInput(CommandStack commands) {
		boolean isLeftPressed = Keyboard.isKeyPressed(Key.LEFT) || Keyboard.isKeyPressed(Key.A);
		boolean isRightKeyPressed = Keyboard.isKeyPressed(Key.RIGHT) || Keyboard.isKeyPressed(Key.D);;

		//Move the player to the left
		if (isLeftPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, -100, 0);
			commands.push(moveCommand);
		}

		//Move the player to the right
		if (isRightKeyPressed) {
			MoveCommand moveCommand = new MoveCommand(NodeType.PLAYER, 100, 0);
			commands.push(moveCommand);
		}
	}

	/**
	 * Checks if the player can shoot again.
	 * 
	 * @return player reloaded
	 */
	private boolean checkReloadTime() {
		if (mClock.getElapsedTime().asMilliseconds() > 500) {
			mClock.restart();
			return true;
		}
		return false;
	}
}
