package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import de.dungeonrunner.Application;
import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.UIAmmoBar;

/**
 * A dynamite item that can be picked up by the player.
 * For implementation details see {@link Diamond} as the only
 * change between those classes is the UI Component.
 * 
 * @author Robert Wolfinger
 *
 */
public class DynamiteItem extends Item{

	private long mPassedTime;
	private boolean mIsCollidable;
	private final Vector2f mDiamondOvershoot = new Vector2f(0,5);
	
	public DynamiteItem(TextureID texID, Properties props) {
		super(texID, props);
		mIsCollidable = true;
	}

	@Override
	protected void updateCurrent(Time dt) {
		if (isPickedUp()) {
			/*
			 * If the diamond was picked up by the player we animate it to the game ui component
			 */
			mIsCollidable = false;
			float approachRate = 2000f;
			mPassedTime += dt.asMilliseconds();

			UIAmmoBar ui_comp = GameState.getGameUI().getAmmoComponent();
			//Compute the target position (the diamonds ui)
			Vector2f targetPosition = Vector2f.add(ui_comp.getPosition(),
					new Vector2f(ui_comp.getWidth() / 2, ui_comp.getHeight() / 2));
			targetPosition = Application.getRenderWindow().mapPixelToCoords(new Vector2i(targetPosition), GameState.getWorld().getWorldCamera());
			
			if (!getBoundingRect().contains(targetPosition)) {
				//If we have not reached the position yet, we adjust the veloctiy of the diamond to move to it
				targetPosition = Vector2f.sub(targetPosition, mDiamondOvershoot);
				Vector2f targetDirection = Helper.unitVector(Vector2f.sub(targetPosition, getPosition()));

				Vector2f newVelocity = Helper.unitVector(
						Vector2f.add(Vector2f.mul(targetDirection, approachRate * dt.asSeconds()), Vector2f.ZERO));
				newVelocity = Vector2f.componentwiseMul(newVelocity, new Vector2f(200, 100));
				newVelocity = Vector2f.mul(newVelocity, mPassedTime / 300);
				setVelocity(newVelocity);
			} else {
				//Otherwise we set the veloctiy to zero and destroy the diamond
				setVelocity(Vector2f.ZERO);
				setPosition(targetPosition);
				destroy();
				ui_comp.incrementAmmo();
			}
		} else {
			setVelocity(getVelocity().x, getVelocity().y + Constants.GRAVITY_DOWN * dt.asSeconds());
		}
		super.updateCurrent(dt);
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		if (mIsCollidable) {
			return super.processCollision(node);
		}
		return CollisionType.NONE;
	}

	@Override
	public boolean pickUpCondition() {
		return !GameState.getGameUI().getAmmoComponent().isMaxReached();
	}
}
