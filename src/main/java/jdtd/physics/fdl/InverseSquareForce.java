package jdtd.physics.fdl;

import jdtd.physics.DynamicBody;
import jdtd.physics.Vector;
import jdtd.physics.forces.PairwiseForce;

public class InverseSquareForce implements PairwiseForce {
	
	private Graph graph;
	private double c3;
	
	public InverseSquareForce(Graph g) { this(g, 1.0); }
	
	public InverseSquareForce(Graph g, double c3) {
		graph = g;
		this.c3 = c3;
	}

	public Vector forceApplied(DynamicBody target, DynamicBody source) {
		if(!graph.containsEdge(target.name(), source.name())) { 
			Vector r = source.location().translationFrom(target.location());
			double F = c3 / Math.sqrt(r.length());
			return r.normalizeToLength(F);
		} else { 
			return new Vector();
		}
	} 
}