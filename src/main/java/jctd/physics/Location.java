package jctd.physics;

public class Location { 
	
	private double x, y;
	
	public Location(double x, double y) { 
		this.x = x;
		this.y = y;
	}
	
	public double x() { return x; }
	public double y() { return y; }
	
	public Location translate(Vector v) { 
		return new Location(x + v.x(), y + v.y());
	}
	
	public Vector translationFrom(Location base) { 
		return new Vector(x - base.x, y - base.y);
	}
	
	public int hashCode() { 
		int code = 17;
		long bits = Double.doubleToLongBits(x);
		code += (int)(bits >> 32); code *= 37;
		code += (int)((Double.doubleToLongBits(y)) >> 32); code *= 37;
		return code;
	}
	
	public boolean equals(Object o) { 
		if(!(o instanceof Location)) return false; 
		Location l = (Location)o;
		return x == l.x && y == l.y;
	}
	
	public String toString() { return String.format("%.3f,%.3f", x, y); }
	
	public double d2(Location l) { 
		double dx = x - l.x, dy = y - l.y;
		return (dx*dx) + (dy*dy);
	}
	
	public double dist(Location l) { 
		return Math.sqrt(d2(l));
	}
}
