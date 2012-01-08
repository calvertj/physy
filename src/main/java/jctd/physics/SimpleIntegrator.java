package jctd.physics;

public class SimpleIntegrator implements Integrator {

	public void update(DynamicBody body, Vector F) {
		Vector a1 = F.scale(1.0 / body.mass());
		Vector v1 = body.velocity().addTo(a1);
		Location x1 = body.location().translate(v1);
		
		body.setAcceleration(a1);
		body.setVelocity(v1);
		body.setLocation(x1);
	} 
}