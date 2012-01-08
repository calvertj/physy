package jctd.physics.forces;

import jctd.physics.DynamicBody;
import jctd.physics.Vector;

public class LogarithmicSpring implements PairwiseForce {
	
	private double c1, c2;
	
	public LogarithmicSpring(double c1, double c2) { 
		this.c1 = c1;
		this.c2 = c2;
	}

	public Vector forceApplied(DynamicBody target, DynamicBody source) {
		Vector r = source.location().translationFrom(target.location());
		double d = r.length();
		double len = c1 * Math.log(d / c2);
		return r.normalizeToLength(len);
	}
}
