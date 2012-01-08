package jdtd.physics.forces;

import jdtd.physics.DynamicBody;

public class BarnesHutFieldGenerator implements ForceFieldGenerator {
	
	private PairwiseForce force;
	
	public BarnesHutFieldGenerator(PairwiseForce pf) { 
		force = pf;
	}
	
	public ForceField generateForceField(Iterable<DynamicBody> bodies) {
		return new BarnesHut(bodies, force);
	} 
}