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
		//result = ((mNode1) == null) ? 0 : mNode1.hashCode();
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
		
		//Check if it contains the same nodes
		CollisionPair other = (CollisionPair) obj;
		//Check if obj contains mNode1
		if(mNode1.equals(other.mNode1) || mNode1.equals(other.mNode2)){
			//check if obj contains mNode2
			if(mNode2.equals(other.mNode1) || mNode2.equals(other.mNode2))
			{
				return true;
			}
		}
		return false;
	}
}