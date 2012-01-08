package jctd.physics.forces;

import jctd.physics.DynamicBody;
import jctd.physics.Vector;

public interface PairwiseForce {
	public Vector forceApplied(DynamicBody target, DynamicBody source);
}
