package jctd.physics;

public class LocatedBody extends Body { 
	
	private Location previous;
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
	
	public boolean hasPreviousLocation() { return previous != null; }
	
	public Location previousLocation() { return previous; }
	public Location location() { return location; }
	
	public void setLocation(Location loc) { 
		previous = location;
		location = loc;
	}
}