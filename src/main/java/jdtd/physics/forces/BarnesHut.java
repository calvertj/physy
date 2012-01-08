package jdtd.physics.forces;

import java.util.*;

import jdtd.physics.Area;
import jdtd.physics.DynamicBody;
import jdtd.physics.LocatedBody;
import jdtd.physics.Location;
import jdtd.physics.Vector;

public class BarnesHut implements ForceField {
	
	private static double theta = 0.5;
	private Quad quad;
	private PairwiseForce forcer;

	private BarnesHut(Location ul, double w, Iterable<DynamicBody> bodies, PairwiseForce pf) { 
		quad = new Quad(new Area(ul.x(), ul.y(), ul.x() + w, ul.y() + w));
		for(DynamicBody body : bodies) { 
			quad.addLocatedBody(body);
		}
		forcer = pf;
	}
	
	private BarnesHut(Area a, Iterable<DynamicBody> bodies, PairwiseForce pf) { 
		this(a.ul(), Math.max(a.width(), a.height()), bodies, pf);
	}
	
	public BarnesHut(Iterable<DynamicBody> bodies, PairwiseForce pf) { 
		this(boundingArea(bodies), bodies, pf);
	}
	
	/* (non-Javadoc)
	 * @see tdanford.physics.ForceField#calculateForce(tdanford.physics.LocatedBody)
	 */
	@Override
	public Vector calculateForce(DynamicBody body) { 
		return quad.calculateForce(body, theta, forcer);
	}
	
	public static Area boundingArea(Iterable<? extends LocatedBody> bodies) { 
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

class Quad extends Area { 
	
	private Quad[] subAreas;
	private DynamicBody body;

	private int bodyCount;
	private double totalMass;
	private double weightedX, weightedY;
	private Location centerOfMass;
	private DynamicBody staticCenter;
	
	private Quad(double x1, double y1, double x2, double y2) { 
		super(x1, y1, x2, y2);
		
		subAreas = null;
		body = null;
		bodyCount = 0;
		totalMass = 0.0;
		weightedX = weightedY = 0.0;
		centerOfMass = null;
	}
	
	public Quad(Area a) { 
		this(a.x1(), a.y1(), a.x2(), a.y2());
	}

	public Vector calculateForce(DynamicBody target, double theta, PairwiseForce forcer) { 
		if(isLeaf()) { 
			if(body != null && !body.equals(target)) {   
				return forcer.forceApplied(target, body);
				
			} else { 
				return new Vector();
			}
		} else { 
			double s = width();
			double d = centerOfMass.dist(target.location());
			
			if((s/d) < theta) { 
				return forcer.forceApplied(target, staticCenter); 
			} else {
				Vector v = new Vector();
				for(int i = 0; i < subAreas.length; i++) { 
					v = v.addTo(subAreas[i].calculateForce(target, theta, forcer));
				}
				return v;
			}
		}
	}
	
	public void addLocatedBody(DynamicBody lb) { 
		assert contains(lb.location());
		
		addToMass(lb.mass(), lb.location());
		
		if(isLeaf()) { 
			if(body == null) { 
				body = lb;
			} else {
				// subdivide.
				subAreas = createSubArray();
				subAreas[classifyLocation(body.location())].addLocatedBody(body);
				subAreas[classifyLocation(lb.location())].addLocatedBody(lb);
				body = null;
			}
		} else { 
			int lbIdx = classifyLocation(lb.location());
			subAreas[lbIdx].addLocatedBody(lb);
		}
	}

	public double cx() { return x1() + (x2()-x1())/2.0; }
	public double cy() { return y1() + (y2()-y1())/2.0; }
	
	public Location centerOfMass() { return centerOfMass; }
	public LocatedBody body() { return body; }
	public boolean isLeaf() { return subAreas == null; }
	
	private Area _nw() { return new Area(x1(), y1(), cx(), cy()); }
	private Area _ne() { return new Area(cx(), y1(), x2(), cy()); }
	private Area _sw() { return new Area(x1(), cy(), cx(), y2()); }
	private Area _se() { return new Area(cx(), cy(), x2(), y2()); }

	private void addToMass(double m, Location l) {
		bodyCount += 1;
		weightedX += (l.x() * m);
		weightedY += (l.y() * m);
		totalMass += m;
		
		assert totalMass > 0.0;
		
		centerOfMass = new Location(weightedX / totalMass, weightedY / totalMass);
		staticCenter = new DynamicBody("center-of-mass", totalMass, centerOfMass, new Vector());
	}
	
	private Quad[] createSubArray() { 
		return new Quad[] { 
				new Quad(_nw()),
				new Quad(_ne()),
				new Quad(_sw()),
				new Quad(_se()),
		};
	}
	
	private int classifyLocation(Location l) { 
		assert contains(l);
		if(l.x() < cx()) {
			if(l.y() < cy()) { 
				return 0;  // NW
			} else { 
				return 2;  // SW
			}
		} else { 
			if(l.y() < cy()) { 
				return 1;  // NE
			} else { 
				return 3;  // SE
			}
		}
	}
	
}


