package jdtd.physics;

public class DynamicBody extends LocatedBody { 
	
	private Vector velocity;
	
	public DynamicBody(LocatedBody b, Vector v) { 
		super(b);
		velocity = v;
	}
	
	public DynamicBody(String n, double m, Location l, Vector v) { 
		super(n, m, l);
		velocity = v;
	}
	
	public Vector velocity() { return velocity; }
	
	public void updateVelocity(Vector a) { 
		velocity = velocity.addTo(a);
		updateLocation(velocity);
	}
	
	public void setVelocity(Vector v) { 
		velocity = v;
	}
}