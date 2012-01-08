package jctd.physics.forces;

import jctd.physics.DynamicBody;
import jctd.physics.Vector;

public class Gravity implements PairwiseForce {
	
	private double G;
	
	public Gravity() { this(1.0); }
	public Gravity(double gg) { 
		G = gg;
	}
	
	public Vector forceApplied(DynamicBody target, DynamicBody source) {
		Vector r = source.location().translationFrom(target.location());
		double scale = G * (target.mass() * source.mass()) / r.l2();
		return r.scale(scale);
	} 
}