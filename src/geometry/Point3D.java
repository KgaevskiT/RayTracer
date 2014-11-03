package geometry;


public class Point3D {
	public Double x;
	public Double y;
	public Double z;

	public Point3D(int x, int y, int z) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}

	public Point3D(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Double distance(Point3D p) {
		return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2) + Math.pow(z - p.z, 2));
	}

	public Point3D sub(Point3D p) {
		return new Point3D(x - p.x, y - p.y, z - p.z);
	}

	@Override
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
}
