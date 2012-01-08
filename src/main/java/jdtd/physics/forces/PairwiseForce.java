package jdtd.physics.forces;

import jdtd.physics.DynamicBody;
import jdtd.physics.Vector;

public interface PairwiseForce {
	public Vector forceApplied(DynamicBody target, DynamicBody source);
}
