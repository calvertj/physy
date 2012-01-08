package jctd.physics;

public class Body {

	private String name;
	private double mass;
	private int radius;
	
	public Body(String n, double m) { 
		assert m > 0.0;
		assert n.length() > 0;

		name = n; mass = m;
		
		radius = (int)Math.ceil(Math.sqrt(mass));
	}
	
	public double mass() { return mass; }
	
	public int radius() { return radius; }
	
	public String name() { return name; }
	
	public int hashCode() { return name.hashCode(); }
	
	public String toString() { return name; }
	
	public boolean equals(Object o) { 
		if(!(o instanceof Body)) { return false; }
		Body b = (Body)o;
		return b.name.equals(name);
	}
}



