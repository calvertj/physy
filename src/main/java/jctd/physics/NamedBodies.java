package jctd.physics;

public interface NamedBodies<BodyType extends Body> {
	public BodyType getBody(String name);
}
