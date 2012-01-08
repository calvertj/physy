package jdtd.physics.forces;

import jdtd.physics.DynamicBody;

public interface ForceFieldGenerator {
	public ForceField generateForceField(Iterable<DynamicBody> bodies);
}

