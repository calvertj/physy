package jdtd.physics.forces;

import jdtd.physics.Area;

public class BoxField extends FieldSum { 
	
	private final double strength;
	private final Area area;
	
	public BoxField(Area a, double s) { 
		super(new WallField(a.x1(), s, true, true),
				new WallField(a.y1(), s, false, true),
				new WallField(a.x2(), s, true, false),
				new WallField(a.y2(), s, false, false));
		area = a;
		strength = s;
	}
}