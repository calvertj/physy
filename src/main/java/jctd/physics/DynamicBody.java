package jctd.physics;

public class DynamicBody extends LocatedBody { 
	
	private Vector previousVelocity;
	private Vector velocity;
	
	private Vector previousAcceleration;
	private Vector acceleration;
	
	public DynamicBody(LocatedBody b, Vector v) { 
		super(b);
		velocity = v;
		acceleration = new Vector();
	}
	
	public DynamicBody(String n, double m, Location l, Vector v) { 
		super(n, m, l);
		velocity = v;
		acceleration = new Vector();
	}
	
	public Vector previousVelocity() { return previousVelocity; }
	public Vector velocity() { return velocity; }
	
	public Vector previousAcceleration() { return previousAcceleration; }
	public Vector acceleration() { return acceleration; }
	
	public void setAcceleration(Vector a) {
		previousAcceleration = acceleration;
		acceleration = a;
	}
	
	public void setVelocity(Vector v) { 
		previousVelocity = velocity;
		velocity = v;
	}
}

