package jctd;

import jctd.physics.DynamicBody;
import jctd.physics.Vector;
import jctd.physics.forces.PairwiseForce;

public class GraphEdgeForce implements PairwiseForce {
	
	private Graph graph;
	private PairwiseForce force;
	
	public GraphEdgeForce(Graph g, PairwiseForce pf) { 
		graph = g;
		force = pf;
	}

	public Vector forceApplied(DynamicBody target, DynamicBody source) {
		if(graph.containsEdge(target.name(), source.name())) { 
			return force.forceApplied(target, source);
		} else { 
			return new Vector();
		}
	}
}
