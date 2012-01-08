package jdtd.physics.forces;

import jdtd.physics.DynamicBody;

public class AddingFieldGenerator implements ForceFieldGenerator {
	
	private ForceFieldGenerator[] generators;
	
	public AddingFieldGenerator(ForceFieldGenerator... fggs) { 
		generators = fggs.clone();
	}
	public ForceField generateForceField(Iterable<DynamicBody> bodies) {
		ForceField[] ffs = new ForceField[generators.length];
		for(int i = 0; i < ffs.length; i++) { 
			ffs[i] = generators[i].generateForceField(bodies);
		}
		return new FieldSum(ffs);
	} 
}