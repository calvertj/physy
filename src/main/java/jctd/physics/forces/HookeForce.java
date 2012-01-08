package jctd.physics.forces;

import jctd.physics.DynamicBody;
import jctd.physics.Vector;

public class HookeForce implements PairwiseForce {
	
	private double k;
	private double equilibriumLength;
	
	public HookeForce(double len, double k) { 
		equilibriumLength = len;
		this.k = k;
	}

	public Vector forceApplied(DynamicBody target, DynamicBody source) {
		Vector r = source.location().translationFrom(target.location());
		double x = r.length() - equilibriumLength;
		return r.normalizeToLength(-k * x);
	}
}
