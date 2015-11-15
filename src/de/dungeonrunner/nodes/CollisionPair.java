package de.dungeonrunner.nodes;

public class CollisionPair {

	public SceneNode mNode1;
	public SceneNode mNode2;

	public CollisionPair(SceneNode node1, SceneNode node2) {
		mNode1 = node1;
		mNode2 = node2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mNode1 == null) ? 0 : mNode1.hashCode());
		result = prime * result + ((mNode2 == null) ? 0 : mNode2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollisionPair other = (CollisionPair) obj;
		if (mNode1 == null) {
			if (other.mNode1 != null)
				return false;
		} else if (!mNode1.equals(other.mNode1))
			return false;
		if (mNode2 == null) {
			if (other.mNode2 != null)
				return false;
		} else if (!mNode2.equals(other.mNode2))
			return false;
		return true;
	}
}