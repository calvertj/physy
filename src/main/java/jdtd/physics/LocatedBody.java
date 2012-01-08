package jdtd.physics;

public class LocatedBody extends Body { 
	
	private Location location;
	
	public LocatedBody(String n, double m, Location l) { 
		super(n, m);
		location = l;
	}
	
	public LocatedBody(Body b, Location l) { 
		this(b.name(), b.mass(), l);
	}
	
	public LocatedBody(LocatedBody b) { 
		this(b, b.location);
	}
	
	public Location location() { return location; }
	
	public void updateLocation(Vector v) { 
		location = location.translate(v);
	}
}