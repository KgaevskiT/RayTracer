package geometry;


public class Vector3D {
	public Double x;
	public Double y;
	public Double z;

	public Vector3D(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		normalize();
	}

	public Vector3D(Point3D p1, Point3D p2) {
		this.x = p2.x - p1.x;
		this.y = p2.y - p1.y;
		this.z = p2.z - p1.z;

		normalize();
	}

	public Vector3D(Point3D p1, Point3D p2, boolean normalize) {
		this.x = p2.x - p1.x;
		this.y = p2.y - p1.y;
		this.z = p2.z - p1.z;

		if (normalize)
			normalize();
	}

	private void normalize() {
		Double length = length();

		this.x /= length;
		this.y /= length;
		this.z /= length;
	}

	public Vector3D crossProduct(Vector3D v) {
		return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}

	public Double dotProduct(Vector3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public void inverse() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
	}

	public Double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
}
