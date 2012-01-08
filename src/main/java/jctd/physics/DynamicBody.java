package jctd.physics;

public class DynamicBody extends LocatedBody { 
	
	private Vector velocity;
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
	
	public Vector velocity() { return velocity; }
	
	public Vector acceleration() { return acceleration; }
	
	public void setAcceleration(Vector a) { 
		acceleration = a;
		updateVelocity(acceleration);
	}
	
	public void updateVelocity(Vector a) { 
		setVelocity(velocity.addTo(a));
		updateLocation(velocity);
	}
	
	public void setVelocity(Vector v) { 
		velocity = v;
	}
}

