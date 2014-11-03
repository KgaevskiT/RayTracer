package geometry;
import geometry.primitive.Primitive;


public class Intersection {
	private Primitive object;
	private Point3D point;
	private Double distance;

	public Intersection(Primitive object, Point3D point, Double distance) {
		this.object = object;
		this.point = point;
		this.distance = distance;
	}

	public Double getDistance() {
		return distance;
	}

	public Primitive getObject() {
		return this.object;
	}

	public Point3D getPoint() {
		return point;
	}
}
