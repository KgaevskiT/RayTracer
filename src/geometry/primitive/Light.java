package geometry.primitive;
import geometry.Point3D;


public class Light {
	private Point3D position;
	private Integer intensity;

	public Light(Point3D position, Integer intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	public Integer getIntensity() {
		return this.intensity;
	}

	public Point3D getPosition() {
		return position;
	}
}
