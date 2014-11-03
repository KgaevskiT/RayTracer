package raytracer;
import geometry.Point3D;
import geometry.Vector3D;


public class Ray {
	private Point3D origin;
	private Vector3D direction;
	private Double currentRefractionIndex;

	public Ray(Point3D origin) {
		this.origin = origin;
		this.direction = null;
		this.currentRefractionIndex = 1.0;
	}

	public Ray(Point3D origin, Vector3D direction) {
		this.origin = origin;
		this.direction = direction;
		this.currentRefractionIndex = 1.0;
	}

	public Ray(Point3D origin, Vector3D direction, double refractionIndex) {
		this.origin = origin;
		this.direction = direction;
		this.currentRefractionIndex = refractionIndex;
	}

	public Point3D getOrigin() {
		return origin;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public Double getRefractionIndex() {
		return this.currentRefractionIndex;
	}

	public void setOrigin(Point3D origin) {
		this.origin = origin;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Ray{origin=");
		sb.append(origin);
		sb.append(",direction=");
		sb.append(direction);
		sb.append("}");

		return sb.toString();
	}
}
