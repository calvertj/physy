package jctd.physics.forces;

import jctd.physics.DynamicBody;

public interface ForceFieldGenerator {
	public ForceField generateForceField(Iterable<DynamicBody> bodies);
}

