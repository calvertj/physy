package jctd.physics;

import java.util.LinkedList;

public class DynamicHistory { 
	
	public static final int MAX_LOCATIONS=50;
	
	public LinkedList<Location> pastLocations;
	
	public DynamicHistory() { 
		pastLocations = new LinkedList<Location>();
	}
	
	public void updateLocation(Location loc) { 
		pastLocations.addFirst(loc);
		if(pastLocations.size() >= MAX_LOCATIONS) { 
			pastLocations.removeLast();
		}
	}
}