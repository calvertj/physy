package jdtd.physics.forces;

import jdtd.physics.DynamicBody;

public class StaticFieldGenerator implements ForceFieldGenerator {
	private ForceField ff; 
	public StaticFieldGenerator(ForceField f) { 
		ff = f;
	}
	public ForceField generateForceField(Iterable<DynamicBody> bodies) {
		return ff;
	} 
}

