package geometry.primitive;

import geometry.Intersection;
import geometry.Point3D;
import geometry.Vector3D;

import java.awt.Color;

import material.Material;
import raytracer.Ray;

public class Plane extends Primitive {
	private Double a;
	private Double b;
	private Double c;
	private Double d;
	private Point3D point;

	public Plane(Double a, Double b, Double c, Double d, Material material) {
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
		Point3D origin = ray.getOrigin();
		Vector3D direction = ray.getDirection();

		Double x = origin.x - point.x;
		Double y = origin.y - point.y;
		Double z = origin.z - point.z;

		Double denominator = a * direction.x + b * direction.y + c * direction.z;

		if (denominator == 0.0) {
			return null;
		}

		Double t = - ((a * x) + (b * y) + (c * z) + d) / denominator;

		Point3D p = new Point3D(origin.x + t * direction.x,
				origin.y + t * direction.y,
				origin.z + t * direction.z);

		if (t <= 0) {
			return null;
		}

		Intersection intersection = new Intersection(this, p, origin.distance(p));

		return intersection;
	}

	@Override
	public Double getCosine(Ray ray) {
		Vector3D n = new Vector3D(a, b, c);
		Double cos = n.dotProduct(ray.getDirection());

		if (cos >= 0)
			return cos;

		n.inverse();
		return n.dotProduct(ray.getDirection());
	}

	@Override
	public Color getColor(Point3D p) {
		return this.material.getColor();
	}

	@Override
	public Ray getReflected(Ray ray, Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ray getRefracted(Ray ray, Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

}
