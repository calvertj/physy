package jdtd.physics;

import java.util.Random;

public class Area {
	
	public static Random rand = new Random();

	private double x1, y1, x2, y2;
	
	public Area(double x1, double y1, double x2, double y2) {
		assert x1 < x2; 
		assert y1 < y2;
		
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public double x1() { return x1; }
	public double y1() { return y1; }
	public double x2() { return x2; }
	public double y2() { return y2; }
	
	public boolean contains(Location l) { 
		return x1 <= l.x() && l.x() < x2 && 
			y1 <= l.y() && l.y() < y2;
	}
	
	public Area expand(double dw, double dh) { 
		return new Area(x1-dw, y1-dh, x2+dw, y2+dh);
	}
	
	public double width() { return x2- x1; }
	public double height() { return y2 - y1; }
	public double area() { return width() * height(); }
	
	public Location ul() { return new Location(x1, y1); }
	public Location ur() { return new Location(x2, y1); }
	public Location ll() { return new Location(x1, y2); }
	public Location lr() { return new Location(x2, y2); }
	
	public Location center() { 
		return new Location(x1 + (x2-x1)/2.0, y1 + (y2-y1)/2.0);
	}
	
	public Location randomLocation() { 
		double x = x1 + rand.nextDouble() * width();
		double y = y1 + rand.nextDouble() * height();
		return new Location(x, y);
	}
}



