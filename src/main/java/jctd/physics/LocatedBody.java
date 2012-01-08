package jctd.physics;

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
	
	public void setLocation(Location loc) { 
		location = loc;
	}
	
	public void updateLocation(Vector v) { 
		setLocation(location.translate(v));
	}
}