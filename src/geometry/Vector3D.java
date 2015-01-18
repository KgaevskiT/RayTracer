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

	public Vector3D(Double x, Double y, Double z, boolean normalize) {
		this.x = x;
		this.y = y;
		this.z = z;

		if (normalize)
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

	/**
	 * Set the vector's length to 1
	 */
	private void normalize() {
		Double length = length();

		this.x /= length;
		this.y /= length;
		this.z /= length;
	}

	/**
	 * @param v1 First vector
	 * @param v2 Second vector
	 * @return Cross product between v1 and v2
	 */
	public static Vector3D cross(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.y * v2.z - v1.z * v2.y,
				v1.z * v2.x - v1.x * v2.z,
				v1.x * v2.y - v1.y * v2.x, false);
	}

	/**
	 * @param v1 First vector
	 * @param v2 Second vector
	 * @return Dot product between v1 and v2
	 */
	public static double dot(Vector3D v1, Vector3D v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	public static Vector3D mean(Vector3D v1, Vector3D v2, Vector3D v3) {
		return new Vector3D((v1.x + v2.x + v3.x) / 3, (v1.y + v2.y + v3.y) / 3,
				(v1.z + v2.z + v3.z) / 3);
	}

	public static Vector3D mean(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4) {
		return new Vector3D((v1.x + v2.x + v3.x + v4.x) / 3,
				(v1.y + v2.y + v3.y + v4.z) / 3,
				(v1.z + v2.z + v3.z + v4.z) / 3);
	}

	/**
	 * Substract the coordinates of the point p1 to the coordinates of p2
	 * @param p1
	 * @param p2
	 * @return The vector from the point p2 to the point p1
	 */
	public static Vector3D sub(Point3D p1, Point3D p2) {
		return new Vector3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z, false);
	}

	/**
	 * Inverse the vector direction
	 */
	public void inverse() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
	}

	/**
	 * Euclidian length
	 * @return The vector's length
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
}
