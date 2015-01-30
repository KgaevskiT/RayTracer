package geometry.primitive;

import geometry.Intersection;
import geometry.Point3D;
import geometry.Vector3D;

import java.awt.Color;

import material.Material;
import raytracer.Ray;

public class Plane extends Primitive {
	private double a;
	private double b;
	private double c;
	private double d;
	private Point3D point;

	public Plane(double a, double b, double c, double d, Material material) {
		super(material);
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public Plane(Point3D p, Vector3D n, Material material) {
		super(material);
		this.a = n.x;
		this.b = n.y;
		this.c = n.z;
		this.d = - (n.x * p.x) - (n.y * p.y) - (n.z * p.z);
		this.point = p;
	}

	@Override
	public Intersection intersect(Ray ray) {
		Point3D O = ray.getOrigin();
		Vector3D dir = ray.getDirection();

		double x = O.x - point.x;
		double y = O.y - point.y;
		double z = O.z - point.z;

		double denominator = a * dir.x + b * dir.y + c * dir.z;

		if (denominator == 0) {
			return null;
		}

		double t = - ((a * x) + (b * y) + (c * z) + d) / denominator;

		Point3D p = new Point3D(O.x + t * dir.x,
				O.y + t * dir.y,
				O.z + t * dir.z);

		if (t <= EPSILON) {
			return null;
		}

		Intersection intersection = new Intersection(this, p, O.distance(p));

		return intersection;
	}

	@Override
	public Color getColor(Intersection intersection) {
		return this.material.getColor();
	}

	@Override
	public double getCosine(Ray ray, Intersection intersection) {
		Vector3D normal = getNormal(null);
		double cosine = Vector3D.dot(ray.getDirection(), normal);

		if (cosine < 0) {
			normal.inverse();
			cosine = Vector3D.dot(ray.getDirection(), normal);
		}

		return cosine;
	}

	@Override
	public Vector3D getNormal(Intersection intersection) {
		return new Vector3D(a, b, c);
	}
}
