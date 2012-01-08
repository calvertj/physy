package jctd.physics.fdl;

import java.util.Map;
import java.util.TreeMap;

import jctd.physics.DynamicBody;
import jctd.physics.NamedBodies;
import jctd.physics.Vector;
import jctd.physics.forces.ForceField;
import jctd.physics.forces.PairwiseForce;

public class GraphEdgeForceField implements ForceField {
	
	private Graph graph;
	private PairwiseForce force;
	private NamedBodies<DynamicBody> lookup;
	
	public GraphEdgeForceField(Graph g, PairwiseForce pf, NamedBodies<DynamicBody> nb, boolean edged) { 
		graph = g;
		force = pf;
		lookup = nb;
	}
	
	public GraphEdgeForceField(Graph g, PairwiseForce pf, Iterable<DynamicBody> nb, boolean edged) { 
		this(g, pf, new DynamicBodyMap(nb), edged);
	}

	public GraphEdgeForceField(Graph g, PairwiseForce pf, Iterable<DynamicBody> nb) { 
		this(g, pf, new DynamicBodyMap(nb), true);
	}

	public Vector calculateForce(DynamicBody target) {
		Vector v = new Vector();

		for(Graph.Edge e : graph.edges(target.name())) { 
			String sourceName = e.e2;
			DynamicBody source = lookup.getBody(sourceName);
			
			//System.out.println(String.format("F: %s <- %s", target.name(), source.name()));
			v = v.addTo(force.forceApplied(target, source));
		}
		return v;
	}
	
	private static class DynamicBodyMap implements NamedBodies<DynamicBody> {
		
		private Map<String,DynamicBody> bodies;
		
		public DynamicBodyMap() { bodies = new TreeMap<String,DynamicBody>(); }
		
		public DynamicBodyMap(Iterable<DynamicBody> bs) {
			this();
			for(DynamicBody b : bs) { 
				bodies.put(b.name(), b);
			}
		}

		public DynamicBody getBody(String name) {
			return bodies.get(name);
		} 
	}
}
