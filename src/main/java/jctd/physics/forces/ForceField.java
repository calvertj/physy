package jctd.physics.forces;

import java.util.Random;

import jctd.physics.DynamicBody;
import jctd.physics.Location;
import jctd.physics.Predicate;
import jctd.physics.Vector;

public interface ForceField {
	public abstract Vector calculateForce(DynamicBody body);
}

class FieldSum implements ForceField {
	
	private ForceField[] fields;
	
	public FieldSum(ForceField... fs) { 
		fields = fs.clone();
	}
	
	public Vector calculateForce(DynamicBody body) {
		Vector v = new Vector();
		for(ForceField f : fields) { 
			v = v.addTo(f.calculateForce(body));
		}
		return v;
	} 
}

class ConditionalField implements ForceField {
	
	private Predicate<DynamicBody> fieldChoice;
	private ForceField acceptsField, rejectsField;
	
	public ConditionalField(Predicate<DynamicBody> cond, ForceField f1, ForceField f2) { 
		fieldChoice = cond;
		acceptsField = f1;
		rejectsField = f2;
	}

	public Vector calculateForce(DynamicBody body) {
		if(fieldChoice.accepts(body)) { 
			return acceptsField.calculateForce(body);
		} else { 
			return rejectsField.calculateForce(body);
		}
	}
}

class Floor extends ConditionalField { 
	public Floor(final double fy, double fstr, double str) { 
		super(new Predicate<DynamicBody>() {
			public boolean accepts(DynamicBody value) {
				return value.location().y() >= fy;
			} 
		}, 
		new WallField(fy, fstr, false, false),
		new FloorGravity(fy, str));
	}
}

class FloorGravity implements ForceField {
	
	private double floorY;
	private double strength;
	
	public FloorGravity(double fy, double str) { 
		floorY = fy;
		strength = str;
	}

	public Vector calculateForce(DynamicBody body) {
		double dy = floorY - body.location().y();
		Vector r = new Vector(0.0, dy).normalizeToLength(strength);
		return r;
	} 
}

class GravityWell implements ForceField {
	
	private Location center;
	private double strength;
	
	public GravityWell(Location c, double s) { 
		center = c;
		strength = s;
	}
	
	public Vector calculateForce(DynamicBody body) {
		Vector r = body.location().translationFrom(center);
		return r.scale(-strength / body.location().d2(center));
	} 
}

class NegativeForceField implements ForceField { 
	private ForceField innerField;
	public NegativeForceField(ForceField ff) { 
		innerField = ff;
	}
	public Vector calculateForce(DynamicBody body) {
		return innerField.calculateForce(body).scale(-1.0);
	}
}

class BrownianField implements ForceField { 
	private double radius;
	private Random rand;
	public BrownianField(double r) { 
		radius = r;
		rand = new Random();
	}
	public Vector calculateForce(DynamicBody body) {
		double rx = (rand.nextGaussian() * radius);
		double ry = (rand.nextGaussian() * radius);
		return new Vector(rx, ry);
	}
}

class FrictionField implements ForceField {
	
	public double strength;
	
	public FrictionField(double s) { 
		strength = s;
	}

	public Vector calculateForce(DynamicBody body) {
		return body.velocity().scale(-1.0 * strength);
	} 
}

class WallField implements ForceField {
	
	public final boolean isX, isLessThan;
	public final double strength;
	public final double coordinate;
	
	public WallField(double c, double s, boolean x, boolean lessThan) { 
		coordinate = c;
		strength = s;
		isX = x;
		isLessThan = lessThan;
	}
	
	public Vector calculateForce(DynamicBody body) {
		double bcoord = isX ? body.location().x() : body.location().y();
		double d = isLessThan ? coordinate - bcoord : bcoord - coordinate;
		boolean wallCrossed = d > 0.0;
		double m = isLessThan ? 1 : -1;
		
		if(wallCrossed) {
			//double f = (d * strength * m);
			double f = strength * m;
			if(isX) { 
				return new Vector(f, 0.0);
			} else { 
				return new Vector(0.0, f);
			}
		} else { 
			return new Vector();
		}
	} 
}