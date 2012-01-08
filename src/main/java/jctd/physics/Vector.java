package jctd.physics;

public class Vector {

	private double x, y;
	
	public Vector(double x, double y) { 
		this.x = x;
		this.y = y;
	}
	
	public Vector() { this(0.0, 0.0); }
	
	public double x() { return x; }
	public double y() { return y; }
	
	public Vector addTo(Vector v) { 
		return new Vector(x + v.x, y + v.y);
	}
	
	public Vector subtract(Vector v) { 
		return addTo(v.scale(-1.0));
	}
	
	public Vector add(Vector ... vs) { 
		Vector added = this;
		for(int i = 0; i < vs.length; i++) { 
			added = added.addTo(vs[i]);
		}
		return added;
	}
	
	public Vector scale(double s) { 
		return new Vector(x*s, y*s);
	}
	
	public Vector normalize() {
		return normalizeToLength(1.0);
	}
	
	public Vector normalizeToLength(double l) { 
		double m = l / length();
		return new Vector(x * m, y * m);		
	}
	
	public double length() { 
		return Math.sqrt(l2());
	}
	
	public double l2() { return x*x + y*y; }
	
	public int hashCode() { 
		int code = 17;
		long bits = Double.doubleToLongBits(x);
		code += (int)(bits >> 32); code *= 37;
		code += (int)((Double.doubleToLongBits(y)) >> 32); code *= 37;
		return code;
	}
	
	public boolean equals(Object o) { 
		if(!(o instanceof Vector)) return false; 
		Vector l = (Vector)o;
		return x == l.x && y == l.y;
	}
	
	public String toString() { return String.format("%.3f,%.3f", x, y); }
}
