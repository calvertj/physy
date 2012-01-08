package jctd.physics;

public class VerletIntegrator implements Integrator {

	public void update(DynamicBody body, Vector F) {
		double dt = 1.0;

		Vector a2 = F.scale(1.0 / body.mass());
		Vector x1 = body.location().asVector();
		Vector v1 = body.velocity();
		
		Vector x0 = null, x2 = null;
		Vector v2 = null;

		if(!body.hasPreviousLocation()) { 
			x2 = x1.add(v1.scale(dt), a2.scale(0.5));
			
			v2 = (x2.subtract(x1)).scale(1.0 / (dt));

		} else { 
			x0 = body.previousLocation().asVector();
			x2 = x1.scale(2.0).add(x1.scale(-1.0), a2.scale(dt*dt));

			v2 = (x2.subtract(x0)).scale(1.0 / (2.0 * dt));
		}

		body.setAcceleration(a2);
		body.setVelocity(v2);
		body.setLocation(new Location(x2.x(), x2.y()));
	} 
}