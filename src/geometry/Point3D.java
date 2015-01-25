package geometry;


public class Point3D {
	public static Point3D ORIGIN = new Point3D(0, 0, 0);

	public double x;
	public double y;
	public double z;

	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param p
	 * @return Euclidian distance to the point p.
	 */
	public double distance(Point3D p) {
		return Math.sqrt(Math.pow(x - p.x, 2)
				+ Math.pow(y - p.y, 2)
				+ Math.pow(z - p.z, 2));
	}

	/**
	 * @param p1
	 * @param p2
	 * @return The middle between the point p1 and p2.
	 */
	public static Point3D mid(Point3D p1, Point3D p2) {
		return new Point3D((p1.x + p2.x) / 2, (p1.y + p2.y) / 2, (p1.z + p2.z) / 2);
	}

	public void move(Vector3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public Point3D sub(Point3D p) {
		return new Point3D(x - p.x, y - p.y, z - p.z);
	}

	/**
	 * @param vertices Point list.
	 * @return The barycenter of all the points given.
	 */
	public static Point3D barycenter(Point3D[] vertices) {
		double x = 0, y = 0, z = 0;

		for (Point3D p : vertices) {
			x += p.x;
			y += p.y;
			z += p.z;
		}

		return new Point3D(x / vertices.length, y / vertices.length, z / vertices.length);
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
}
