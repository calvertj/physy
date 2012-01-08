package jctd.physics;

import java.util.*;

import jctd.physics.forces.ForceField;
import jctd.physics.forces.ForceFieldGenerator;

public class Simulation implements NamedBodies<DynamicBody> {

	private Map<String,DynamicBody> namedBodies;
	private ArrayList<DynamicBody> bodies;
	private HashMap<DynamicBody,DynamicHistory> histories;
	private ForceFieldGenerator fieldGenerator;
	
	private Integrator integrator;
	
	public Simulation(ForceFieldGenerator ffg, Iterable<DynamicBody> bs) {
		fieldGenerator = ffg;
		histories = new HashMap<DynamicBody,DynamicHistory>();
		bodies = new ArrayList<DynamicBody>();
		namedBodies = new TreeMap<String,DynamicBody>();
		
		//integrator = new SimpleIntegrator();
		integrator = new VerletIntegrator();
		
		for(DynamicBody b : bs) {
			bodies.add(b);
			namedBodies.put(b.name(), b);
			histories.put(b, new DynamicHistory());
		}
	}
	
	public DynamicBody getBody(String name) { return namedBodies.get(name); }
	
	public Iterable<DynamicBody> bodies() { return bodies; }
	
	public synchronized void step() { 
		ForceField field = fieldGenerator.generateForceField(bodies);
		
		for(DynamicBody body : bodies) { 
			Vector F = field.calculateForce(body);
			integrator.update(body, F);
			histories.get(body).updateLocation(body.location());
		}
	}
	
	public DynamicHistory getDynamicHistory(DynamicBody b) { 
		return histories.get(b);
	}
	
	public Area boundingBox() { 
		return boundingBox(bodies);
	}
	
	public static Area boundingBox(Iterable<? extends LocatedBody> bodies) { 
		Double x1 = null, y1 = null, x2 = null, y2 = null;
		for(LocatedBody b : bodies) {
			Location loc = b.location();
			if(x1 == null) { 
				x1 = loc.x(); 
				x2 = loc.x(); 
				y1 = loc.y(); 
				y2 = loc.y();
			} else { 
				x1 = Math.min(x1, loc.x());
				y1 = Math.min(y1, loc.y());
				x2 = Math.max(x2, loc.x());
				y2 = Math.max(y2, loc.y());
			}
		}
		double w = x2-x1, h = y2-y1;
		return new Area(x1, y1, x2, y2).expand(w/100.0, h/100.0);
	}
}


