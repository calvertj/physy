package jctd.physics;

public interface Integrator { 
	public void update(DynamicBody body, Vector F);
}

class BasicIntegrator implements Integrator {

	public void update(DynamicBody body, Vector F) {
		Vector a = F.scale(1.0 / body.mass());
		body.setAcceleration(a);
	} 
}