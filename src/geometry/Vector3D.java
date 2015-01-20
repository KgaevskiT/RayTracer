package geometry;


public class Vector3D {
	public double x;
	public double y;
	public double z;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		normalize();
	}

	public Vector3D(double x, double y, double z, boolean normalize) {
		this.x = x;
		this.y = y;
		this.z = z;

		if (normalize)
			normalize();
	}

	/**
	 * Create a unitary Vector from the point p1 to p2.
	 * @param p1
	 * @param p2
	 */
	public Vector3D(Point3D p1, Point3D p2) {
		this.x = p2.x - p1.x;
		this.y = p2.y - p1.y;
		this.z = p2.z - p1.z;

		normalize();
	}

	/**
	 * Create a Vector from the point p1 to p2.
	 * @param p1
	 * @param p2
	 * @param normalize Normalize the vector if true.
	 */
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
	public void normalize() {
		Double length = length();

		this.x /= length;
		this.y /= length;
		this.z /= length;
	}

	/**
	 * @param vectors A vector list.
	 * @return The sum of all the vectors.
	 */
	public static Vector3D add(Vector3D[] vectors) {
		double x = 0, y = 0, z = 0;
		for (Vector3D v : vectors) {
			x += v.x;
			y += v.y;
			z += v.z;
		}
		return new Vector3D(x, y, z, false);
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
	 * @param v1 First vector.
	 * @param v2 Second vector.
	 * @return Dot product between two vectors.
	 */
	public static double dot(Vector3D v1, Vector3D v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/**
	 * @param vectors The vector list.
	 * @return The mean of all vectors.
	 */
	public static Vector3D mean(Vector3D[] vectors) {
		double x = 0, y = 0, z = 0;

		for (Vector3D v : vectors) {
			x += v.x;
			y += v.y;
			z += v.z;
		}

		return new Vector3D(x / vectors.length, y / vectors.length, z / vectors.length, false);
	}

	/**
	 * @param scalar Real number
	 * @param vector Vector
	 * @return The vector v multiplied by the scalar.
	 */
	public static Vector3D mul(double scalar, Vector3D vector) {
		return new Vector3D(scalar * vector.x, scalar * vector.y, scalar * vector.z, false);
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
	 * @param v1 First vector
	 * @param v2 Second vector
	 * @return The second vector subtracted to the first vector.
	 */
	public static Vector3D sub(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, false);
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
	 * @return The vector's Euclidian length
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
}
