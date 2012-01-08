package jdtd.physics;

public class SquareArea extends Area {
	public static double maxDim(Area a) { return Math.max(a.width(), a.height()); }
	public SquareArea(Area a) { 
		super(a.x1(), a.y1(), a.x1()+maxDim(a), a.y1()+maxDim(a));
	}
}