package geometry;
import geometry.primitive.Primitive;


public class Intersection {
	private Primitive object;
	private int groupId;
	private int faceId;
	private Point3D point;
	private double distance;
	private Vector3D normal;

	public Intersection(Primitive object, Point3D point, double distance) {
		this.object = object;
		this.faceId = -1;
		this.point = point;
		this.distance = distance;
		this.normal = null;
	}

	public Intersection(Primitive object, int faceId, Point3D point, double distance) {
		this.object = object;
		this.faceId = faceId;
		this.point = point;
		this.distance = distance;
		this.normal = null;
	}

	public double getDistance() {
		return this.distance;
	}

	public int getFaceId() {
		return faceId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public Primitive getObject() {
		return this.object;
	}

	public Point3D getPoint() {
		return this.point;
	}

	public Vector3D getNormal(Primitive primitive) {
		if (this.normal == null)
			this.normal = primitive.getNormal(this);
		return this.normal;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
