package de.dungeonrunner.entities;

import java.util.Properties;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import de.dungeonrunner.nodes.SceneNode;
import de.dungeonrunner.singleton.TextureHolder.TextureID;
import de.dungeonrunner.state.GameState;
import de.dungeonrunner.util.Constants;
import de.dungeonrunner.util.Helper;
import de.dungeonrunner.view.UIDiamonds;

public class Diamond extends Item {

	private boolean mIsPickedUp;
	private long mPassedTime;
	private boolean mIsCollidable;

	public Diamond(TextureID texID, Properties props) {
		super(texID, props);
		mIsPickedUp = false;
		mIsCollidable = true;
	}

	@Override
	protected CollisionType processCollision(SceneNode node) {
		if (mIsCollidable) {
			FloatRect intersection = node.getBoundingRect().intersection(getBoundingRect());
			if (intersection != null) {
				if (node instanceof PlayerUnit) {
					mIsPickedUp = true;
				}
			}
			CollisionType type = super.processCollision(node);
			if (type == CollisionType.BOTTOM) {
				setVelocity(0, 0);
			}
			if (type == CollisionType.TOP) {
				setVelocity(getVelocity().x, 0);
			}
			return type;
		}
		return CollisionType.NONE;
	}

	@Override
	protected void updateCurrent(Time dt) {
		if (isPickedUp()) {
			mIsCollidable = false;
			float approachRate = 2000f;
			mPassedTime += dt.asMilliseconds();

			UIDiamonds ui_comp = GameState.getGameUI().getDiamondsComponent();
			Vector2f targetPosition = Vector2f.add(ui_comp.getPosition(),
					new Vector2f(ui_comp.getWidth() / 2, ui_comp.getHeight() - getBoundingRect().height / 2));
			if (!getBoundingRect().contains(targetPosition)) {
				Vector2f targetDirection = Helper.unitVector(Vector2f.sub(targetPosition, getWorldPosition()));

				Vector2f newVelocity = Helper.unitVector(
						Vector2f.add(Vector2f.mul(targetDirection, approachRate * dt.asSeconds()), Vector2f.ZERO));
				newVelocity = Vector2f.componentwiseMul(newVelocity, new Vector2f(200, 100));
				newVelocity = Vector2f.mul(newVelocity, mPassedTime / 300);
				setVelocity(newVelocity);
			} else {
				setVelocity(Vector2f.ZERO);
				setPosition(targetPosition);
				destroy();
				ui_comp.incrementDiamonds();
			}
		} else {
			setVelocity(getVelocity().x, getVelocity().y + Constants.GRAVITY_DOWN * dt.asSeconds());
		}
		super.updateCurrent(dt);
	}

	private boolean isPickedUp() {
		return mIsPickedUp;
	}

}
