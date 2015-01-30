package raytracer;
import geometry.Point3D;
import geometry.Vector3D;


public class Ray {
	private Point3D origin;
	private Vector3D direction;
	private double currentRefractionIndex = 1;
	private boolean isInObject = false;

	public Ray(Point3D origin) {
		this.origin = origin;
		this.direction = null;
	}

	public Ray(Point3D origin, Vector3D direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Ray(Point3D origin, Vector3D direction, double refractionIndex, boolean isInObject) {
		this.origin = origin;
		this.direction = direction;
		this.currentRefractionIndex = refractionIndex;
		this.isInObject = isInObject;
	}

	public Point3D getOrigin() {
		return origin;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public double getRefractionIndex() {
		return this.currentRefractionIndex;
	}

	public boolean isInObject() {
		return this.isInObject;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}

	public void setInObject(boolean isInObject) {
		this.isInObject = isInObject;
	}

	public void setOrigin(Point3D origin) {
		this.origin = origin;
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
